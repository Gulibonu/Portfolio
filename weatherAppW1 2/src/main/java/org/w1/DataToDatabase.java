package org.w1;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class DataToDatabase {
    private static final String API_KEY="d5c3bae4f557e3cc8afa3d4888d87081";
    private static final String DB_URL="jdbc:postgresql://localhost:5432/openweather";
    private static final String USER="postgres";
    private static final String PASSWORD="0880";

    private static JSONObject fetchWeatherData(String city) throws IOException {
        String apiUrl = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + API_KEY;
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        return new JSONObject(response.toString());
    }

    private static void insertWeatherData(JSONObject weatherData) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String sql = "INSERT INTO weather_data (city, temp, pressure) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, weatherData.getString("name"));
            preparedStatement.setDouble(2, weatherData.getJSONObject("main").getDouble("temp"));
            preparedStatement.setDouble(3, weatherData.getJSONObject("main").getDouble("pressure"));
            preparedStatement.executeUpdate();
        }
    }

    public static void main(String[] args) {
        try {
            String city = "Swindon";
            JSONObject weatherData = fetchWeatherData(city);
            insertWeatherData(weatherData);
            System.out.println(weatherData.toString());
            System.out.println("Weather data inserted successfully!");
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
