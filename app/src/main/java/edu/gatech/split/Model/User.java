package edu.gatech.split.Model;

/**
 * Created by tonyzhang on 10/20/18.
 */

public class User {
    public String name;
    public double total;

    // Default no-arg constructor required by Firebase
    public User() {

    }

    public User(String name, double total) {
        this.name = name;
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public double getTotal() {
        return total;
    }
}