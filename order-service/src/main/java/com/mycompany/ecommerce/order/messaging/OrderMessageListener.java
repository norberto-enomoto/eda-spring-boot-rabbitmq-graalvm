package com.mycompany.ecommerce.order.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.mycompany.ecommerce.order.model.Order;

@Component
public class OrderMessageListener {

    private static final Logger logger = LoggerFactory.getLogger(OrderMessageListener.class);

    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = "order.created.queue", durable = "true"),
        exchange = @Exchange(value = "order-exchange", type = "topic"),
        key = "order.created"
    ))
    public void handleorderCreated(Order order) {
        logger.info("Received order created event: {}", order);
        try {
            logger.info("order.created -> " + order);
        } catch (Exception e) {
            logger.error("Error processing new order", e);
            // Implementar lógica de tratamento de erro, como retry ou dead-letter queue
        }
    }

    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = "order.updated.queue", durable = "true"),
        exchange = @Exchange(value = "order-exchange", type = "topic"),
        key = "order.updated"
    ))
    public void handleorderUpdated(Order order) {
        logger.info("Received order updated event: {}", order);
        try {
            logger.info("order.updated -> " + order);
        } catch (Exception e) {
            logger.error("Error processing updated order", e);
            // Implementar lógica de tratamento de erro, como retry ou dead-letter queue
        }
    }

    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = "order.deleted.queue", durable = "true"),
        exchange = @Exchange(value = "order-exchange", type = "topic"),
        key = "order.deleted"
    ))
    public void handleorderDeleted(Long orderId) {
        logger.info("Received order deleted event for ID: {}", orderId);
        try {
            logger.info("order.delete -> " + orderId);
        } catch (Exception e) {
            logger.error("Error processing deleted order", e);
            // Implementar lógica de tratamento de erro, como retry ou dead-letter queue
        }
    }
}