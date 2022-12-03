/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bootcamp.tommy.server.customer;

import java.util.List;

import com.bootcamp.tommy.server.protocol.EndpointRequest;
import com.bootcamp.tommy.server.protocol.EndpointResponse;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Bootcamp-5
 */
@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    
    @GetMapping("/findall")
    public List<Customer> getCustomer() {
        return customerRepository.findAll();
    }

    @PostMapping("/create")
    public EndpointResponse createCustomer(@RequestBody EndpointRequest<Customer> request) {
        var response = new EndpointResponse<>();
        response.setId("200");

        if (request.getMessage().getFirstname() == null || request.getMessage().getFirstname().isEmpty()) {
            return response.status("414", "Firstname is empty");
        } else if (request.getMessage().getLastname() == null || request.getMessage().getLastname().isEmpty()) {
            return response.status("414", "Lastname is empty");
        } else if (request.getMessage().getDocument() == null || request.getMessage().getDocument().isEmpty()) {
            return response.status("414", "Document is empty");
        } else if (request.getMessage().getPhone() == null || request.getMessage().getPhone().isEmpty()) {
            return response.status("414", "Phone is empty");
        } else if (request.getMessage().getEmail() == null || request.getMessage().getEmail().isEmpty()) {
            return response.status("414", "Email is empty");
        } else if (request.getMessage().getAddress() == null || request.getMessage().getAddress().isEmpty()) {
            return response.status("414", "Address is empty");
        }
        
        Optional<Customer> customerEmail = customerRepository.findCustomerEmail(request.getMessage().getEmail());
        Optional<Customer> customerDoc = customerRepository.findCustomerDocument(request.getMessage().getDocument());
        Optional<Customer> customerPhone = customerRepository.findCustomerPhone(request.getMessage().getPhone());
        if (customerEmail.isPresent()) {
            return response.status("454", "Email taken");
        } else if (customerDoc.isPresent()) {
            return response.status("454", "Document taken");
        } else if (customerPhone.isPresent()) {
            return response.status("454", "Phone taken");
        }

        if (response.getId().equals("200")) {
            String fullname = request.getMessage().getFirstname().concat(" ").concat(request.getMessage().getLastname());
            Customer newCustomer = new Customer(request.getMessage().getFirstname(),
                    request.getMessage().getLastname(),
                    fullname,
                    request.getMessage().getDocument(),
                    request.getMessage().getPhone(),
                    request.getMessage().getEmail(),
                    request.getMessage().getAddress());
            customerRepository.save(newCustomer);

            response.setMessage("Customer saved");
        }
        return response;
    }

    @DeleteMapping("/delete/{customerID}")
    public void deleteCustomer(@PathVariable Long customerID) {
        if(!(customerRepository.existsById(customerID))) {
            throw new IllegalStateException("customer doesnt exist");
        } else {
            customerRepository.deleteById(customerID);
        }
    }
}