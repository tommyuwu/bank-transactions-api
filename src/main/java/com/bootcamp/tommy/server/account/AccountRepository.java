/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.bootcamp.tommy.server.account;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Bootcamp-5
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long>{
    @Query("SELECT a "
            + "FROM Account a "
            + "WHERE a.accNumber=?1")
    Optional<Account> findAccount(String accNumber);
}
