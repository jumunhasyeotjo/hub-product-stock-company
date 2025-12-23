import http from 'k6/http';
import { check, sleep, group } from 'k6';
import { Counter, Rate, Trend } from 'k6/metrics';

/**
 * HubService 캐시 성능 벤치마크
 * 
 * 사용법:
 * 1. 캐시 타입 전환: curl -X PUT "localhost:8088/internal/api/v1/dynamic/hub?type=CAFFEINE"
 * 2. 테스트 실행: k6 run --env CACHE_TYPE=CAFFEINE hub-cache-benchmark.js
 * 3. 다른 타입으로 반복
 */

// ==================== 설정 ====================
const BASE_URL = __ENV.BASE_URL || 'http://localhost:8088';
const CACHE_TYPE = __ENV.CACHE_TYPE || 'UNKNOWN';

// 테스트용 Hub ID (실제 존재하는 ID로 변경 필요)
const TEST_HUB_ID = __ENV.HUB_ID || 'a73979b2-518d-495e-84be-de3c026acc94';
const hubUpdateSeq = new Counter('hub_update_seq');

// ==================== 커스텀 메트릭 ====================
const readLatency = new Trend('hub_read_latency', true);
const writeLatency = new Trend('hub_write_latency', true);
const searchLatency = new Trend('hub_search_latency', true);
const getAllLatency = new Trend('hub_getall_latency', true);

const readErrors = new Counter('hub_read_errors');
const writeErrors = new Counter('hub_write_errors');
const successRate = new Rate('hub_success_rate');

// ==================== 테스트 시나리오 ====================
export const options = {
    scenarios: {
        // 시나리오 1: 읽기 집중 (캐시 효과 측정)
        read_heavy: {
            executor: 'constant-vus',
            vus: 500,
            duration: '30s',
            exec: 'readHeavyScenario',
            tags: { scenario: 'read_heavy' },
        },
        
        // 시나리오 2: 쓰기 후 읽기 (캐시 무효화 측정)
        write_then_read: {
            executor: 'constant-vus',
            vus: 250,
            duration: '30s',
            startTime: '35s',
            exec: 'writeThenReadScenario',
            tags: { scenario: 'write_then_read' },
        },
        
        // 시나리오 3: 혼합 워크로드 (실제 사용 패턴)
        mixed_workload: {
            executor: 'ramping-vus',
            startVUs: 1,
            stages: [
                { duration: '10s', target: 500 },
                { duration: '20s', target: 1000 },
                { duration: '10s', target: 500 },
                { duration: '10s', target: 0 },
            ],
            startTime: '70s',
            exec: 'mixedWorkloadScenario',
            tags: { scenario: 'mixed' },
        },
        
        // 시나리오 4: 부하 테스트 (최대 처리량)
        stress_test: {
            executor: 'ramping-arrival-rate',
            startRate: 10,
            timeUnit: '1s',
            preAllocatedVUs: 50,
            maxVUs: 100,
            stages: [
                { duration: '10s', target: 2500 },
                { duration: '20s', target: 5000 },
                { duration: '10s', target: 2500 },
                { duration: '10s', target: 0 },
            ],
            startTime: '130s',
            exec: 'stressTestScenario',
            tags: { scenario: 'stress' },
        },
    },
    
    thresholds: {
        'hub_read_latency': ['p(95)<200', 'p(99)<500'],
        'hub_success_rate': ['rate>0.95'],
        'http_req_duration': ['p(95)<300'],
    },
    
    tags: {
        cache_type: CACHE_TYPE,
    },
};

// ==================== 헬퍼 함수 ====================
const headers = {
    'Content-Type': 'application/json',
};

function checkResponse(res, name) {
    const success = check(res, {
        [`${name} status 2xx`]: (r) => r.status >= 200 && r.status < 300,
        [`${name} has body`]: (r) => r.body && r.body.length > 0,
    });
    successRate.add(success);
    return success;
}

// ==================== API 호출 함수 ====================

// 단건 조회 (캐시 히트 측정)
function getHubById(hubId) {
    const start = Date.now();
    const res = http.get(`${BASE_URL}/api/v1/hubs/${hubId}`, { headers, tags: { name: 'GetHubById' } });
    readLatency.add(Date.now() - start);
    
    if (!checkResponse(res, 'GetHubById')) {
        readErrors.add(1);
    }
    return res;
}

