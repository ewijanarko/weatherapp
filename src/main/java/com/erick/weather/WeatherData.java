package com.erick.weather;

import java.time.LocalDateTime;

// WeatherData classes to support forecast data
public class WeatherData {
    Main main;
    Wind wind;
    Weather[] weather;
    String name;
    Coord coord; // Added for location coordinates

    // Nested class to match the "main" object in the JSON
    class Main {
        double temp;
        double humidity;
        double feels_like;
        double temp_min;
        double temp_max;
    }

    // Nested class to match the "wind" object in the JSON
    class Wind {
        double speed;
        double deg; // Wind direction
    }

    // Nested class to match the objects inside the "weather" array in the JSON
    class Weather {
        String main;
        String description;
        String icon;
    }

    // Nested class for coordinates
    class Coord {
        double lat;
        double lon;
    }
}

// Class for forecast data
class ForecastData {
    ForecastItem[] list;
    City city;

    class ForecastItem {
        Main main;
        Weather[] weather;
        Wind wind;
        String dt_txt; // Date/time text
        long dt; // Unix timestamp

        class Main {
            double temp;
            double humidity;
            double temp_min;
            double temp_max;
        }

        class Weather {
            String main;
            String description;
            String icon;
        }

        class Wind {
            double speed;
        }
    }

    class City {
        String name;
        String country;
        Coord coord;

        class Coord {
            double lat;
            double lon;
        }
    }
}

// Class for search history
class SearchHistoryItem {
    private String cityName;
    private LocalDateTime searchTime;
    private double temperature;
    private String conditions;
    private String units;

    public SearchHistoryItem(String cityName, double temperature, String conditions, String units) {
        this.cityName = cityName;
        this.searchTime = LocalDateTime.now();
        this.temperature = temperature;
        this.conditions = conditions;
        this.units = units;
    }

    // Getters
    public String getCityName() {
        return cityName;
    }

    public LocalDateTime getSearchTime() {
        return searchTime;
    }

    public double getTemperature() {
        return temperature;
    }

    public String getConditions() {
        return conditions;
    }

    public String getUnits() {
        return units;
    }

    @Override
    public String toString() {
        String tempUnit = units.equals("metric") ? "°C" : "°F";
        return String.format("%s - %.1f%s, %s (%s)",
                cityName, temperature, tempUnit, conditions,
                searchTime.toLocalDate().toString() + " " +
                        searchTime.toLocalTime().toString().substring(0, 8));
    }
}