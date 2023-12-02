package org.weather.logic.webapi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.weather.data.model.WeatherInfo;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class OpenWeatherMapApi implements WeatherApi {

    private static final String API_KEY = "d4e020824aa3616ae34ae2815f6c1fbd";
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather";

    private final ObjectMapper objectMapper;

    public OpenWeatherMapApi() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public WeatherInfo getWeather(String city) {
        String apiUrl = BASE_URL + "?q=" + city + "&appid=" + API_KEY;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .build();

        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode rootNode = objectMapper.readTree(response.body());

            // Parsowanie danych z JSONa do obiektu WeatherInfo
            WeatherInfo weatherInfo = new WeatherInfo(
                    rootNode.path("name").asText(),
                    rootNode.path("sys").path("country").asText(),
                    rootNode.path("dt").asText(),
                    rootNode.path("main").path("temp").asDouble(),
                    rootNode.path("main").path("pressure").asDouble(),
                    rootNode.path("main").path("humidity").asDouble(),
                    rootNode.path("wind").path("deg").asText(),
                    rootNode.path("wind").path("speed").asDouble()
            );

            return weatherInfo;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public WeatherInfo getWeatherByCoordinates(Double lat, Double lon) {
        String apiUrl = BASE_URL + "?lat=" + lat + "&lon=" + lon + "&appid=" + API_KEY;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .build();

        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode rootNode = objectMapper.readTree(response.body());

            // Parsowanie danych z JSONa do obiektu WeatherInfo
            WeatherInfo weatherInfo = new WeatherInfo(
                    rootNode.path("name").asText(),
                    rootNode.path("sys").path("country").asText(),
                    rootNode.path("dt").asText(),
                    rootNode.path("main").path("temp").asDouble(),
                    rootNode.path("main").path("pressure").asDouble(),
                    rootNode.path("main").path("humidity").asDouble(),
                    rootNode.path("wind").path("deg").asText(),
                    rootNode.path("wind").path("speed").asDouble()
            );

            return weatherInfo;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
