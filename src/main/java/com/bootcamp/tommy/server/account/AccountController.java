/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bootcamp.tommy.server.account;

import java.util.Collection;
import java.util.List;

import com.bootcamp.tommy.server.account.accmovement.AccMovement;
import com.bootcamp.tommy.server.account.accmovement.AccMovementRepository;
import com.bootcamp.tommy.server.customer.Customer;
import com.bootcamp.tommy.server.customer.CustomerRepository;
import com.bootcamp.tommy.server.protocol.EndpointRequest;
import com.bootcamp.tommy.server.protocol.EndpointResponse;
import java.util.Optional;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author Bootcamp-5
 */
@RestController
@RequestMapping("/account")
public class AccountController {
    
    private final AccountRepository accountRepository;
    private final AccMovementRepository accMovementRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public AccountController(AccountRepository accountRepository, AccMovementRepository accMovementRepository, CustomerRepository customerRepository) {
        this.accountRepository = accountRepository;
        this.accMovementRepository = accMovementRepository; 
        this.customerRepository = customerRepository;
    }
    
    @GetMapping("/movements")
    @ResponseBody
    public Collection<AccMovement> getAccountMovements(@RequestParam String accountID) {
        return accMovementRepository.findAllMovements(accountID);
    }

    @PostMapping("/create")
    public EndpointResponse createAccount(@RequestBody EndpointRequest<Customer> request) {
        var response = new EndpointResponse<>();
        Random rd = new Random();
        Optional<Account> acc;
        
        if(!(customerRepository.existsById(request.getMessage().getId()))) {
            return response.status("404", "Client doesnt exists");
        } else {
            String accountNumber = "12";
            String assignedAccNumber;
            do {
                Long aux = rd.nextLong(1000000-99999);
                assignedAccNumber = accountNumber.concat(aux.toString());
                acc = accountRepository.findAccount(assignedAccNumber);
                
            } while(acc.isPresent());
            
            Account newAccount = new Account(assignedAccNumber,
                    0.0,
                    request.getMessage().getId(),
                    12L);
            accountRepository.save(newAccount);

            response.setId("200");
            response.setMessage("Account created");
        }        
        return response;
    }
    @GetMapping("/findall")
    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }
}
