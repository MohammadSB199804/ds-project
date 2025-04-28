package com.example.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ProducerApplication {
//@EnableAsync: Tells Spring that async methods like @Async publishMessages() are allowed.
	public static void main(String[] args) {
		//It starts the embedded Tomcat server (port 8080), connects to RabbitMQ, prepares APIs, etc.
		SpringApplication.run(ProducerApplication.class, args);
	}
}
/*
* 🧠 Overall Workflow (Big Picture)
* You hit: http://localhost:8080/send-messages?count=100000
* ProducerController handles the HTTP request → passes count to MessagePublisher.
* MessagePublisher starts an async task:
	- Records start time.
	- Loops through count messages:
		+ Creates a new JSON message.
		+ Publishes each message using rabbitTemplate.
	- Records end time.
	- Logs how many messages were sent, how long it took, and throughput.
* Messages arrive in RabbitMQ Exchange → routed to demo.queue.
* (Later, the consumer service will pick them up.)
* */
/*------------------------------------------------------------------------------*/
/*
* 🔥 What does "Async" mean?
* Async (short for Asynchronous) means:
	-"Do not wait for a task to finish — just start it, and move on immediately."

* In normal (synchronous) systems:
	-If you send 1 message, the producer waits until RabbitMQ says "OK, received" before moving to send the next message.

* In asynchronous systems:
	-You send the message to RabbitMQ
	-Immediately you continue sending the next message
	-You don't wait for RabbitMQ acknowledgment after every single message
* 🧠 Why is it important?
	-In real-world high-load systems (like Twitter, Netflix, Banks), producers must be super fast because:
		+ They sometimes need to send millions of messages per second.
		+ Waiting for every single message would make the producer very slow and waste time.
* 🚗 Simple Real-Life Example
	- Imagine you are driving and delivering 1000 packages to houses.
	* Synchronous Delivery	: Knock on door → Wait for person to open → Hand package → Say goodbye → Go to next house.
	* Asynchronous Delivery : Leave package at the door → Take a photo → Immediately drive to the next house without waiting for anyone.
* Before Async
	- You call /send-messages?count=100000
	- The API waits until all 100,000 messages are sent.
	- Only then you see HTTP response.
(Takes long time. Browser will hang.)
* After Async
	- You call /send-messages?count=100000
	- The method starts sending messages in the background.
	- The API immediately returns the response: 🚀 Sending 100000 messages asynchronously! Check the logs.

Async Publishing means your producer sends messages continuously without waiting, which is critical to achieve high throughput, low latency, and simulate real-world messaging systems — perfect for your RabbitMQ vs Kafka performance comparison.

* Important
Without Async | With Async
Slow sending | Fast sending
Main thread blocked | Main thread free
Not realistic for production systems | Realistic high-load simulation
Lower throughput measurement | Higher throughput measurement
Risk of HTTP timeouts on big tests | Instant HTTP success
*/