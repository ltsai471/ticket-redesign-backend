## Setup & Testing
http://localhost:8080/test/welcome
http://localhost:8080/setup/setupUsers
http://localhost:8080/setup/addCampaign?name=test1
http://localhost:8080/setup/addSeatsByCampaign?campaignId=1

## System Endpoint
http://localhost:8080
npm run dev

## Docker Test
//docker-compose up --build
docker-compose down

## Redis Setup
```sh
docker run -d --name redis-container -p 6379:6379 redis
docker exec -it redis-container redis-cli

# Confirm Redis is Running
redis-cli -h 127.0.0.1 -p 6379 ping

# redis operate
set mykey "hello"
get mykey
keys *
```

## Kafka Setup
```sh
# setup
docker-compose up --build

```
- Kafka UI
  http://localhost:8082/
- 


## Prometheus 
1. install Prometheus on Windows or via docker
2. edit prometheus.yml under /prometheus
3. prometheus.exe --config.file=prometheus.yml
4. http://localhost:9090

## Grafana
1. install Grafana on Windows or via docker
2. http://localhost:3000
3. default username and pwd are admin/admin
4. create datasource and dashboard