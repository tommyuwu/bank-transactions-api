/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.bootcamp.tommy.server.bank;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Bootcamp-5
 */
@Repository
public interface BankRepository extends JpaRepository<Bank, Long>{
    
    @Query("SELECT b "
            + "FROM Bank b "
            + "WHERE b.name=?1")
    Optional<Bank> findBankname(String bankName);
    @Query("SELECT b "
            + "FROM Bank b "
            + "WHERE b.externalTransferID=?1")
    Optional<Bank> findByExternalId(Long externalId);
}
