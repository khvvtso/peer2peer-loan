package com.example.peer2peer_loan.controller;

import com.example.peer2peer_loan.dto.LoanRequest;
import com.example.peer2peer_loan.model.Loan;
import com.example.peer2peer_loan.model.User;
import com.example.peer2peer_loan.repository.LoanRepository;
import com.example.peer2peer_loan.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private UserRepository userRepository;

    // ✅ BORROWER: Request a loan
    @PostMapping("/request")
    public ResponseEntity<?> requestLoan(@RequestBody LoanRequest loanRequest, Authentication auth) {
        String email = auth.getName();
        User borrower = userRepository.findByEmail(email).orElse(null);

        if (borrower == null || !"BORROWER".equalsIgnoreCase(borrower.getRole())) {
            return ResponseEntity.status(403).body("Only borrowers can request loans.");
        }

        Loan loan = new Loan();
        loan.setAmount(loanRequest.getAmount());
        loan.setPurpose(loanRequest.getPurpose());
        loan.setDueDate(loanRequest.getDueDate());
        loan.setBorrower(borrower);
        loan.setStatus("PENDING"); // make sure this field is always set

        Loan savedLoan = loanRepository.save(loan);
        return ResponseEntity.ok(savedLoan);
    }

    // ✅ LENDER: Fund a loan
    @PostMapping("/{loanId}/fund")
    public ResponseEntity<?> fundLoan(@PathVariable Long loanId, Authentication auth) {
        String lenderEmail = auth.getName();

        User lender = userRepository.findByEmail(lenderEmail).orElse(null);
        if (lender == null || !"LENDER".equalsIgnoreCase(lender.getRole())) {
            return ResponseEntity.status(403).body("Only lenders can fund loans.");
        }

        Loan loan = loanRepository.findById(loanId).orElse(null);
        if (loan == null) {
            return ResponseEntity.status(404).body("Loan not found.");
        }

        if (!"PENDING".equalsIgnoreCase(loan.getStatus())) {
            return ResponseEntity.badRequest().body("Loan is not available for funding.");
        }

        // Mark loan as funded
        loan.setStatus("FUNDED");

        // Safely update borrower's balance
        User borrower = loan.getBorrower();
        double currentBalance = borrower.getBalance() != null ? borrower.getBalance() : 0.0;
        borrower.setBalance(currentBalance + loan.getAmount());

        // Save changes
        userRepository.save(borrower);
        loanRepository.save(loan);

        return ResponseEntity.ok("Loan funded successfully.");
    }

    @PostMapping("/{loanId}/repay")
    public ResponseEntity<?> repayLoan(@PathVariable Long loanId, Authentication auth) {
        String borrowerEmail = auth.getName();

        User borrower = userRepository.findByEmail(borrowerEmail).orElse(null);
        if (borrower == null || !"BORROWER".equalsIgnoreCase(borrower.getRole())) {
            return ResponseEntity.status(403).body("Only borrowers can repay loans.");
        }

        Loan loan = loanRepository.findById(loanId).orElse(null);
        if (loan == null) {
            return ResponseEntity.status(404).body("Loan not found.");
        }

        if (!loan.getBorrower().getId().equals(borrower.getId())) {
            return ResponseEntity.status(403).body("You are not authorized to repay this loan.");
        }

        if (!"FUNDED".equalsIgnoreCase(loan.getStatus())) {
            return ResponseEntity.badRequest().body("Only funded loans can be repaid.");
        }

        if (borrower.getBalance() < loan.getAmount()) {
            return ResponseEntity.badRequest().body("Insufficient balance to repay the loan.");
        }

        // Deduct amount from borrower
        borrower.setBalance(borrower.getBalance() - loan.getAmount());

        // Update loan status
        loan.setStatus("REPAID");

        // Save updates
        userRepository.save(borrower);
        loanRepository.save(loan);

        return ResponseEntity.ok("Loan repaid successfully.");
    }
}