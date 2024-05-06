package org.w1;

import org.json.JSONArray;
import spark.Request;
import spark.Response;
import static spark.Spark.*;
public class WeatherAPI {

    private static String getWeatherDataAsJson(Request req, Response res) {
        JSONArray jsonArray = new JSONArray();
        try {
            jsonArray = WeatherDataFetcher.getWeatherDataAsJson();
        } catch (Exception e) {
            e.printStackTrace();
            res.status(500);
            return "Internal Server Error";
        }
        res.type("application/json");
        return jsonArray.toString();
    }

    public static void main(String[] args) {
        port(8080);
        before((req, res) -> {
            res.header("Access-Control-Allow-Origin", "*");
            res.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            res.header("Access-Control-Allow-Headers", "Content-Type, Authorization, Accept");
            res.header("Access-Control-Allow-Credentials", "true");
        });
        get("/weather", (req, res) -> getWeatherDataAsJson(req, res));
    }



}
