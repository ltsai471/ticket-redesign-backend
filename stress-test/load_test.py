from locust import HttpUser, task, between
import random
import json
from datetime import datetime
import logging

class TicketSystemUser(HttpUser):
    wait_time = between(0.1, 0.5)  # Simulate real user behavior with small delays
    
    def on_start(self):
        # Initialize any necessary setup
        self.headers = {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        }
    
    @task(3)  # Higher weight for order events
    def create_order(self):
        # Generate random seat selection
        seat_id = f"{random.randint(1, 100)}/{random.choice(['A', 'B', 'C', 'D', 'E'])}"
        
        payload = {
            "userId": f"user_{random.randint(1, 1000)}",
            "seatId": seat_id,
            "timestamp": datetime.utcnow().isoformat(),
            "eventId": f"event_{random.randint(1, 10)}"
        }
        
        with self.client.post(
            "/api/orders",
            json=payload,
            headers=self.headers,
            catch_response=True
        ) as response:
            if response.status_code == 200:
                response.success()
            else:
                response.failure(f"Failed with status {response.status_code}: {response.text}")

    @task(1)  # Lower weight for checking seat availability
    def check_seat_availability(self):
        seat_id = f"{random.randint(1, 100)}/{random.choice(['A', 'B', 'C', 'D', 'E'])}"
        
        with self.client.get(
            f"/api/seats/{seat_id}",
            headers=self.headers,
            catch_response=True
        ) as response:
            if response.status_code == 200:
                response.success()
            else:
                response.failure(f"Failed with status {response.status_code}: {response.text}") 