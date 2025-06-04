import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate, Trend } from 'k6/metrics';
import { randomIntBetween } from 'https://jslib.k6.io/k6-utils/1.2.0/index.js';

// Custom metrics
const orderSuccessRate = new Rate('order_success_rate');
const orderTrend = new Trend('order_trend');
const seatCheckTrend = new Trend('seat_check_trend');

// Test configuration
export const options = {
    stages: [
        { duration: '5m', target: 500 },  // Warm-up
        { duration: '10m', target: 2000 }, // Ramp-up
        { duration: '15m', target: 3000 }, // Peak load
        { duration: '30m', target: 3000 }, // Sustained load
        { duration: '5m', target: 0 },    // Ramp-down
    ],
    thresholds: {
        'order_success_rate': ['rate>0.99'],  // 99% success rate
        'order_trend': ['p(95)<200'],        // 95% of orders under 200ms
        'seat_check_trend': ['p(95)<100'],   // 95% of seat checks under 100ms
        'http_req_duration': ['p(95)<500'],  // 95% of all requests under 500ms
    },
};

// Test data generation
function generateOrderPayload() {
    const seatId = `${randomIntBetween(1, 100)}/${['A', 'B', 'C', 'D', 'E'][randomIntBetween(0, 4)]}`;
    return JSON.stringify({
        userId: `user_${randomIntBetween(1, 1000)}`,
        seatId: seatId,
        timestamp: new Date().toISOString(),
        eventId: `event_${randomIntBetween(1, 10)}`
    });
}

// Main test function
export default function () {
    const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';
    const headers = {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
    };

    // Order creation test
    {
        const payload = generateOrderPayload();
        const orderResponse = http.post(`${BASE_URL}/api/orders`, payload, {
            headers: headers,
            tags: { name: 'create_order' }
        });

        orderSuccessRate.add(orderResponse.status === 200);
        orderTrend.add(orderResponse.timings.duration);

        check(orderResponse, {
            'order status is 200': (r) => r.status === 200,
            'order response time < 200ms': (r) => r.timings.duration < 200,
        });
    }

    // Seat availability check
    {
        const seatId = `${randomIntBetween(1, 100)}/${['A', 'B', 'C', 'D', 'E'][randomIntBetween(0, 4)]}`;
        const seatResponse = http.get(`${BASE_URL}/api/seats/${seatId}`, {
            headers: headers,
            tags: { name: 'check_seat' }
        });

        seatCheckTrend.add(seatResponse.timings.duration);

        check(seatResponse, {
            'seat check status is 200': (r) => r.status === 200,
            'seat check response time < 100ms': (r) => r.timings.duration < 100,
        });
    }

    // Add a small sleep to prevent overwhelming the system
    sleep(randomIntBetween(1, 3) / 10); // 100-300ms
} 