# ds-project
Distributed Sytems Course Project - SWEN7303

DS Project Setup Instructions [RabbitMQ Project]

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
SELECT
    COUNT(*) AS total_messages,
    AVG(latency_in_millis) AS average_latency_ms,
    MIN(latency_in_millis) AS min_latency_ms,
    MAX(latency_in_millis) AS max_latency_ms
FROM messages;

-------------------------------------------------------------------------------------------------


