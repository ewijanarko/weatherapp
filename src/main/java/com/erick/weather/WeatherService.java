package com.erick.weather;

import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class WeatherService {

    private static final String API_KEY = "6b596476828d3ce5fb3e702b98f386b6";

    public static String getWeatherData(String city, String units) {
        try {
            String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8.toString());
            String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + encodedCity + "&appid=" + API_KEY
                    + "&units=" + units;

            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode == 401) {
                return "{\"error\": \"Invalid API Key. Please check your key.\"}";
            }
            if (responseCode != 200) {
                if (responseCode == 404) {
                    return "{\"error\": \"City not found\"}";
                }
                return "{\"error\": \"Could not retrieve weather data. Response code: " + responseCode + "\"}";
            }

            Scanner scanner = new Scanner(url.openStream());
            StringBuilder inline = new StringBuilder();
            while (scanner.hasNext()) {
                inline.append(scanner.nextLine());
            }
            scanner.close();

            return inline.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }

    // To get 5-day weather forecast
    public static String getForecastData(String city, String units) {
        try {
            String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8.toString());
            String apiUrl = "https://api.openweathermap.org/data/2.5/forecast?q=" + encodedCity + "&appid=" + API_KEY
                    + "&units=" + units;

            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode == 401) {
                return "{\"error\": \"Invalid API Key. Please check your key.\"}";
            }
            if (responseCode != 200) {
                if (responseCode == 404) {
                    return "{\"error\": \"City not found\"}";
                }
                return "{\"error\": \"Could not retrieve forecast data. Response code: " + responseCode + "\"}";
            }

            Scanner scanner = new Scanner(url.openStream());
            StringBuilder inline = new StringBuilder();
            while (scanner.hasNext()) {
                inline.append(scanner.nextLine());
            }
            scanner.close();

            return inline.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }
}