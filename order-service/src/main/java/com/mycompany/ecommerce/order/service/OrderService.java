package com.mycompany.ecommerce.order.service;

import com.mycompany.ecommerce.order.model.Order;
import com.mycompany.ecommerce.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;
    

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public Order createOrder(Order order) {
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("CREATED");
        Order savedOrder = orderRepository.save(order);
        rabbitTemplate.convertAndSend("order-exchange", "order.created", savedOrder);
        log.info("*********************************");
        log.info("Mensagem enviada:");
        log.info("exchange: {}","order-exchange");
        log.info("routingKey: {}","order.created");
        log.info("Mensagem: {}", savedOrder);        
        log.info("*********************************");
        return savedOrder;
    }

    public Order updateOrderStatus(Long id, String status) {
        Order order = getOrderById(id);
        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);
        rabbitTemplate.convertAndSend("order-exchange", "order.updated", updatedOrder);
        log.info("*********************************");
        log.info("Mensagem enviada:");
        log.info("exchange: {}","order-exchange");
        log.info("routingKey: {}","order.updated");
        log.info("Mensagem: {}", updatedOrder);        
        log.info("*********************************");        
        return updatedOrder;
    }
}