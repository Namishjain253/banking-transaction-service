package org.hsbc.banking.service;

import org.hsbc.banking.dto.CustomerRequest;
import org.hsbc.banking.dto.CustomerResponse;
import org.hsbc.banking.entity.Customer;
import org.hsbc.banking.repository.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerResponse createCustomer(CustomerRequest request) {

        if (customerRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        if (customerRepository
                .findByMobileNumber(request.getMobileNumber())
                .isPresent()) {
            throw new IllegalArgumentException("Mobile number already exists");
        }

        Customer customer = new Customer(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getMobileNumber()
        );

        Customer savedCustomer = customerRepository.save(customer);

        return new CustomerResponse(
                savedCustomer.getId(),
                savedCustomer.getFirstName(),
                savedCustomer.getLastName(),
                savedCustomer.getEmail(),
                savedCustomer.getMobileNumber()
        );
    }
}