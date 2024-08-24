package com.example.tvzprojekt.Model;

public class Kljucevi<K, V> {
    public static <K, V> boolean provjeraKljuceva(K objektKljuc, V objektVrijednost) {
        return objektKljuc.toString().equals(objektVrijednost.toString());
    }
}
