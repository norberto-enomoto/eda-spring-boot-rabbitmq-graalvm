package com.mycompany.ecommerce.customer.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.mycompany.ecommerce.customer.model.Customer;

@Component
public class CustomerMessageListener {

    private static final Logger logger = LoggerFactory.getLogger(CustomerMessageListener.class);

    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = "customer.created.queue", durable = "true"),
        exchange = @Exchange(value = "customer-exchange", type = "topic"),
        key = "customer.created"
    ))
    public void handleCustomerCreated(Customer customer) {
        logger.info("Received customer created event: {}", customer);
        try {
            logger.info("customer.created -> " + customer);
        } catch (Exception e) {
            logger.error("Error processing new customer", e);
            // Implementar lógica de tratamento de erro, como retry ou dead-letter queue
        }
    }

    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = "customer.updated.queue", durable = "true"),
        exchange = @Exchange(value = "customer-exchange", type = "topic"),
        key = "customer.updated"
    ))
    public void handleCustomerUpdated(Customer customer) {
        logger.info("Received customer updated event: {}", customer);
        try {
            logger.info("customer.updated -> " + customer);
        } catch (Exception e) {
            logger.error("Error processing updated customer", e);
            // Implementar lógica de tratamento de erro, como retry ou dead-letter queue
        }
    }

    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = "customer.deleted.queue", durable = "true"),
        exchange = @Exchange(value = "customer-exchange", type = "topic"),
        key = "customer.deleted"
    ))
    public void handleCustomerDeleted(Long customerId) {
        logger.info("Received customer deleted event for ID: {}", customerId);
        try {
            logger.info("customer.delete -> " + customerId);
        } catch (Exception e) {
            logger.error("Error processing deleted customer", e);
            // Implementar lógica de tratamento de erro, como retry ou dead-letter queue
        }
    }
}