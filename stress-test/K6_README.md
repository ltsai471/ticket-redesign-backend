# k6 Load Testing for Ticket System

This directory contains k6 load testing scripts for the ticket system.

## Prerequisites

1. Install k6:
   - Windows (using Chocolatey):
     ```bash
     choco install k6
     ```
   - macOS:
     ```bash
     brew install k6
     ```
   - Linux:
     ```bash
     sudo gpg -k
     sudo gpg --no-default-keyring --keyring /usr/share/keyrings/k6-archive-keyring.gpg --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys C5AD17C747E3415A3642D57D77C6C491D6AC1D69
     echo "deb [signed-by=/usr/share/keyrings/k6-archive-keyring.gpg] https://dl.k6.io/deb stable main" | sudo tee /etc/apt/sources.list.d/k6.list
     sudo apt-get update
     sudo apt-get install k6
     ```

## Running the Tests

1. Basic run:
   ```bash
   k6 run k6_test.js
   ```

2. Run with custom base URL:
   ```bash
   k6 run -e BASE_URL=http://your-api-url:8080 k6_test.js
   ```

3. Run with output to JSON:
   ```bash
   k6 run --out json=results.json k6_test.js
   ```

4. Run with real-time metrics:
   ```bash
   k6 run --out influxdb=http://localhost:8086/k6 k6_test.js
   ```

## Test Configuration

The test is configured with the following stages:
- Warm-up: 5 minutes, up to 500 users
- Ramp-up: 10 minutes, up to 2000 users
- Peak load: 15 minutes, 3000 users
- Sustained load: 30 minutes, 3000 users
- Ramp-down: 5 minutes

## Performance Thresholds

The test will fail if:
- Order success rate falls below 99%
- 95% of orders take longer than 200ms
- 95% of seat checks take longer than 100ms
- 95% of all requests take longer than 500ms

## Monitoring

The test collects the following metrics:
- Order success rate
- Order response time trend
- Seat check response time trend
- HTTP request duration
- Virtual users
- Request rate
- Error rate

## Visualizing Results

You can visualize the results using:
1. k6 Cloud (requires account)
2. Grafana (with InfluxDB)
3. k6 HTML report

To generate an HTML report:
```bash
k6 run --out json=results.json k6_test.js
k6 report results.json
``` 