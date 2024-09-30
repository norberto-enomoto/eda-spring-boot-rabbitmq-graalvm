package com.mycompany.ecommerce.customer.service;

import com.mycompany.ecommerce.customer.model.Customer;
import com.mycompany.ecommerce.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final RabbitTemplate rabbitTemplate;

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    public Customer createCustomer(Customer customer) {
        Customer savedCustomer = customerRepository.save(customer);
        rabbitTemplate.convertAndSend("customer-exchange", "customer.created", savedCustomer);
        log.info("*********************************");
        log.info("Mensagem enviada:");
        log.info("exchange: {}","customer-exchange");
        log.info("routingKey: {}","customer.created");
        log.info("Mensagem: {}", savedCustomer);        
        log.info("*********************************");
        return savedCustomer;
    }

    public Customer updateCustomer(Long id, Customer customer) {
        Customer existingCustomer = getCustomerById(id);
        existingCustomer.setName(customer.getName());
        existingCustomer.setEmail(customer.getEmail());
        Customer updatedCustomer = customerRepository.save(existingCustomer);
        rabbitTemplate.convertAndSend("customer-exchange", "customer.updated", updatedCustomer);
        log.info("*********************************");
        log.info("Mensagem enviada:");
        log.info("exchange: {}","customer-exchange");
        log.info("routingKey: {}","customer.updated");
        log.info("Mensagem: {}", updatedCustomer);        
        log.info("*********************************");
                
        return updatedCustomer;
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);  
        rabbitTemplate.convertAndSend("customer-exchange", "customer.deleted", id);  
        log.info("*********************************");
        log.info("Mensagem enviada:");
        log.info("exchange: {}","exchange");
        log.info("routingKey: {}","customer.deleted");
        log.info("Mensagem: {}", id);        
        log.info("*********************************");

    }
}