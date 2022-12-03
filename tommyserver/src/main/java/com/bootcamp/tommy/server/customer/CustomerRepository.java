/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.bootcamp.tommy.server.customer;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Bootcamp-5
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{

    @Query("SELECT c "
            + "FROM Customer c "
            + "WHERE c.document=?1")
    Optional<Customer> findCustomerDocument(String document);
    @Query("SELECT c "
            + "FROM Customer c "
            + "WHERE c.phone=?1")
    Optional<Customer> findCustomerPhone(String phone);
    @Query("SELECT c "
            + "FROM Customer c "
            + "WHERE c.email=?1")
    Optional<Customer> findCustomerEmail(String email);
}
