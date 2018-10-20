package edu.gatech.split.Model;

public class Transaction {
    String key;
    String purpose;
    double amount;

    // Default no-arg constructor required by Firebase
    public Transaction() {

    }

    public Transaction(String key, String purpose, double amount) {
        this.key = key;
        this.purpose = purpose;
        this.amount = amount;
    }

    public String getPurpose() {
        return purpose;
    }

    public double getAmount() {
        return amount;
    }
}
