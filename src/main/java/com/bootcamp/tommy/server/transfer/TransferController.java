/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bootcamp.tommy.server.transfer;

import com.bootcamp.tommy.server.account.accmovement.AccMovement;
import com.bootcamp.tommy.server.account.accmovement.AccMovementRepository;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import com.bootcamp.tommy.server.account.Account;
import com.bootcamp.tommy.server.account.AccountRepository;
import com.bootcamp.tommy.server.action.TransferActions;
import com.bootcamp.tommy.server.bank.Bank;
import com.bootcamp.tommy.server.bank.BankRepository;
import com.bootcamp.tommy.server.protocol.EndpointRequest;
import com.bootcamp.tommy.server.protocol.EndpointResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Bootcamp-5
 */
@RestController
@RequestMapping("/transfer")
public class TransferController {
    
    private final TransferRepository transferRepository;
    private final AccountRepository accountRepository;
    private final AccMovementRepository accMovementRepository;
    private final BankRepository bankRepository;
    private final TransferActions transferActions;
    RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public TransferController(TransferRepository transferRepository, AccountRepository accountRepository, AccMovementRepository accMovementRepository, BankRepository bankRepository, TransferActions transferActions) {
        this.transferRepository = transferRepository;
        this.accountRepository = accountRepository;
        this.accMovementRepository = accMovementRepository;
        this.bankRepository = bankRepository;
        this.transferActions = transferActions;
    }
    
    @GetMapping("/findall")
    public List<Transfer> getTransfers() {
        return transferRepository.findAll();
    }

    @PostMapping("/send")
    @Transactional
    public EndpointResponse sendTransaction(@RequestBody EndpointRequest<Transfer> request) {
        var response = new EndpointResponse<>();

        //Validacion del monto
        if (request.getMessage().getAmount() < 10000) {
            return response.status("403", "Amount cant be less than 10.000gs");
        }

        //Validacion de las cuentas
        if (request.getMessage().getSenderAcc().equals(request.getMessage().getReceiverAcc())) {
            return response.status("405", "Both account are the same");
        }

        //Validacion de los bancos
        Optional<Bank> senderBank = bankRepository.findByExternalId(request.getMessage().getSenderBank()); 
        Optional<Bank> receiverBank = bankRepository.findByExternalId(request.getMessage().getReceiverBank());
        if (senderBank.isEmpty() || receiverBank.isEmpty()) {
            return response.status("404", "Bank not found");
        }

        if (request.getMessage().getReceiverBank() == 12) { //Mismo banco
            
            response = restTemplate.postForObject("http://localhost:8080/transfer/receive", request, EndpointResponse.class);

        } else if (request.getMessage().getReceiverBank() == 34) { //Transferecia banco Adan
            transferActions.validateOriginCustomer(request, response);
            if (response.getId().equals("200")) {
                response = restTemplate.postForObject("http://192.168.126.52:8080/transfer/receive", request, EndpointResponse.class);
            }

        } else if (request.getMessage().getReceiverBank() == 56) { //Transferecia banco Hugo
            transferActions.validateOriginCustomer(request, response);
            if (response.getId().equals("200")) {
                response = restTemplate.postForObject("http://192.168.126.60:8080/transfer/receive", request, EndpointResponse.class); //cambiar a la url de hugo
            }
        } else {
            return response.status("407", "Bank doesnt accept transactions from this bank");

        }

        if (response.getId().equals("200")) { //Si la respuesta es positiva guarda la transaccion
            Transfer confirmedTransaction = new Transfer(request.getMessage().getSenderFullname(),
                    request.getMessage().getSenderDocument(),
                    request.getMessage().getSenderAcc(),
                    request.getMessage().getSenderBank(),
                    request.getMessage().getReceiverFullname(),
                    request.getMessage().getReceiverDocument(),
                    request.getMessage().getReceiverAcc(),
                    request.getMessage().getReceiverBank(),
                    request.getMessage().getAmount());
            transferRepository.save(confirmedTransaction);

            if (request.getMessage().getReceiverBank() == 34 || request.getMessage().getReceiverBank() == 56) { //Si mando a otros bancos de forma exitosa, se guarda el debito
                Optional<Account> accountOrigin;
                accountOrigin = accountRepository.findAccount(request.getMessage().getSenderAcc());
                accountOrigin.orElseThrow().setBalance(accountOrigin.get().getBalance() - request.getMessage().getAmount());
     
                AccMovement movementDebit = new AccMovement(request.getMessage().getSenderAcc(), request.getMessage().getReceiverAcc(), "Debit", Calendar.getInstance().getTime(), request.getMessage().getAmount());
                accMovementRepository.save(movementDebit);
            }
            response.setMessage("Successful transaction");
        }
        return response;
    }

