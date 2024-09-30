package com.mycompany.ecommerce.consumer.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.mycompany.ecommerce.consumer.model.Order;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OrderMessageListener {
    
    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = "order.created.queue", durable = "true"),
        exchange = @Exchange(value = "order-exchange", type = "topic"),
        key = "order.created"
    ))
    public void handleorderCreated(Order order) {
        log.info("*********************************");
        log.info("Mensagem recebida:");
        log.info("exchange: {}","order-exchange");
        log.info("routingKey: {}","order.created");
        log.info("Mensagem: {}", order);        
        log.info("*********************************");     
    }

    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = "order.updated.queue", durable = "true"),
        exchange = @Exchange(value = "order-exchange", type = "topic"),
        key = "order.updated"
    ))
    public void handleorderUpdated(Order order) {
        log.info("*********************************");
        log.info("Mensagem recebida:");
        log.info("exchange: {}","order-exchange");
        log.info("routingKey: {}","order.updated");
        log.info("Mensagem: {}", order);        
        log.info("*********************************");         
    }

    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = "order.deleted.queue", durable = "true"),
        exchange = @Exchange(value = "order-exchange", type = "topic"),
        key = "order.deleted"
    ))
    public void handleorderDeleted(Long orderId) {
        log.info("*********************************");
        log.info("Mensagem recebida:");
        log.info("exchange: {}","order-exchange");
        log.info("routingKey: {}","order.deleted");
        log.info("Mensagem: {}", orderId);        
        log.info("*********************************");         
    }
}