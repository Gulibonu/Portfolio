package org.w1;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WeatherDataFetcher {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/openweather";
    private static final String USER = "postgres";
    private static final String PASSWORD = "0880";

    public static JSONArray getWeatherDataAsJson() throws SQLException {
        JSONArray jsonArray = new JSONArray();
        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
                String sql = "SELECT * FROM weather_data";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("city", resultSet.getString("city"));
                    jsonObject.put("temperature", resultSet.getDouble("temp"));
                    jsonObject.put("pressure", resultSet.getDouble("pressure"));
                    jsonArray.put(jsonObject);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    public static void main(String[] args) {
        try {
            JSONArray weatherData = getWeatherDataAsJson();
            System.out.println(weatherData.toString());
        } catch (SQLException e) {
            System.err.println("Error retrieving weather data: " + e.getMessage());
        }
    }
}
