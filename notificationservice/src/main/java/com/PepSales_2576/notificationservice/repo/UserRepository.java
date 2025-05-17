package com.PepSales_2576.notificationservice.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.PepSales_2576.notificationservice.modal.User;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUserId(String userId);
}
