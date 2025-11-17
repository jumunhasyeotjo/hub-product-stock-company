import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate } from 'k6/metrics';

const errorRate = new Rate('errors');
// k6 run script/stock.js
export let options = {
    scenarios: {
        // 재고 차감 성능테스트
        ramping_test: {
            executor: 'ramping-vus',
            startVUs: 0,
            stages: [
                { duration: '30s', target: 200 },
            ],
        },
    },
    thresholds: {
        'http_req_duration': ['p(95)<1000', 'p(99)<2000'],
        'http_req_failed': ['rate<0.05'],
        'errors': ['rate<0.1'],
    },
};

export default function () {
    const payload = JSON.stringify({
        productId: '4689cfcf-b308-453b-8dc3-56a7cc027df4',
        amount: 1,
    });

    const params = {
        headers: {
            'Content-Type': 'application/json',
        },
        timeout: '30s',
    };

    let res = http.post('http://localhost:8080/api/v1/stocks/decrement', payload, params);

    let success = check(res, {
        'status is 200': (r) => r.status === 200,
        'response time < 1s': (r) => r.timings.duration < 1000,
    });

    errorRate.add(!success);

    // think time 없이 최대 부하 테스트
}