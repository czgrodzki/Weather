package org.weather.logic.webapi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.weather.data.model.WeatherInfo;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WeatherstackApi implements WeatherApi{
    private static final String API_KEY = "b992a7e1ee0965000ee00774ce1b4f98";
    private static final String BASE_URL = "http://api.weatherstack.com/current";

    private final ObjectMapper objectMapper;

    public WeatherstackApi() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public WeatherInfo getWeather(String city) {
        String apiUrl = BASE_URL + "?access_key=" + API_KEY + "&query=" + city;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .build();

        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode rootNode = objectMapper.readTree(response.body());

            // Parsowanie danych z JSONa do obiektu WeatherInfo
            WeatherInfo weatherInfo = new WeatherInfo(
                    city,
                    rootNode.path("location").path("country").asText(),
                    rootNode.path("current").path("observation_time").asText(),
                    rootNode.path("current").path("temperature").asDouble(),
                    rootNode.path("current").path("pressure").asDouble(),
                    rootNode.path("current").path("humidity").asDouble(),
                    rootNode.path("current").path("wind_dir").asText(),
                    rootNode.path("current").path("wind_speed").asDouble()
            );

            return weatherInfo;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
