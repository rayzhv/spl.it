package edu.gatech.split.Model;

/**
 * Created by tonyzhang on 10/20/18.
 */

public class User {
    public String name;
    public double total;
    String key;


    // Default no-arg constructor required by Firebase
    public User() {

    }

    public User(String key, String name, double total) {
        this.key = key;
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