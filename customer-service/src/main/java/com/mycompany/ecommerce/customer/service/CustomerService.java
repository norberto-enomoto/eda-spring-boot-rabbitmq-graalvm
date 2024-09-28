package com.mycompany.ecommerce.customer.service;

import com.mycompany.ecommerce.customer.model.Customer;
import com.mycompany.ecommerce.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
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
        return savedCustomer;
    }

    public Customer updateCustomer(Long id, Customer customer) {
        Customer existingCustomer = getCustomerById(id);
        existingCustomer.setName(customer.getName());
        existingCustomer.setEmail(customer.getEmail());
        Customer updatedCustomer = customerRepository.save(existingCustomer);
        rabbitTemplate.convertAndSend("customer-exchange", "customer.updated", updatedCustomer);
        return updatedCustomer;
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);  
        rabbitTemplate.convertAndSend("customer-exchange", "customer.deleted", id);  
    }
}