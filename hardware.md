# Hardware specifications needed

1. **Application Server Requirements**

   a. **CPU Requirements**
    - Base calculation: 
    - =`(Expected Concurrent Users × Operations per User) / (Operations per Second per CPU Core)`
    - =`(预期并发使用者人数 × 每个使用者操作数) / (单核CPU每秒可处理操作数)`
    - Example:
      ```
      - 1000 concurrent users
      - 2 operations per user per minute
      - 100 operations per second per CPU core
      - Required cores = (1000 × 2) / (100 × 60) ≈ 0.33 cores
      - Recommended: 4-8 cores for safety margin
      ```

   b. **Memory Requirements**
    - Base calculation: 
    - =`(Concurrent Users × Memory per User) + (System Overhead)`
    - =`(并发使用者人数 × 每个使用者需要的内存大小) + (buffer)`
    - Example:
      ```
      - 1000 concurrent users
      - 50MB per user session
      - 2GB system overhead
      - Required RAM = (1000 × 50MB) + 2GB = 52GB
      - Recommended: 64GB for safety margin
      ```

2. **Database Server Requirements**

   a. **CPU Requirements**
    - Base calculation: 
    - =`(Database Operations per Second) / (Operations per Second per CPU Core)`
    - =`DB每秒操作数 / 单核CPU每秒可处理操作数`
    - Example:
      ```
      - 500 database operations per second
      - 200 operations per second per CPU core
      - Required cores = 500/200 = 2.5 cores
      - Recommended: 8-16 cores for safety margin
      ```

   b. **Memory Requirements**
    - Base calculation: 
    - =`(Active Data Size × 0.2) + (Connection Pool × Memory per Connection)`
    - =`[cache](活跃data大小 × 0.2) + [connection](Connection Pool × 每个Connection需要的内存大小)`
    - Example:
      ```
      - 100GB active data
      - 20% for caching = 20GB
      - 1000 connections × 10MB = 10GB
      - Required RAM = 20GB + 10GB = 30GB
      - Recommended: 64GB for safety margin
      ```

3. **Kafka Cluster Requirements**

   a. **Broker Requirements**
    - Base calculation: 
    - =`(Message Throughput × Message Size) / (Broker Throughput)`
    - =`(讯息吞吐量 × 讯息大小) / Broker吞吐量`
    - Example:
      ```
      - 10,000 messages per second
      - 1KB per message
      - 50MB/s per broker
      - Required brokers = (10,000 × 1KB) / 50MB = 0.2 brokers
      - Recommended: 3 brokers for redundancy
      ```

   b. **Per Broker Specifications**
   ```
   CPU: 8-16 cores
   RAM: 32-64GB
   Storage: 1-2TB SSD
   Network: 10Gbps
   ```

4. **Redis Cache Requirements**

   a. **Memory Requirements**
    - Base calculation: 
    - =`(Number of Seats × Memory per Seat) + (Overhead)`
    - =`(座位数 × 每个座位需要的内存数) + (buffer)`
    - Example:
      ```
      - 10,000 seats
      - 1KB per seat
      - 20% overhead
      - Required RAM = (10,000 × 1KB) × 1.2 = 12MB
      - Recommended: 16GB for safety margin
      ```

5. **Network Requirements**

   a. **Bandwidth Calculation**
   ```
   - 1000 concurrent users
   - 2KB per request
   - 2 requests per second per user
   - Required bandwidth = 1000 × 2KB × 2 = 4MB/s
   - = 并发使用者数 * request大小
   - Recommended: 1Gbps for safety margin
   ```

6. **Storage Requirements**

   a. **Database Storage**
   ```
   - sigma(一个表一天的资料笔数 * 一笔资料的大小 * 天数)
   - 100,000 tickets per day
   - 1KB per ticket record
   - 30 days retention
   - Required storage = 100,000 × 1KB × 30 = 3GB
   - Recommended: 100GB for safety margin
   ```

7. **Recommended Hardware Specifications**

   a. **Application Servers (2-3 nodes)**
   ```
   CPU: 8 cores
   RAM: 64GB
   Storage: 500GB SSD
   Network: 1Gbps
   ```

   b. **Database Servers (Primary + Replica)**
   ```
   CPU: 16 cores
   RAM: 64GB
   Storage: 1TB SSD
   Network: 1Gbps
   ```

   c. **Kafka Brokers (3 nodes)**
   ```
   CPU: 8 cores
   RAM: 32GB
   Storage: 1TB SSD
   Network: 1Gbps
   ```

   d. **Redis Servers (2 nodes)**
   ```
   CPU: 4 cores
   RAM: 16GB
   Storage: 100GB SSD
   Network: 1Gbps
   ```

8. **Monitoring Requirements**
   ```
   CPU: 4 cores
   RAM: 16GB
   Storage: 500GB SSD
   Network: 1Gbps
   ```

9. **Scaling Considerations**
    - Vertical scaling: Increase CPU/RAM of existing servers
    - Horizontal scaling: Add more application servers
    - Auto-scaling: Based on CPU/Memory utilization
    - Load balancing: Across multiple application servers

10. **Cost Optimization**
    - Use spot instances for non-critical components
    - Implement auto-scaling
    - Use reserved instances for stable workloads
    - Monitor and optimize resource usage
