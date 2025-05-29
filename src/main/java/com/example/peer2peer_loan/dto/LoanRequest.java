package com.example.peer2peer_loan.dto;

import java.time.LocalDate;

public class LoanRequest {
    private Double amount;
    private String purpose;
    private LocalDate dueDate;

    // Getters and setters
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
}