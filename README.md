# DS-Project
Distributed Sytems Course Project - SWEN7303

DS Project Setup Instructions [RabbitMQ Project]

# 🟦 Distributed Messaging System: Producer & Consumer with [RabbitMQ, Kafka & PostgreSQL] (Dockerized)

This project demonstrates a **fully containerized distributed system** where:

- A **Spring Boot Producer** sends messages asynchronously to a **RabbitMQ queue**
- A **Spring Boot Consumer** listens on the queue and saves received messages into a **PostgreSQL** database
- All components are containerized via Docker and communicate over a custom network

---

## 🏗️ Architecture

```text
+----------+        +-----------+        +------------+         +-------------+
| Producer | -----> | RabbitMQ  | -----> | Consumer   | ----->  | PostgreSQL  |
+----------+        +-----------+        +------------+         +-------------+
    8080              5672/15672              8081                  5432

🚀 How to Run the Entire System

# 🟦 Distributed Messaging System: Producer & Consumer with RabbitMQ & PostgreSQL (Dockerized)

✅ Prerequisites
- Docker & Docker Compose installed
- Java 17+ (locally for debugging, optional if you're only using Docker)
- Maven installed (optional if building locally)
- Ports 8080, 8081, 5432, 5672, and 15672 must be free

ds-project/
│
└── message-brokers/
    └── rabbitMQ/
        ├── producer/
        ├── consumer/
        ├── docker-compose.yaml
        └── rabbitmq.conf  <-- sets frame_max to 100MB

🐳 Step 1: Build Producer & Consumer Docker Images

From the root of the rabbitMQ/ folder:

    - docker-compose build

This will:

* Build the producer and consumer using your Dockerfiles
* Set JVM heap size to 2GB (-Xmx2G) to handle large messages
* Cache dependencies for faster builds

🐇 Step 2: Start All Services

Run everything using Docker Compose:
    
    - docker-compose up

This will:

* Start RabbitMQ (with frame_max set to 100MB)
* Start PostgreSQL (consumerdb)
* Start producer and consumer apps (Spring Boot)

✅ You should see logs from RabbitMQ, producer, and consumer in your terminal.

📤 Step 3: Send Large Messages

Use curl or Postman to hit the producer’s REST API.

Example to send 10MB messages:

    - curl "http://localhost:8080/send-messages?count=1&size=10485760"

This sends:

* count=1 message
* size=10485760 = 10MB payload

 📌 Access RabbitMQ Management UI:
        http://localhost:15672
        Default Login: guest / guest

📦 Data Storage

Messages are stored in PostgreSQL in the messages table, with full content and latency.
You can connect to the database using any tool like DBeaver, psql, or PgAdmin.

🛑 Stopping Everything

When done:

    - docker-compose down

To stop and remove containers. Add -v to remove volumes too (e.g., if you want a fresh DB):

    - docker-compose down -v

*******************************************************************************************************

# 🟦 Distributed Messaging System: Producer & Consumer with Kafka & PostgreSQL (Dockerized)

✅ Step 1: Make sure your folder structure is like this:

message-brokers/
├── kafka/
│   ├── docker-compose.yml
│   ├── kafak.producer/
│   └── consumer/

✅ Step 2: Build Docker Images for Producer & Consumer

Open a terminal and navigate to the kafka folder:

    - docker-compose build

This builds:

* kafka-producer from ./kafak.producer
* kafka-consumer from ./consumer

✅ Step 3: Start the System

Run the full system (Kafka + Zookeeper + PostgreSQL + Producer + Consumer):

    - docker-compose up

This will start all services and attach logs to your terminal.

If you want to run it in background mode:

    - docker-compose up -d

✅ Step 4: Send Messages from Producer

Use your browser or Postman to hit the following endpoints:

🔹 Send 10 messages of 1MB:

    - http://localhost:8083/send-messages?count=10&size=1048576

✅ Step 5: Clean Up

To shut everything down:

  - docker-compose down -v

To stop containers but keep them reusable:

    - docker-compose stop


#Run postgress to check data :

* docker exec -it postgres psql -U macbook -d consumerdb

* SELECT * FROM messages;

* TRUNCATE TABLE messages;

* SELECT COUNT(*) FROM messages;

* Reset the auto-increment sequence for IDs:

  - ALTER SEQUENCE messages_id_seq RESTART WITH 1;

* Return the AVG of latency, MIN, and MAX

  - SELECT COUNT(*) AS total_messages,
    AVG(latency_in_millis) AS average_latency_ms,
    MIN(latency_in_millis) AS min_latency_ms,
    MAX(latency_in_millis) AS max_latency_ms
FROM messages;
