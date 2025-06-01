package com.erick.weather;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.paint.CycleMethod;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WeatherAppGUI extends Application {

    private TextField cityInput = new TextField();
    private Button getWeatherButton = new Button("Get Weather");
    private ImageView weatherIcon = new ImageView();

    // Unit selection
    private ToggleGroup unitGroup = new ToggleGroup();
    private RadioButton celsiusButton = new RadioButton("Celsius");
    private RadioButton fahrenheitButton = new RadioButton("Fahrenheit");

    // Current weather labels
    private Label locationLabel = new Label("Location: --");
    private Label temperatureLabel = new Label("Temperature: --");
    private Label humidityLabel = new Label("Humidity: --%");
    private Label windSpeedLabel = new Label("Wind Speed: --");
    private Label conditionsLabel = new Label("Conditions: --");

    // NEW: Forecast display
    private VBox forecastBox = new VBox(5);
    private Label forecastTitle = new Label("5-Day Forecast");

    // NEW: History tracking
    private List<SearchHistoryItem> searchHistory = new ArrayList<>();
    private ListView<SearchHistoryItem> historyListView = new ListView<>();
    private Button clearHistoryButton = new Button("Clear History");

    // Main layout containers
    private VBox root = new VBox();
    private TabPane tabPane = new TabPane();

    private Gson gson = new Gson();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Weather Information App");

        setupUI();
        setupDynamicBackground();
        
        Scene scene = new Scene(root, 600, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setupUI() {
        root.setSpacing(10);
        root.setPadding(new Insets(15));

        // Configure unit selection
        celsiusButton.setToggleGroup(unitGroup);
        celsiusButton.setSelected(true);
        fahrenheitButton.setToggleGroup(unitGroup);

        HBox unitSelectionBox = new HBox(10, celsiusButton, fahrenheitButton);
        unitSelectionBox.setAlignment(Pos.CENTER);

        weatherIcon.setFitHeight(50);
        weatherIcon.setFitWidth(50);

        getWeatherButton.setOnAction(event -> {
            String city = cityInput.getText();
            if (city != null && !city.trim().isEmpty()) {
                fetchWeatherData(city);
                fetchForecastData(city);
            }
        });

        cityInput.setPromptText("Enter city name");
        cityInput.setOnAction(event -> getWeatherButton.fire()); // Enter key support

        // Setup tabs
        setupTabs();

        root.getChildren().addAll(
                new Label("Enter City:"),
                cityInput,
                unitSelectionBox,
                getWeatherButton,
                tabPane
        );
    }

    private void setupTabs() {
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // Current Weather Tab
        Tab currentTab = new Tab("Current Weather");
        VBox currentWeatherBox = new VBox(10);
        currentWeatherBox.setPadding(new Insets(10));
        currentWeatherBox.getChildren().addAll(
                weatherIcon,
                locationLabel,
                temperatureLabel,
                humidityLabel,
                windSpeedLabel,
                conditionsLabel
        );
        currentTab.setContent(new ScrollPane(currentWeatherBox));

        // Forecast Tab
        Tab forecastTab = new Tab("Forecast");
        VBox forecastContainer = new VBox(10);
        forecastContainer.setPadding(new Insets(10));
        
        forecastTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        forecastBox.setPadding(new Insets(10));
        forecastBox.setStyle("-fx-border-color: lightgray; -fx-border-radius: 5;");
        
        forecastContainer.getChildren().addAll(forecastTitle, forecastBox);
        forecastTab.setContent(new ScrollPane(forecastContainer));

        // History Tab
        Tab historyTab = new Tab("Search History");
        VBox historyContainer = new VBox(10);
        historyContainer.setPadding(new Insets(10));
        
        Label historyTitle = new Label("Recent Searches");
        historyTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        historyListView.setPrefHeight(300);
        clearHistoryButton.setOnAction(event -> clearHistory());
        
        historyContainer.getChildren().addAll(historyTitle, historyListView, clearHistoryButton);
        historyTab.setContent(historyContainer);

        tabPane.getTabs().addAll(currentTab, forecastTab, historyTab);
    }

    private void setupDynamicBackground() {
        LocalTime currentTime = LocalTime.now();
        LinearGradient gradient;

        // Determine time of day and set appropriate background
        if (currentTime.isBefore(LocalTime.of(6, 0))) {
            // Night (midnight to 6 AM) - Dark blue gradient
            gradient = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.web("#2c3e50")),
                    new Stop(1, Color.web("#34495e")));
        } else if (currentTime.isBefore(LocalTime.of(12, 0))) {
            // Morning (6 AM to noon) - Light blue gradient
            gradient = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.web("#74b9ff")),
                    new Stop(1, Color.web("#0984e3")));
        } else if (currentTime.isBefore(LocalTime.of(18, 0))) {
            // Afternoon (noon to 6 PM) - Bright blue gradient
            gradient = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.web("#81ecec")),
                    new Stop(1, Color.web("#74b9ff")));
        } else if (currentTime.isBefore(LocalTime.of(20, 0))) {
            // Evening (6 PM to 8 PM) - Sunset gradient
            gradient = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.web("#fd79a8")),
                    new Stop(0.5, Color.web("#fdcb6e")),
                    new Stop(1, Color.web("#fd79a8")));
        } else {
            // Night (8 PM to midnight) - Dark gradient
            gradient = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.web("#2d3436")),
                    new Stop(1, Color.web("#636e72")));
        }

        BackgroundFill backgroundFill = new BackgroundFill(gradient, null, null);
        Background background = new Background(backgroundFill);
        root.setBackground(background);
    }

    private void fetchWeatherData(String city) {
        String selectedUnit = celsiusButton.isSelected() ? "metric" : "imperial";

        Task<WeatherData> task = new Task<>() {
            @Override
            protected WeatherData call() throws Exception {
                String jsonResponse = WeatherService.getWeatherData(city, selectedUnit);
                return gson.fromJson(jsonResponse, WeatherData.class);
            }
        };
        
        task.setOnSucceeded(event -> {
            WeatherData data = task.getValue();
            updateUI(data);
            // Add to search history
            if (data.name != null && data.weather != null && data.weather.length > 0) {
                addToHistory(data.name, data.main.temp, data.weather[0].description, selectedUnit);
            }
        });
        
        task.setOnFailed(event -> {
            locationLabel.setText("Error: Failed to fetch data.");
            System.out.println("Task failed: " + task.getException());
        });
        
        new Thread(task).start();
    }

    private void fetchForecastData(String city) {
        String selectedUnit = celsiusButton.isSelected() ? "metric" : "imperial";

        Task<ForecastData> task = new Task<>() {
            @Override
            protected ForecastData call() throws Exception {
                String jsonResponse = WeatherService.getForecastData(city, selectedUnit);
                return gson.fromJson(jsonResponse, ForecastData.class);
            }
        };
        
        task.setOnSucceeded(event -> updateForecastUI(task.getValue()));
        task.setOnFailed(event -> {
            Platform.runLater(() -> {
                forecastBox.getChildren().clear();
                forecastBox.getChildren().add(new Label("Error: Failed to fetch forecast data."));
            });
        });
        
        new Thread(task).start();
    }

    private void updateUI(WeatherData data) {
        if (data.name == null) {
            locationLabel.setText("Error: City not found");
            return;
        }

        String tempUnit = celsiusButton.isSelected() ? "°C" : "°F";
        String speedUnit = celsiusButton.isSelected() ? "km/h" : "mph";

        Platform.runLater(() -> {
            locationLabel.setText("Location: " + data.name);
            temperatureLabel.setText(String.format("Temperature: %.1f%s", data.main.temp, tempUnit));
            humidityLabel.setText(String.format("Humidity: %.0f%%", data.main.humidity));
            windSpeedLabel.setText(String.format("Wind Speed: %.1f %s", data.wind.speed, speedUnit));

            if (data.weather != null && data.weather.length > 0) {
                conditionsLabel.setText("Conditions: " + data.weather[0].description);
                String iconCode = data.weather[0].icon;
                String iconPath = "/icons/" + iconCode + ".png";
                try {
                    Image image = new Image(getClass().getResourceAsStream(iconPath));
                    weatherIcon.setImage(image);
                } catch (Exception e) {
                    weatherIcon.setImage(null);
                }
            }
        });
    }

    private void updateForecastUI(ForecastData forecast) {
        Platform.runLater(() -> {
            forecastBox.getChildren().clear();
            
            if (forecast == null || forecast.list == null) {
                forecastBox.getChildren().add(new Label("No forecast data available"));
                return;
            }

            String tempUnit = celsiusButton.isSelected() ? "°C" : "°F";
            
            // Show next 5 days (take every 8th item for daily forecast, as API returns 3-hour intervals)
            for (int i = 0; i < Math.min(5, forecast.list.length); i += 8) {
                if (i < forecast.list.length) {
                    ForecastData.ForecastItem item = forecast.list[i];
                    
                    HBox dayBox = new HBox(10);
                    dayBox.setAlignment(Pos.CENTER_LEFT);
                    dayBox.setPadding(new Insets(5));
                    dayBox.setStyle("-fx-border-color: lightblue; -fx-border-radius: 3; -fx-background-color: rgba(173, 216, 230, 0.3);");
                    
                    // Format date
                    String dateStr = item.dt_txt.substring(0, 10); // Get YYYY-MM-DD part
                    Label dateLabel = new Label(dateStr);
                    dateLabel.setPrefWidth(80);
                    
                    // Temperature
                    Label tempLabel = new Label(String.format("%.1f%s", item.main.temp, tempUnit));
                    tempLabel.setPrefWidth(60);
                    
                    // Conditions
                    String conditions = item.weather != null && item.weather.length > 0 ? 
                                      item.weather[0].description : "N/A";
                    Label conditionsLabel = new Label(conditions);
                    conditionsLabel.setPrefWidth(120);
                    
                    dayBox.getChildren().addAll(dateLabel, tempLabel, conditionsLabel);
                    forecastBox.getChildren().add(dayBox);
                }
            }
        });
    }

    private void addToHistory(String cityName, double temperature, String conditions, String units) {
        SearchHistoryItem historyItem = new SearchHistoryItem(cityName, temperature, conditions, units);
        searchHistory.add(0, historyItem); // Add to beginning of list
        
        // Keep only last 20 searches
        if (searchHistory.size() > 20) {
            searchHistory = searchHistory.subList(0, 20);
        }
        
        Platform.runLater(() -> {
            historyListView.getItems().clear();
            historyListView.getItems().addAll(searchHistory);
        });
    }

    private void clearHistory() {
        searchHistory.clear();
        historyListView.getItems().clear();
    }
}