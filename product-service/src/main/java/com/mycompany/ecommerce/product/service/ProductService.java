package com.mycompany.ecommerce.product.service;

import com.mycompany.ecommerce.product.model.Product;
import com.mycompany.ecommerce.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final RabbitTemplate rabbitTemplate;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Product createProduct(Product product) {
        Product savedProduct = productRepository.save(product);
        rabbitTemplate.convertAndSend("product-exchange", "product.created", savedProduct);
        log.info("*********************************");
        log.info("Mensagem enviada:");
        log.info("exchange: {}","product-exchange");
        log.info("routingKey: {}","product.created");
        log.info("Mensagem: {}", savedProduct);        
        log.info("*********************************");
        return savedProduct;
    }

    public Product updateProduct(Long id, Product product) {
        Product existingProduct = getProductById(id);
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setStock(product.getStock());
        Product updatedProduct = productRepository.save(existingProduct);
        rabbitTemplate.convertAndSend("product-exchange", "product.updated", updatedProduct);
        log.info("*********************************");
        log.info("Mensagem enviada:");
        log.info("exchange: {}","product-exchange");
        log.info("routingKey: {}","product.updated");
        log.info("Mensagem: {}", updatedProduct);        
        log.info("*********************************");

        return updatedProduct;
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
        rabbitTemplate.convertAndSend("product-exchange", "product.deleted", id);
        log.info("*********************************");
        log.info("Mensagem enviada:");
        log.info("exchange: {}","product-exchange");
        log.info("routingKey: {}","product.updated");
        log.info("Mensagem: {}", id);        
        log.info("*********************************");        
    }

    public Product updateStock(Long id, Integer quantity) {
        Product product = getProductById(id);
        product.setStock(product.getStock() + quantity);
        Product updatedProduct = productRepository.save(product);
        rabbitTemplate.convertAndSend("product-exchange", "product.updated", updatedProduct);
        log.info("*********************************");
        log.info("Mensagem enviada:");
        log.info("exchange: {}","product-exchange");
        log.info("routingKey: {}","product.updated");
        log.info("Mensagem: {}", updatedProduct);        
        log.info("*********************************");
        return updatedProduct;
    }
}