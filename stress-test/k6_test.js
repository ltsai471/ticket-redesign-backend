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
        { duration: '1m', target: 10 },  // Warm-up
        // { duration: '5m', target: 500 },  // Warm-up
        // { duration: '10m', target: 2000 }, // Ramp-up
        // { duration: '15m', target: 3000 }, // Peak load
        // { duration: '30m', target: 3000 }, // Sustained load
        // { duration: '5m', target: 0 },    // Ramp-down
    ],
    thresholds: {
        'order_success_rate': ['rate>0.99'],  // 99% success rate
        'order_trend': ['p(95)<200'],        // 95% of orders under 200ms
        'seat_check_trend': ['p(95)<100'],   // 95% of seat checks under 100ms
        'http_req_duration': ['p(95)<500'],  // 95% of all requests under 500ms
    },
};

// Helper function to get random area
function getRandomArea() {
    const areas = ['A', 'B', 'C'];
    return areas[randomIntBetween(0, 2)];
}

// Helper function to find available seat
function findAvailableSeat(seats) {
    const availableSeats = seats.filter(seat => seat.status === 'available');
    if (availableSeats.length === 0) return null;
    return availableSeats[randomIntBetween(0, availableSeats.length - 1)];
}

// Main test function
export default function () {
    const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';
    const headers = {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
    };

    // Check available seats
    const area = getRandomArea();
    const seatResponse = http.get(`${BASE_URL}/api/seats/1/${area}`, {
        headers: headers,
        tags: { name: 'check_seats' }
    });

    seatCheckTrend.add(seatResponse.timings.duration);

    check(seatResponse, {
        'seat check status is 200': (r) => r.status === 200,
        'seat check response time < 100ms': (r) => r.timings.duration < 100,
    });

    // Parse seats response
    const seats = JSON.parse(seatResponse.body);
    const availableSeat = findAvailableSeat(seats);

    // If we found an available seat, create an order
    if (availableSeat) {
        const orderPayload = JSON.stringify({
            userId: randomIntBetween(1, 500),
            campaignId: availableSeat.campaignId,
            area: availableSeat.area,
            row: availableSeat.seatRow,
            column: availableSeat.seatColumn
        });

        const orderResponse = http.post(`${BASE_URL}/oper/buyTicket`, orderPayload, {
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

    // Add a small sleep to prevent overwhelming the system
    sleep(randomIntBetween(1, 3) / 10); // 100-300ms
} 