package com.example.tvzprojekt.Model;

import java.io.Serializable;

public class User implements Serializable {

    private String username;
    private String hash;
    private Privilegije status;

    public User(String username, String hash, Privilegije status) {
        this.username = username;
        this.hash = hash;
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public String getHash() {
        return hash;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Privilegije getStatus() {
        return status;
    }

    public void setStatus(Privilegije status) {
        this.status = status;
    }
}
