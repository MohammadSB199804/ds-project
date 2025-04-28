package com.example.producer.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public DirectExchange exchange() {
        //Creates a Direct Exchange (meaning: routes messages using exact routingKey matches).
        return new DirectExchange("demo.exchange");
    }

    @Bean
    public Queue queue() {
        //Declares a durable queue (survives RabbitMQ restarts).
        return new Queue("demo.queue", true);
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        //When you send to demo.exchange with demo.routing.key, the message lands in demo.queue.
        return BindingBuilder.bind(queue).to(exchange).with("demo.routing.key");
    }

    // ➡️ Add this Bean to support JSON payloads
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        //This tells Spring to convert Java Objects into JSON before sending to RabbitMQ (important for MessagePayload).
        return new Jackson2JsonMessageConverter();
    }

    // ➡️ Tell RabbitTemplate to use the JSON converter
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        //RabbitTemplate is what you use to send messages.
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        //Here, we set the message converter for JSON automatically.
        template.setMessageConverter(messageConverter());
        return template;
    }
}
