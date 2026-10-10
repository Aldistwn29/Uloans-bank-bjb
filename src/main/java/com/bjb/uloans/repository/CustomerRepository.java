package com.bjb.uloans.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bjb.uloans.model.Customer;

// cari customer berdasarkan nik
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByNik(String nik);
}