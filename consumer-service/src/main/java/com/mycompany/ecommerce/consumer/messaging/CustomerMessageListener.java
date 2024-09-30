package com.mycompany.ecommerce.consumer.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.mycompany.ecommerce.consumer.model.Customer;

import lombok.extern.slf4j.Slf4j;



@Component
@Slf4j
public class CustomerMessageListener {

    private static final Logger logger = LoggerFactory.getLogger(CustomerMessageListener.class);

    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = "customer.created.queue", durable = "true"),
        exchange = @Exchange(value = "customer-exchange", type = "topic"),
        key = "customer.created"
    ))
    public void handleCustomerCreated(Customer customer) {
        log.info("*********************************");
        log.info("Mensagem recebida:");
        log.info("exchange: {}","customer-exchange");
        log.info("routingKey: {}","customer.created");
        log.info("Mensagem: {}", customer);        
        log.info("*********************************");
    }

    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = "customer.updated.queue", durable = "true"),
        exchange = @Exchange(value = "customer-exchange", type = "topic"),
        key = "customer.updated"
    ))
    public void handleCustomerUpdated(Customer customer) {
        log.info("*********************************");
        log.info("Mensagem recebida:");
        log.info("exchange: {}","customer-exchange");
        log.info("routingKey: {}","customer.updated");
        log.info("Mensagem: {}", customer);        
        log.info("*********************************");
    }

    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = "customer.deleted.queue", durable = "true"),
        exchange = @Exchange(value = "customer-exchange", type = "topic"),
        key = "customer.deleted"
    ))
    public void handleCustomerDeleted(Long customerId) {
        log.info("*********************************");
        log.info("Mensagem recebida:");
        log.info("exchange: {}","customer-exchange");
        log.info("routingKey: {}","customer.deleted");
        log.info("Mensagem: {}", customerId);        
        log.info("*********************************");
    }
}