    @PostMapping("/receive")
    @Transactional
    public EndpointResponse receiveTransaction(@RequestBody EndpointRequest<Transfer> request) {
        var response = new EndpointResponse<>();
        Optional<Account> accountOrigin;
        Optional<Account> accountDestiny;

        if (request.getMessage().getSenderBank() == 12 && request.getMessage().getReceiverBank() == 12) {
            //Validar cuenta de origen
            transferActions.validateOriginCustomer(request, response);
            if (response.getId().equals("200")) {
                transferActions.validateDestinyCustomer(request, response);
                if (response.getId().equals("200")) {
                    accountOrigin = accountRepository.findAccount(request.getMessage().getSenderAcc());
                    accountDestiny = accountRepository.findAccount(request.getMessage().getReceiverAcc());

                    //Actualizacion del balance
                    accountOrigin.orElseThrow().setBalance(accountOrigin.get().getBalance() - request.getMessage().getAmount());
                    accountDestiny.orElseThrow().setBalance(accountDestiny.get().getBalance() + request.getMessage().getAmount());

                    //Guardando para el historial de transacciones
                    AccMovement movementDebit = new AccMovement(request.getMessage().getSenderAcc(), request.getMessage().getReceiverAcc(), "Debit", Calendar.getInstance().getTime(), request.getMessage().getAmount());
                    accMovementRepository.save(movementDebit);
                    AccMovement movementCredit = new AccMovement(request.getMessage().getReceiverAcc(), request.getMessage().getSenderAcc(), "Credit", Calendar.getInstance().getTime(), request.getMessage().getAmount());
                    accMovementRepository.save(movementCredit);
                }
            }
        } else if (request.getMessage().getSenderBank() == 34 || request.getMessage().getSenderBank() == 56) { //Transferencia de otro banco
            //Validar cuenta de destino
            transferActions.validateDestinyCustomer(request, response);
            if (response.getId().equals("200")) {
                accountDestiny = accountRepository.findAccount(request.getMessage().getReceiverAcc());

                //Actualizacion del balance
                accountDestiny.orElseThrow().setBalance(accountDestiny.get().getBalance() + request.getMessage().getAmount());
            }

            //Guardando para el historial de transacciones
            AccMovement movementCredit = new AccMovement(request.getMessage().getReceiverAcc(), request.getMessage().getSenderAcc(), "Credit", Calendar.getInstance().getTime(), request.getMessage().getAmount());
            accMovementRepository.save(movementCredit);
        } else {
            return response.status("495", "unknow error");
        }
        return response;
    }
    
    @PostMapping("/deposit")
    @ResponseBody
    @Transactional
    public EndpointResponse deposit(@RequestParam String accountDeposit, @RequestParam Double amount) {
        var response = new EndpointResponse<>();

        //Si existe la cuenta
        Optional<Account> account = accountRepository.findAccount(accountDeposit);
        if (account.isEmpty()) {
            return response.status("404", "Account doesnt exists");
        } 

        //Validacion de parametros
        if ((amount < 10000)) {
            return response.status("403", "Amount cant be less than 10.000gs");
        }

        //Actualizacion de balance
        account.orElseThrow().setBalance(account.get().getBalance() + amount);

        //Deposito
        AccMovement movement = new AccMovement(account.get().getAccNumber(), "-", "Deposit", Calendar.getInstance().getTime(), amount);
        accMovementRepository.save(movement);

        return response.status("200", "Successful deposit");
    }
}
