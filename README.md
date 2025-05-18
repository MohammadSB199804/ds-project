# DS-Project
Distributed Sytems Course Project - SWEN7303

DS Project Setup Instructions [RabbitMQ Project]

# 🟦 Distributed Messaging System: Producer & Consumer with RabbitMQ & PostgreSQL (Dockerized)

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

DS Project Setup Instructions [Kafka Project]

1️⃣ Run containerized kafka producer and consumer : 

docker-compose down -v
docker-compose build
docker-compose up --remove-orphans


#Run postgress to check data
docker exec -it postgres psql -U macbook -d consumerdb
SELECT * FROM messages;
TRUNCATE TABLE messages;
SELECT COUNT(*) FROM messages;




































*******************************************************************************************************
📋 Prerequisites

- Install Maven (to build and run Spring Boot projects).
- Install RabbitMQ.
- Install PostgreSQL.

🛠️ Setup Steps

1. Install and Start RabbitMQ
Install RabbitMQ on your local machine.

* Start RabbitMQ server:
  -rabbitmq-server
OR: 
  -brew services start rabbitmq
OR:
- docker run -d --hostname rabbitmq-host --name rabbitmq \
-p 5672:5672 -p 15672:15672 \
rabbitmq:management

* Verify that RabbitMQ service is running:
  -brew services list
  
* Check if RabbitMQ is listening on the default port (5672):
  -sudo lsof -i :5672
  
* Open RabbitMQ Management UI in your browser:
  -http://localhost:15672/
    Username: guest
    Password: guest
  
2. Install and Start PostgreSQL
Install PostgreSQL database.

* Start PostgreSQL service:
  -brew services start postgresql@15
    Note:
    If needed, you can stop PostgreSQL with:
  -brew services stop postgresql@15

🚀 Running the Projects

3. Run the Producer Project
Navigate to the producer project directory.

* Run the producer using Maven:
  - ./mvnw spring-boot:run

4. Send Messages
Once the producer is running, open your browser and trigger sending messages:
http://localhost:8080/send-messages?count=300000
Replace 300000 with the number of transactions you want to send.


🧠 Important Notes for Consumer Project
Ensure that database username and password in the consumer project match your PostgreSQL setup.
You can either:
Use the default postgres superuser.
Or create a new PostgreSQL user with appropriate roles.


🗄️ PostgreSQL Useful Commands
* Connect to PostgreSQL as the default user:
  -psql -U postgres
* Connect to a specific database :
  - psql -U postgres -d your_database_name
* Connect using a specific database owner:
  - psql -U owner_username -d your_database_name
* Clear all rows from a table:
  - TRUNCATE TABLE your_table_name;
* Reset the auto-increment sequence for IDs:
  - ALTER SEQUENCE messages_id_seq RESTART WITH 1;
* Return the AVG of latency, MIN, and MAX
  - SELECT COUNT(*) AS total_messages,
    AVG(latency_in_millis) AS average_latency_ms,
    MIN(latency_in_millis) AS min_latency_ms,
    MAX(latency_in_millis) AS max_latency_ms
FROM messages;

-------------------------------------------------------------------------------------------------

DS Project Setup Instructions [Kafka Project]

📋 Prerequisites

- Install Offset Explorer (Tool for kAFKA UI).
- Install Kafka.
- Install PostgreSQL.

🛠️ Setup Steps

1. Start the services using Docker:
  - docker-compose up -d
2. Run Kafka UI in a separate container:
  - docker run -d \
      -p 8090:8080 \
      -e KAFKA_CLUSTERS_0_NAME=local \
      -e KAFKA_CLUSTERS_0_BOOTSTRAPSERVERS=localhost:9092 \
      provectuslabs/kafka-ui

🛑 To Stop Kafka Services:

1. Stop all running containers:
  - docker stop $(docker ps -aq)
2. Take down the Docker Compose environment:
  - docker-compose down
3. Remove all stopped containers:
  - docker rm $(docker ps -aq)

