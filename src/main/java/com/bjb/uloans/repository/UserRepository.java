package com.bjb.uloans.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bjb.uloans.model.User;

// find user by username for login
public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByUsername(String username);
}