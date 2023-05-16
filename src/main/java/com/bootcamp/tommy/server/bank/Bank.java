/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bootcamp.tommy.server.bank;

import jakarta.persistence.*;
/**
 *
 * @author Bootcamp-5
 */
@Entity
@Table
public class Bank {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private Long externalTransferID;

    public Bank() {
    }
    public Bank(String name, Long externalTransferID) {
        this.name = name;
        this.externalTransferID = externalTransferID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getExternalTransferID() {
        return externalTransferID;
    }

    public void setExternalTransferID(Long externalTransferID) {
        this.externalTransferID = externalTransferID;
    }
}