// 전체 조회 (캐시 효과 큼)
function getAllHubs() {
    const start = Date.now();
    const res = http.get(`${BASE_URL}/api/v1/hubs`, { headers, tags: { name: 'GetAllHubs' } });
    getAllLatency.add(Date.now() - start);
    
    checkResponse(res, 'GetAllHubs');
    return res;
}

// 검색 (캐시 미적용)
function searchHubs(name) {
    const start = Date.now();
    const res = http.get(`${BASE_URL}/api/v1/hubs/search?name=${encodeURIComponent(name)}`, { headers, tags: { name: 'SearchHubs' } });
    searchLatency.add(Date.now() - start);
    
    checkResponse(res, 'SearchHubs');
    return res;
}

// 수정 (캐시 무효화)
function updateHub(hubId) {
    const start = Date.now();
    const seq = hubUpdateSeq.add(1); // 원자적 증가
    const payload = JSON.stringify({
        hubId: hubId,
        name: `TestHub-${seq}`,
        address: '서울시 강남구 테스트로 123',
        latitude: 37.5665 + (Math.random() * 0.01),
        longitude: 126.9780 + (Math.random() * 0.01),
    });
    
    const res = http.patch(`${BASE_URL}/api/v1/hubs`, payload, { headers, tags: { name: 'UpdateHub' } });
    writeLatency.add(Date.now() - start);
    
    if (!checkResponse(res, 'UpdateHub')) {
        writeErrors.add(1);
    }
    return res;
}

// ==================== 시나리오 구현 ====================

// 시나리오 1: 읽기 집중
export function readHeavyScenario() {
    group('Read Heavy', () => {
        // 단건 조회 반복 (캐시 히트율 측정)
        for (let i = 0; i < 5; i++) {
            getHubById(TEST_HUB_ID);
            sleep(0.1);
        }
        
        // 전체 조회
        getAllHubs();
        sleep(0.2);
    });
}

// 시나리오 2: 쓰기 후 읽기
export function writeThenReadScenario() {
    group('Write Then Read', () => {
        // 쓰기 (캐시 무효화)
        updateHub(TEST_HUB_ID);
        sleep(0.1);
        
        // 읽기 (캐시 미스 예상)
        getHubById(TEST_HUB_ID);
        sleep(0.1);
        
        // 다시 읽기 (캐시 히트 예상)
        getHubById(TEST_HUB_ID);
        sleep(0.2);
    });
}

// 시나리오 3: 혼합 워크로드
export function mixedWorkloadScenario() {
    group('Mixed Workload', () => {
        const rand = Math.random();
        
        if (rand < 0.7) {
            // 70% 읽기
            getHubById(TEST_HUB_ID);
        } else if (rand < 0.85) {
            // 15% 전체 조회
            getAllHubs();
        } else if (rand < 0.95) {
            // 10% 검색
            searchHubs('경기 남부 센터');
        } else {
            // 5% 수정
            updateHub(TEST_HUB_ID);
        }
        
        sleep(0.1);
    });
}

// 시나리오 4: 스트레스 테스트
export function stressTestScenario() {
    group('Stress Test', () => {
        getHubById(TEST_HUB_ID);
    });
}

// ==================== 테스트 전후 처리 ====================
export function setup() {
    console.log(`\n========================================`);
    console.log(`  Cache Type: ${CACHE_TYPE}`);
    console.log(`  Base URL: ${BASE_URL}`);
    console.log(`  Test Hub ID: ${TEST_HUB_ID}`);
    console.log(`========================================\n`);
    
    // 현재 캐시 타입 확인
    const res = http.get(`${BASE_URL}/api/v1/dynamic`);
    if (res.status === 200) {
        console.log(`Current config: ${res.body}`);
    }
    
    // 웜업
    console.log('Warming up...');
    for (let i = 0; i < 10; i++) {
        http.get(`${BASE_URL}/api/v1/hubs/${TEST_HUB_ID}`);
    }
    
    return { startTime: Date.now() };
}

export function teardown(data) {
    const duration = (Date.now() - data.startTime) / 1000;
    console.log(`\n========================================`);
    console.log(`  Test completed in ${duration.toFixed(2)}s`);
    console.log(`  Cache Type: ${CACHE_TYPE}`);
    console.log(`========================================\n`);
}

// ==================== 기본 실행 ====================
export default function () {
    mixedWorkloadScenario();
}
