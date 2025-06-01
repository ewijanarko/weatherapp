A modern JavaFX weather application that provides current weather conditions, 5-day forecasts, search history tracking, and dynamic backgrounds based on time of day.

Features

1. Current Weather
- Real-time weather data for any city worldwide
- Temperature, humidity, wind speed, and weather conditions
- Weather icons for visual representation
- Support for both Celsius and Fahrenheit units

2. 5-Day Forecast
- Extended weather forecast with daily predictions
- Temperature trends and weather conditions
- Easy-to-read tabular format

3. Search History
- Automatic tracking of weather searches with timestamps
- View up to 20 recent searches
- Clear history functionality
- Persistent during app session

4. Dynamic Backgrounds
- Time-based background gradients that change throughout the day:
  - Night (12 AM - 6 AM): Dark blue gradient
  - Morning (6 AM - 12 PM): Light blue gradient
  - Afternoon (12 PM - 6 PM): Bright blue gradient
  - Evening (6 PM - 8 PM): Beautiful sunset gradient
  - Night (8 PM - 12 AM): Dark gray gradient

User Interface
- Clean, tabbed interface for organized information
- Responsive design with proper error handling
- Keyboard support (Enter key for search)

Technologies Used

- Java 17: Core programming language
- JavaFX 17.0.6: Modern GUI framework
- Maven: Build automation and dependency management
- Gson 2.10.1: JSON parsing library
- OpenWeatherMap API: Weather data source

Prerequisites
Before running the application, ensure you have:

- Java 17 or higher installed
- Maven 3.6+ installed
- Internet connection for weather data
- OpenWeatherMap API key (free registration required)
Getting Started
How to Use
Basic Weather Search
1. Launch the application
2. Enter a city name in the text field
3. Select temperature unit (Celsius or Fahrenheit)
4. Click "Get Weather" or press Enter
5. View results in the "Current Weather" tab

Viewing Forecast
1. Search for a city (as above)
2. Click the "Forecast" tab
3. View 5-day weather predictions

Checking Search History
1. Click the "Search History" tab
2. View your recent searches with timestamps
3. Click "Clear History" to reset if needed

Project Structure
weatherapp/
├── pom.xml                          # Maven configuration
├── README.md                        
└── src/
    └── main/
        ├── java/
        │   └── com/erick/weather/
        │       ├── Main.java               # Application entry point
        │       ├── WeatherAppGUI.java      # Main GUI class with tabs
        │       ├── WeatherService.java     # API service class
        │       └── WeatherData.java        # Data models
        └── resources/
            └── icons/              # Weather icons (optional)
```

Implementation Details
Architecture Overview
The application follows a clean separation of concerns:

- Main.java: Application launcher
- WeatherAppGUI.java: User interface and event handling
- WeatherService.java: HTTP API communication
- WeatherData.java: Data models and search history

API Integration
- Uses OpenWeatherMap REST API
- Two endpoints: current weather and 5-day forecast
- Handles HTTP errors and JSON parsing
- Rate limiting: 1,000 calls per day (free tier)

GUI Framework
- JavaFX for modern, responsive UI
- TabPane for organized content display
- Concurrent tasks for non-blocking API calls
- Dynamic styling with CSS-like properties

Data Management
- In-memory storage for search history
- Gson for efficient JSON serialization/deserialization
- Proper error handling and user feedback

Error Handling
The application handles various error scenarios:

- Invalid city names: User-friendly error messages
- Network connectivity issues: Timeout and connection error handling
- API key problems: Clear error reporting
- Rate limiting: Appropriate error messages
- Invalid API responses: Graceful degradation

API Usage
Current Weather Endpoint
```
GET https://api.openweathermap.org/data/2.5/weather
Parameters:
- q: city name
- appid: API key
- units: metric/imperial
```

Forecast Endpoint
```
GET https://api.openweathermap.org/data/2.5/forecast
Parameters:
- q: city name
- appid: API key
- units: metric/imperial
```

Customization

Adding Weather Icons
1. Create `src/main/resources/icons/` directory
2. Add PNG files named after OpenWeatherMap icon codes:
   - `01d.png` (clear sky day)
   - `02d.png` (few clouds day)
   - `10d.png` (rain day)
   - etc.

Modifying Background Colors
Edit the `setupDynamicBackground()` method in `WeatherAppGUI.java` to customize colors:
```java
// Example: Custom sunset colors
new Stop(0, Color.web("#your-color-here")),
new Stop(1, Color.web("#your-other-color"))
```

Extending History
Modify the history limit in `addToHistory()` method:
```java
// Keep only last X searches
if (searchHistory.size() > X) {
    searchHistory = searchHistory.subList(0, X);
}
```

Testing
Manual Testing Checklist
- [ ] Search for valid city names
- [ ] Test invalid city names
- [ ] Switch between Celsius and Fahrenheit
- [ ] Check all three tabs functionality
- [ ] Verify search history tracking
- [ ] Test "Clear History" button
- [ ] Observe dynamic background changes
- [ ] Test keyboard shortcuts (Enter key)

Unit Testing
The project is configured with JUnit Jupiter for future unit tests:
```bash
mvn test
```

Building for Production

Create Distribution Package
```bash
# Build with production profile
mvn clean package -Pprod

# The JAR will be in target/ directory
ls target/weatherapp-1.0-SNAPSHOT.jar
```

System Requirements
- Runtime: Java 17+ with JavaFX modules
- Memory: Minimum 256MB RAM
- Network: Internet connection required
- OS: Windows, macOS, or Linux
API Key Issues
- Verify your API key is active on OpenWeatherMap
- Check for typos in the API key
- Ensure you haven't exceeded the daily limit
Build Errors
```bash
# Clean and rebuild
mvn clean compile
```
UI Not Displaying
- Ensure JavaFX is properly installed
- Check Java version compatibility
- Verify all dependencies are downloaded
