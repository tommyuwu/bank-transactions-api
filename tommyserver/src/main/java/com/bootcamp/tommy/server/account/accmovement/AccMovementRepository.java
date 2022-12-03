/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.bootcamp.tommy.server.account.accmovement;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Bootcamp-5
 */
@Repository
public interface AccMovementRepository extends JpaRepository<AccMovement,Long>{
    
    @Query("SELECT a "
            + "FROM AccMovement a "
            + "WHERE a.accountID=?1")
    Collection<AccMovement> findAllMovements(String Acc);
}
