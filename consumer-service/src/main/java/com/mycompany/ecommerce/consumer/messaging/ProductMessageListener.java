package com.mycompany.ecommerce.consumer.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.mycompany.ecommerce.consumer.model.Product;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ProductMessageListener {

    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = "product.created.queue", durable = "true"),
        exchange = @Exchange(value = "product-exchange", type = "topic"),
        key = "product.created"
    ))
    public void handleProductCreated(Product Product) {
        log.info("*********************************");
        log.info("Mensagem recebida:");
        log.info("exchange: {}","product-exchange");
        log.info("routingKey: {}","product.created");
        log.info("Mensagem: {}", Product);        
        log.info("*********************************");
    }

    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = "product.updated.queue", durable = "true"),
        exchange = @Exchange(value = "product-exchange", type = "topic"),
        key = "product.updated"
    ))
    public void handleProductUpdated(Product Product) {
        log.info("*********************************");
        log.info("Mensagem recebida:");
        log.info("exchange: {}","product-exchange");
        log.info("routingKey: {}","product.updated");
        log.info("Mensagem: {}", Product);        
        log.info("*********************************");
    }

    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = "product.deleted.queue", durable = "true"),
        exchange = @Exchange(value = "product-exchange", type = "topic"),
        key = "product.deleted"
    ))
    public void handleProductDeleted(Long ProductId) {
        log.info("*********************************");
        log.info("Mensagem recebida:");
        log.info("exchange: {}","product-exchange");
        log.info("routingKey: {}","product.deleted");
        log.info("Mensagem: {}", ProductId);        
        log.info("*********************************");        
    }
}