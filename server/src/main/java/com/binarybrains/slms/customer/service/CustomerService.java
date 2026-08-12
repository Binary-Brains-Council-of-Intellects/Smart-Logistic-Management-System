package com.binarybrains.slms.customer.service;

import com.binarybrains.slms.common.exception.CustomerNotFoundException;
import com.binarybrains.slms.customer.dto.CreateCustomerRequest;
import com.binarybrains.slms.customer.dto.CustomerResponse;
import com.binarybrains.slms.customer.model.Customer;
import com.binarybrains.slms.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerResponse createCustomer(CreateCustomerRequest request) {
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Customer with email '" + request.getEmail() + "' already exists");
        }
        Customer customer = new Customer(request.getName(), request.getEmail(),
                request.getPhone(), request.getAddress(), request.getCustomerType());
        return CustomerResponse.fromCustomer(customerRepository.save(customer));
    }

    public CustomerResponse getCustomerById(String id) {
        return CustomerResponse.fromCustomer(findOrThrow(id));
    }

    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll().stream().map(CustomerResponse::fromCustomer).collect(Collectors.toList());
    }

    public List<CustomerResponse> getActiveCustomers() {
        return customerRepository.findByActiveTrue().stream().map(CustomerResponse::fromCustomer).collect(Collectors.toList());
    }

    public List<CustomerResponse> searchCustomers(String keyword) {
        return customerRepository.searchByName(keyword).stream().map(CustomerResponse::fromCustomer).collect(Collectors.toList());
    }

    public CustomerResponse updateCustomer(String id, CreateCustomerRequest request) {
        Customer customer = findOrThrow(id);
        if (request.getName() != null) customer.setName(request.getName());
        if (request.getEmail() != null) customer.setEmail(request.getEmail());
        if (request.getPhone() != null) customer.setPhone(request.getPhone());
        if (request.getAddress() != null) customer.setAddress(request.getAddress());
        if (request.getCustomerType() != null) customer.setCustomerType(request.getCustomerType());
        customer.setUpdatedAt(LocalDateTime.now());
        return CustomerResponse.fromCustomer(customerRepository.save(customer));
    }

    public void deactivateCustomer(String id) {
        Customer c = findOrThrow(id); c.deactivate(); customerRepository.save(c);
    }

    public void activateCustomer(String id) {
        Customer c = findOrThrow(id); c.activate(); customerRepository.save(c);
    }

    public void deleteCustomer(String id) {
        if (!customerRepository.existsById(id)) throw new CustomerNotFoundException(id);
        customerRepository.deleteById(id);
    }

    private Customer findOrThrow(String id) {
        return customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
    }
}
