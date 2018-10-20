package edu.gatech.split.Model;

/**
 * Created by tonyzhang on 10/20/18.
 */

public class User {
    public String name;
    public double paidtotal;
    public double owe;
    String key;


    // Default no-arg constructor required by Firebase
    public User() {

    }

    public User(String key, String name, double paidtotal, double owe) {
        this.key = key;
        this.name = name;
        this.paidtotal = paidtotal;
        this.owe = owe;
    }

    public String getName() {
        return name;
    }

    public double getTotal() {
        return paidtotal;
    }

    public double getOwe() { return owe; }
}