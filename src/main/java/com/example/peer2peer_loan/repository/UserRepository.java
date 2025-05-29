package com.example.peer2peer_loan.repository;

import com.example.peer2peer_loan.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); // ✅ Add this method
}