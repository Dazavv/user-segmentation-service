package com.example.usersegmentationservice.repository;

import com.example.usersegmentationservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
