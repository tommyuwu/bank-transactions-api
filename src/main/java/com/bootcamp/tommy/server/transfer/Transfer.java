/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bootcamp.tommy.server.transfer;

import jakarta.persistence.*;

/**
 *
 * @author Bootcamp-5
 */
@Entity
@Table
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String senderFullname;
    private String senderDocument;
    private String senderAcc;
    private Long senderBank;
    private String receiverFullname;
    private String receiverDocument;
    private String receiverAcc;
    private Long receiverBank;
    private Double amount;

    public Transfer() {
    }
    public Transfer(String senderFullname, String senderDocument, String senderAcc, Long senderBank, String receiverFullname, String receiverDocument, String receiverAcc, Long receiverBank, Double amount) {
        this.senderFullname = senderFullname;
        this.senderDocument = senderDocument;
        this.senderAcc = senderAcc;
        this.senderBank = senderBank;
        this.receiverFullname = receiverFullname;
        this.receiverDocument = receiverDocument;
        this.receiverAcc = receiverAcc;
        this.receiverBank = receiverBank;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSenderFullname() {
        return senderFullname;
    }

    public void setSenderFullname(String senderFullname) {
        this.senderFullname = senderFullname;
    }

    public String getSenderDocument() {
        return senderDocument;
    }

    public void setSenderDocument(String senderDocument) {
        this.senderDocument = senderDocument;
    }

    public String getSenderAcc() {
        return senderAcc;
    }

    public void setSenderAcc(String senderAcc) {
        this.senderAcc = senderAcc;
    }

    public Long getSenderBank() {
        return senderBank;
    }

    public void setSenderBank(Long senderBank) {
        this.senderBank = senderBank;
    }

    public String getReceiverFullname() {
        return receiverFullname;
    }

    public void setReceiverFullname(String receiverFullname) {
        this.receiverFullname = receiverFullname;
    }

    public String getReceiverDocument() {
        return receiverDocument;
    }

    public void setReceiverDocument(String receiverDocument) {
        this.receiverDocument = receiverDocument;
    }

    public String getReceiverAcc() {
        return receiverAcc;
    }

    public void setReceiverAcc(String receiverAcc) {
        this.receiverAcc = receiverAcc;
    }

    public Long getReceiverBank() {
        return receiverBank;
    }

    public void setReceiverBank(Long receiverBank) {
        this.receiverBank = receiverBank;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

}
