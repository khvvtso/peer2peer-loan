package com.example.peer2peer_loan.repository;

import com.example.peer2peer_loan.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByStatus(String status);
    List<Loan> findByBorrowerEmail(String email);
}