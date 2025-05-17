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

1️⃣ Create Docker Network
    docker network create ds-network

2️⃣ Start RabbitMQ
    docker run -d --hostname rabbit-host \
      --network ds-network \
      --name rabbitmq \
      -p 5672:5672 -p 15672:15672 \
      rabbitmq:management

    📌 Access RabbitMQ Management UI:
        http://localhost:15672
        Default Login: guest / guest

3️⃣ Start PostgreSQL
    docker run -d \
      --network ds-network \
      --name postgres \
      -e POSTGRES_USER=macbook \
      -e POSTGRES_PASSWORD=guest \
      -e POSTGRES_DB=consumerdb \
      -p 5432:5432 \
      postgres

4️⃣ Build and Run Producer App
    docker build -t producer-app .

    docker run -d \
      --network ds-network \
      --name producer \
      -p 8080:8080 \
      producer-app

5️⃣ Build and Run Consumer App
    docker build -t consumer-app .

    docker run -d \
      --network ds-network \
      --name consumer \
      -p 8081:8081 \
      consumer-app

6️⃣ Trigger Message Sending
    curl "http://localhost:8080/send-messages?count=10"
    ✅ You should see:
        Messages published in Producer logs
        Messages consumed & inserted into DB in Consumer logs --> to see logs user : docker logs producer

🗃️ Check Stored Messages
    Log into PostgreSQL:
        docker exec -it postgres psql -U macbook -d consumerdb
    Then run:
        SELECT * FROM messages;

❌ How to Stop and Clean Everything
    🔻 Stop all containers:
        docker stop producer consumer rabbitmq postgres

    🧹 Remove all containers:
        docker rm producer consumer rabbitmq postgres

    ❌ Delete Docker network:
        docker network rm ds-network
*******************************************************************************************************

DS Project Setup Instructions [Kafka Project]

1️⃣ Run containerized kafka producer : 

# Stop and clean everything
docker compose down -v

# Rebuild the producer (since app.properties changed)
docker compose build kafka-producer

# Start everything again
docker compose up -d



































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

