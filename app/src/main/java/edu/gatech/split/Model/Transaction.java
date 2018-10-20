package edu.gatech.split.Model;

public class Transaction {
    String key;
    String purpose;
    double amount;
    public Transaction(String key, String purpose, double amount) {
        this.key = key;
        this.purpose = purpose;
        this.amount = amount;
    }
}
