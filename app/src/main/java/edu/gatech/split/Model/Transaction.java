package edu.gatech.split.Model;

public class Transaction {
    private String key;
    private String payer;
    private String purpose;
    private double amount;

    // Default no-arg constructor required by Firebase
    public Transaction() {

    }

    public Transaction(String key, String purpose, double amount, String payer) {
        this.key = key;
        this.purpose = purpose;
        this.amount = amount;
        this.payer = payer;
    }

    public String getPurpose() {
        return purpose;
    }

    public String getPayer() { return payer; }

    public double getAmount() {
        return amount;
    }

}
