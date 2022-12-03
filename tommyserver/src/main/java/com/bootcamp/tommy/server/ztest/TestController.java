package com.bootcamp.tommy.server.ztest;

import com.bootcamp.tommy.server.customer.Customer;
import com.bootcamp.tommy.server.customer.CustomerRepository;
import com.bootcamp.tommy.server.protocol.EndpointRequest;
import com.bootcamp.tommy.server.protocol.EndpointResponse;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;


@RestController
@RequestMapping("/test")
public class TestController {
    private final CustomerRepository customerRepository;
    private final TestRepository testRepository;
    @Autowired
    public TestController(CustomerRepository customerRepository, TestRepository testRepository) {
        this.customerRepository = customerRepository;
        this.testRepository = testRepository;
    }
    
    @GetMapping
    public String helloWorld() {
        return "Hello World";
    }
    
    @GetMapping("/findall")
    public List<Test> findall() {
        return testRepository.findAll();
    }
    
    @PostMapping("/createAccount")
    public EndpointResponse createAccount(@RequestBody EndpointRequest<Customer> request) {
        var response = new EndpointResponse<>();
        Random rd = new Random();
        Optional<Test> acc;
                
        
        if(!(customerRepository.existsById(request.getMessage().getId()))) {
            return response.status("404", "Client doesnt exists");
        } else {
            String accountNumber = "12";
            String assignedAccNumber;
            do {
                Long aux = rd.nextLong(1000000-99999);
                assignedAccNumber = accountNumber.concat(aux.toString());
                acc = testRepository.findAccount(assignedAccNumber);
                
            } while(acc.isPresent());
            
            Test newAccount = new Test(assignedAccNumber,
                    0.0,
                    request.getMessage().getId(),
                    1L);
            testRepository.save(newAccount);

            response.setId("200");
            response.setMessage("Account created");
        }        
        return response;
    }
}

