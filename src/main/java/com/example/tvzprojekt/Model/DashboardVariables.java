package com.example.tvzprojekt.Model;

import com.example.tvzprojekt.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DashboardVariables {

    // kartica studenti
    public static int brojStudenata;
    public static int najGodiste;
    public static String najSmjer;
    public static int brucosi;
    public static int diplomirani;

    // kartica profesori
    public static int brojProfesora;
    public static String najpokrivenijiSmjer;
    public static String deficitarniSmjer;
    public static int studentiPoProfesoru;

    // kartica transakcije
    public static double bilanca;
    public static double prihodi;
    public static double rashodi;
    public static int brojTransakcija;

    // kartica eventi
    public static int brojEvenata;
    public static String zadnjiEvent;
    public static int ukupnaPosjecenost;


    public static void initialize() {
        try(Connection connection = DatabaseConnector.getConnection()) {
            brojStudenata = brojStudenata(connection);
            najGodiste = izvuciNajGodinu(connection);
            najSmjer = izvuciNajSmjer(connection);
            brucosi = brojBrucosa(connection);
            diplomirani = brojDiplomiranih(connection);
            //--------------------------
            brojProfesora = brojProfesora(connection);
            najpokrivenijiSmjer = izvuciNajpokrivenijiSmjer(connection);
            deficitarniSmjer = izvuciNajdeficitarnijiSmjer(connection);
            studentiPoProfesoru = (int) (brojStudenata / brojProfesora);
            //--------------------------
            bilanca = izvuciBilancu(connection);
            prihodi = izvuciPrihode(connection);
            rashodi = izvuciRashode(connection);
            brojTransakcija = brojTransakcija(connection);
            //--------------------------
            brojEvenata = brojEvenata(connection);
            zadnjiEvent = izvuciZadnjiEvent(connection);
            ukupnaPosjecenost = izvuciPosjecenost(connection);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // sluzi za brojanje tabela da ne ponavljam kod u 4 funkcije
    private static int prebrojavanje(Connection connection, String query) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getInt(1);
    }
    private static int brojStudenata(Connection connection) throws SQLException {
        String query = "SELECT COUNT(*) FROM STUDENTI";
        return prebrojavanje(connection, query);
    }

    private static int brojEvenata(Connection connection) throws SQLException {
        String query = "SELECT COUNT(*) FROM EVENTI";
        return prebrojavanje(connection, query);
    }

    private static int brojProfesora(Connection connection) throws SQLException {
        String query = "SELECT COUNT(*) FROM PROFESORI";
        return prebrojavanje(connection, query);
    }

    private static int brojTransakcija(Connection connection) throws SQLException {
        String query = "SELECT COUNT(*) FROM TRANSAKCIJE";
        return prebrojavanje(connection, query);
    }

    private static int brojBrucosa(Connection connection) throws SQLException {
        String query = "SELECT COUNT(*) FROM STUDENTI WHERE GODINA = 1";
        return prebrojavanje(connection, query);
    }

    private static int brojDiplomiranih(Connection connection) throws SQLException {
        String query = "SELECT COUNT(*) FROM STUDENTI WHERE GODINA = 5";
        return prebrojavanje(connection, query);
    }

    private static String izvuciNajSmjer(Connection connection) throws SQLException {
        String query = "SELECT SMJER, COUNT(SMJER) AS BROJ_STUDENTI\n" +
                "FROM studenti\n" +
                "GROUP BY SMJER\n" +
                "ORDER BY BROJ_STUDENTI DESC\n" +
                "LIMIT 1;";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getString("SMJER");
    }

    private static int izvuciNajGodinu(Connection connection) throws SQLException {
        String query = "SELECT YEAR(DATUM) AS GODINA_RODENJA, COUNT(YEAR(DATUM)) AS BROJ_STUDENTI\n" +
                "FROM studenti\n" +
                "GROUP BY GODINA_RODENJA\n" +
                "ORDER BY BROJ_STUDENTI DESC\n" +
                "LIMIT 1;";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return Integer.parseInt(resultSet.getString("GODINA_RODENJA"));
    }

    private static String izvuciNajpokrivenijiSmjer(Connection connection) throws SQLException{
        String query = "SELECT SMJER, COUNT(SMJER) AS BROJ_PROFESORA\n" +
                "FROM profesori\n" +
                "GROUP BY SMJER\n" +
                "ORDER BY BROJ_PROFESORA DESC\n" +
                "LIMIT 1;";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getString("SMJER");
    }

    private static String izvuciNajdeficitarnijiSmjer(Connection connection) throws SQLException{
        String query = "SELECT SMJER, COUNT(SMJER) AS BROJ_PROFESORA\n" +
                "FROM profesori\n" +
                "GROUP BY SMJER\n" +
                "ORDER BY BROJ_PROFESORA ASC\n" +
                "LIMIT 1;";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getString("SMJER");
    }

    private static double izvuciBilancu(Connection connection) throws SQLException {
        String query = "SELECT SUM(IZNOS) AS UKUPNA_BILANCA\n" +
                "FROM transakcije;";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getDouble("UKUPNA_BILANCA");
    }

    private static double izvuciPrihode(Connection connection) throws SQLException {
        String query = "SELECT SUM(IZNOS) AS PRIHODI\n" +
                "FROM transakcije\n" +
                "WHERE IZNOS > 0;";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getDouble("PRIHODI");
    }

    private static double izvuciRashode(Connection connection) throws SQLException {
        String query = "SELECT SUM(IZNOS) AS RASHODI\n" +
                "FROM transakcije\n" +
                "WHERE IZNOS < 0;";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getDouble("RASHODI");
    }

    private static String izvuciZadnjiEvent(Connection connection) throws SQLException {
        String query = "SELECT NAZIV_EVENTA\n" +
                "FROM eventi\n" +
                "ORDER BY DATUM DESC\n" +
                "LIMIT 1;";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getString("NAZIV_EVENTA");
    }

    private static int izvuciPosjecenost(Connection connection) throws SQLException {
        String query = "SELECT SUM(POSJECENOST) AS UKUPNA_POSJECENOST\n" +
                "FROM eventi;";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getInt("UKUPNA_POSJECENOST");
    }
}
