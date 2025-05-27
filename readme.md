## Setup & Testing
http://localhost:8080/test/welcome
http://localhost:8080/setup/setupUsers
http://localhost:8080/setup/addCampaign?name=test1
http://localhost:8080/setup/addSeatsByCampaign?campaignId=1

## System Endpoint
http://localhost:8080

## Docker Test
//docker-compose up --build

## Redis Setup
```sh
docker run -d --name redis-container -p 6379:6379 redis
docker exec -it redis-container redis-cli

# Confirm Redis is Running
redis-cli -h 127.0.0.1 -p 6379 ping

```


