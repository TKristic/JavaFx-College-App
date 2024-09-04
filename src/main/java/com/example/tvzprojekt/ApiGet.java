package com.example.tvzprojekt;

import com.example.tvzprojekt.Model.Institucija;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ApiGet {

    private static final String kljuc = "sdfghjklgzfesrdgthjkiugzfdtrgzhujiokpjiugfdtrzgu";
    private static final String link = "https://fujcisto.com//api.php";

    public static void main(String[] args) {

        try {
            URL url = new URL(link + "?api_key=" + kljuc);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();

                // Parsiraj JSON koristeći JSON Simple
                JSONParser parser = new JSONParser();
                JSONObject jsonObject = (JSONObject) parser.parse(response.toString());

                JSONArray institucijeArray = (JSONArray) jsonObject.get("institucije");

                List<Institucija> institucije = new ArrayList<>();

                for (Object obj : institucijeArray) {
                    JSONObject institucijaJson = (JSONObject) obj;

                    String naziv = (String) institucijaJson.get("naziv");
                    String skracenica = (String) institucijaJson.get("skraćenica");
                    String adresa = (String) institucijaJson.get("adresa");
                    String webStranica = (String) institucijaJson.get("web_stranica");

                    Institucija institucija = new Institucija(naziv, skracenica, adresa, webStranica);
                    institucije.add(institucija);
                }

                // Ispis svih institucija
                for (Institucija institucija : institucije) {
                    System.out.println(institucija);
                }

            } else {
                System.out.println("GET request failed.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

