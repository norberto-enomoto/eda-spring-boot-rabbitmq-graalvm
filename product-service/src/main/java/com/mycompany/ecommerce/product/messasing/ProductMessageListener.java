package com.mycompany.ecommerce.product.messasing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.mycompany.ecommerce.product.model.Product;

@Component
public class ProductMessageListener {

    private static final Logger logger = LoggerFactory.getLogger(ProductMessageListener.class);

    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = "product.created.queue", durable = "true"),
        exchange = @Exchange(value = "product-exchange", type = "topic"),
        key = "product.created"
    ))
    public void handleProductCreated(Product Product) {
        logger.info("Received Product created event: {}", Product);
        try {
            logger.info("Product.created -> " + Product);
        } catch (Exception e) {
            logger.error("Error processing new Product", e);
            // Implementar lógica de tratamento de erro, como retry ou dead-letter queue
        }
    }

    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = "product.updated.queue", durable = "true"),
        exchange = @Exchange(value = "product-exchange", type = "topic"),
        key = "product.updated"
    ))
    public void handleProductUpdated(Product Product) {
        logger.info("Received Product updated event: {}", Product);
        try {
            logger.info("Product.updated -> " + Product);
        } catch (Exception e) {
            logger.error("Error processing updated Product", e);
            // Implementar lógica de tratamento de erro, como retry ou dead-letter queue
        }
    }

    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = "product.deleted.queue", durable = "true"),
        exchange = @Exchange(value = "product-exchange", type = "topic"),
        key = "product.deleted"
    ))
    public void handleProductDeleted(Long ProductId) {
        logger.info("Received Product deleted event for ID: {}", ProductId);
        try {
            logger.info("Product.delete -> " + ProductId);
        } catch (Exception e) {
            logger.error("Error processing deleted Product", e);
            // Implementar lógica de tratamento de erro, como retry ou dead-letter queue
        }
    }
}