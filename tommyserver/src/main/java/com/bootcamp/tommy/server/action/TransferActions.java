package com.bootcamp.tommy.server.action;

import com.bootcamp.tommy.server.account.Account;
import com.bootcamp.tommy.server.account.AccountRepository;
import com.bootcamp.tommy.server.customer.Customer;
import com.bootcamp.tommy.server.customer.CustomerRepository;
import com.bootcamp.tommy.server.protocol.EndpointRequest;
import com.bootcamp.tommy.server.protocol.EndpointResponse;
import com.bootcamp.tommy.server.transfer.Transfer;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TransferActions {
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public TransferActions(AccountRepository accountRepository, CustomerRepository customerRepository) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
    }

    public void validateOriginCustomer(EndpointRequest<Transfer> request, EndpointResponse<Object> response) {
        Optional<Account> accountOrigin = accountRepository.findAccount(request.getMessage().getSenderAcc());
        Optional<Customer> originCustomer;
        response.setId("200");
        
        if (accountOrigin.isEmpty()) {
            response.status("404", "Sender account doesnt exists");
        } else {
            originCustomer = customerRepository.findById(accountOrigin.orElseThrow().getClientID());
            if (accountOrigin.orElseThrow().getBalance() < request.getMessage().getAmount()) {
                response.status("401", "Sender account doesnt have sufficient balance");
            }
            if (!(request.getMessage().getSenderFullname().equals(originCustomer.orElseThrow().getFullname()) && request.getMessage().getSenderDocument().equals(originCustomer.orElseThrow().getDocument()))) {
                response.status("409", "The name or the document provided does not match the account owner");
            }
            if (!(Objects.equals(request.getMessage().getSenderBank(), accountOrigin.orElseThrow().getBankID()))) {
                response.status("409", "The bank id provided does not match the account");
            }
        }
    }

    public void validateDestinyCustomer(EndpointRequest<Transfer> request, EndpointResponse<Object> response) {
        Optional<Account> accountDestiny = accountRepository.findAccount(request.getMessage().getReceiverAcc());
        Optional<Customer> destinyCustomer;
        response.setId("200");

        if (accountDestiny.isEmpty()) {
            response.status("404", "Destiny account doesnt exists");
        } else {
            destinyCustomer = customerRepository.findById(accountDestiny.orElseThrow().getClientID());
            if (!(request.getMessage().getReceiverFullname().equals(destinyCustomer.orElseThrow().getFullname()) && request.getMessage().getReceiverDocument().equals(destinyCustomer.orElseThrow().getDocument()))) {
                response.status("409", "The name or the document provided does not match the account owner");
            }
            if (!(Objects.equals(request.getMessage().getReceiverBank(), accountDestiny.orElseThrow().getBankID()))) {
                response.status("409", "The bank id provided does not match the account");
            }
        }
    }
}
