import http from 'k6/http';
import { check, sleep, group } from 'k6';
import { Counter, Rate, Trend } from 'k6/metrics';

/**
 * StockVariationService 락 전략 벤치마크
 * 
 * 비교 대상:
 * - DEFAULT: Atomic Update (락 없음)
 * - PESSIMISTIC_LOCK: 비관적 락
 * 
 * 사용법:
 * 1. 락 타입 전환: curl -X PUT "localhost:8088/internal/api/v1/dynamic/stock?type=DEFAULT"
 * 2. 테스트 실행: k6 run --env LOCK_TYPE=DEFAULT stock-lock-benchmark.js
 */

// ==================== 설정 ====================
const BASE_URL = __ENV.BASE_URL || 'http://localhost:8088';
const LOCK_TYPE = __ENV.LOCK_TYPE || 'UNKNOWN';

// 테스트용 Product ID (실제 존재하는 ID로 변경 필요)
const TEST_PRODUCT_ID = __ENV.PRODUCT_ID || 'test-product-uuid';

// ==================== 커스텀 메트릭 ====================
const incrementLatency = new Trend('stock_increment_latency', true);
const decrementLatency = new Trend('stock_decrement_latency', true);

const incrementErrors = new Counter('stock_increment_errors');
const decrementErrors = new Counter('stock_decrement_errors');
const lockConflicts = new Counter('stock_lock_conflicts');
const successRate = new Rate('stock_success_rate');

// ==================== 테스트 시나리오 ====================
export const options = {
    scenarios: {
        // 시나리오 1: 동시 증가 (동시성 테스트)
        concurrent_increment: {
            executor: 'constant-vus',
            vus: 20,
            duration: '30s',
            exec: 'concurrentIncrementScenario',
            tags: { scenario: 'concurrent_increment' },
        },
        
        // 시나리오 2: 동시 감소 (재고 차감 경쟁)
        concurrent_decrement: {
            executor: 'constant-vus',
            vus: 20,
            duration: '30s',
            startTime: '35s',
            exec: 'concurrentDecrementScenario',
            tags: { scenario: 'concurrent_decrement' },
        },
        
        // 시나리오 3: 혼합 (증가/감소 동시)
        mixed_operations: {
            executor: 'constant-vus',
            vus: 20,
            duration: '30s',
            startTime: '70s',
            exec: 'mixedOperationsScenario',
            tags: { scenario: 'mixed' },
        },
        
        // 시나리오 4: 고부하 동시성 (락 경합 최대화)
        high_contention: {
            executor: 'ramping-vus',
            startVUs: 10,
            stages: [
                { duration: '10s', target: 30 },
                { duration: '20s', target: 50 },
                { duration: '10s', target: 30 },
                { duration: '10s', target: 0 },
            ],
            startTime: '105s',
            exec: 'highContentionScenario',
            tags: { scenario: 'high_contention' },
        },
    },
    
    thresholds: {
        'stock_increment_latency': ['p(95)<500', 'p(99)<1000'],
        'stock_decrement_latency': ['p(95)<500', 'p(99)<1000'],
        'stock_success_rate': ['rate>0.90'],
    },
    
    tags: {
        lock_type: LOCK_TYPE,
    },
};

// ==================== 헬퍼 함수 ====================
const headers = {
    'Content-Type': 'application/json',
    'X-User-Id': '1',
};

function checkResponse(res, name) {
    const success = check(res, {
        [`${name} status 2xx`]: (r) => r.status >= 200 && r.status < 300,
    });
    
    // 락 충돌 감지 (409 Conflict 또는 특정 에러)
    if (res.status === 409 || (res.body && res.body.includes('lock'))) {
        lockConflicts.add(1);
    }
    
    successRate.add(success);
    return success;
}

// ==================== API 호출 함수 ====================

// 재고 증가
function incrementStock(productId, amount) {
    const start = Date.now();
    const payload = JSON.stringify({
        productId: productId,
        amount: amount || 1,
    });
    
    const res = http.post(
        `${BASE_URL}/api/v1/stocks/increment`,
        payload,
        { headers, tags: { name: 'IncrementStock' } }
    );
    
    incrementLatency.add(Date.now() - start);
    
    if (!checkResponse(res, 'IncrementStock')) {
        incrementErrors.add(1);
    }
    return res;
}

// 재고 감소
function decrementStock(productId, amount) {
    const start = Date.now();
    const payload = JSON.stringify({
        productId: productId,
        amount: amount || 1,
    });
    
    const res = http.post(
        `${BASE_URL}/api/v1/stocks/decrement`,
        payload,
        { headers, tags: { name: 'DecrementStock' } }
    );
    
    decrementLatency.add(Date.now() - start);
    
    if (!checkResponse(res, 'DecrementStock')) {
        decrementErrors.add(1);
    }
    return res;
}

// ==================== 시나리오 구현 ====================

// 시나리오 1: 동시 증가
export function concurrentIncrementScenario() {
    group('Concurrent Increment', () => {
        incrementStock(TEST_PRODUCT_ID, 1);
        // 딜레이 없이 최대한 빠르게 요청
    });
}

// 시나리오 2: 동시 감소
export function concurrentDecrementScenario() {
    group('Concurrent Decrement', () => {
        decrementStock(TEST_PRODUCT_ID, 1);
    });
}

// 시나리오 3: 혼합 작업
export function mixedOperationsScenario() {
    group('Mixed Operations', () => {
        if (Math.random() < 0.5) {
            incrementStock(TEST_PRODUCT_ID, 1);
        } else {
            decrementStock(TEST_PRODUCT_ID, 1);
        }
    });
}

// 시나리오 4: 고부하 동시성
export function highContentionScenario() {
    group('High Contention', () => {
        // 같은 상품에 대해 빠르게 연속 요청
        for (let i = 0; i < 3; i++) {
            if (Math.random() < 0.5) {
                incrementStock(TEST_PRODUCT_ID, 1);
            } else {
                decrementStock(TEST_PRODUCT_ID, 1);
            }
        }
    });
}

// ==================== 테스트 전후 처리 ====================
export function setup() {
    console.log(`\n========================================`);
    console.log(`  Lock Type: ${LOCK_TYPE}`);
    console.log(`  Base URL: ${BASE_URL}`);
    console.log(`  Test Product ID: ${TEST_PRODUCT_ID}`);
    console.log(`========================================\n`);
    
    // 현재 설정 확인
    const res = http.get(`${BASE_URL}/internal/api/v1/dynamic`);
    if (res.status === 200) {
        console.log(`Current config: ${res.body}`);
    }
    
    // 초기 재고 설정 (충분히 높게)
    console.log('Setting up initial stock...');
    for (let i = 0; i < 100; i++) {
        incrementStock(TEST_PRODUCT_ID, 100);
    }
    
    return { startTime: Date.now() };
}

export function teardown(data) {
    const duration = (Date.now() - data.startTime) / 1000;
    console.log(`\n========================================`);
    console.log(`  Test completed in ${duration.toFixed(2)}s`);
    console.log(`  Lock Type: ${LOCK_TYPE}`);
    console.log(`========================================\n`);
}

// ==================== 기본 실행 ====================
export default function () {
    mixedOperationsScenario();
}
