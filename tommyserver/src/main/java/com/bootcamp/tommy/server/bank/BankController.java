/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bootcamp.tommy.server.bank;

import java.util.List;

import com.bootcamp.tommy.server.protocol.EndpointRequest;
import com.bootcamp.tommy.server.protocol.EndpointResponse;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author Bootcamp-5
 */
@RestController
@RequestMapping("/bank")
public class BankController {

    private final BankRepository bankRepository;

    @Autowired
    public BankController(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }
    
    @GetMapping("/findall")
    public List<Bank> getBanks() {
        return bankRepository.findAll();
    }

    @PostMapping("/register")
    public EndpointResponse registerBank(@RequestBody EndpointRequest<Bank> request) {
        var response = new EndpointResponse<>();
        response.setId("200");

        if (request.getMessage().getName() == null || request.getMessage().getName().isEmpty()) {
            return response.status("414", "Bank name is empty");
        }
        if (request.getMessage().getExternalTransferID() == null || request.getMessage().getExternalTransferID() < 1) {
            return response.status("414", "Bank external id is empty or zero");
        }
        
        Optional<Bank> bankName = bankRepository.findBankname(request.getMessage().getName());
        if (bankName.isPresent()) {
            return response.status("454", "Bank is already register");
        }
        Optional<Bank> bankExternalID = bankRepository.findByExternalId(request.getMessage().getExternalTransferID());
        if (bankExternalID.isPresent()) {
            return response.status("454", "Bank external id is already taken");
        }

        if (response.getId().equals("200")) {
            Bank newBank = new Bank(request.getMessage().getName(), request.getMessage().getExternalTransferID());
            bankRepository.save(newBank);

            response.setMessage("Bank saved");
        }
        return response;
    }
}
