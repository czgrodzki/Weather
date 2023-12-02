package org.weather.logic.service;

import org.weather.data.model.Location;
import org.weather.data.model.WeatherInfo;
import org.weather.data.repository.LocationFileRepositoryImpl;
import org.weather.data.repository.LocationRepository;
import org.weather.data.repository.WeatherInfoFileRepositoryImpl;
import org.weather.data.repository.WeatherInfoRepository;
import org.weather.logic.webapi.OpenWeatherMapApi;
import org.weather.logic.webapi.WeatherApi;
import org.weather.logic.webapi.WeatherstackApi;

import java.util.List;

public class LocationService {

    private final LocationRepository locationRepository;
    private final WeatherApi weatherApi;
    private final OpenWeatherMapApi openWeatherMapApi;
    private final WeatherInfoRepository weatherInfoFileRepository;

    // Tworzymy w konstruktorze instancje klas, do których LocationService deleguje konkretne zachowania
    public LocationService() {
        this.locationRepository = new LocationFileRepositoryImpl();
        this.weatherApi = new WeatherstackApi();
        this.openWeatherMapApi = new OpenWeatherMapApi();
        this.weatherInfoFileRepository = new WeatherInfoFileRepositoryImpl();
    }

    public void addLocation(String city, String country, double lat, double lon) {
        if (validateCoordinates(lat, lon)) {
            Location location = new Location(city, country, lat, lon);
            locationRepository.addLocation(location);
            System.out.println("Lokalizacja dodana pomyślnie.");
        } else {
            System.out.println("Błędne współrzędne. Sprawdź zakres lat (-90 do 90) i lon (-180 do 180).");
        }
    }

    public List<Location> getAvailableLocations() {
        return locationRepository.getLocations();
    }

    public WeatherInfo getAverageWeather(String city) {
        Location location = locationRepository.getLocationByCity(city);
        if (location != null) {
            WeatherInfo weatherInfoStack = weatherApi.getWeather(city);
            WeatherInfo weatherInfoFile = openWeatherMapApi.getWeather(city);

            if (weatherInfoStack != null && weatherInfoFile != null) {
                WeatherInfo averageWeather = calculateAverageWeather(weatherInfoStack, weatherInfoFile);
                weatherInfoFileRepository.saveWeatherInfo(averageWeather);
                return averageWeather;
            } else {
                System.out.println("Brak prognozy pogody dla miasta: " + city);
            }
        } else {
            System.out.println("Brak lokalizacji dla miasta: " + city);
        }
        return null;
    }

    public WeatherInfo getWeatherFromFile(String city) {
        return weatherInfoFileRepository.getWeatherInfoByCity(city);
    }

    public WeatherInfo getWeatherByCoordinates(String city) {
        final Location locationByCity = locationRepository.getLocationByCity(city);
        return openWeatherMapApi.getWeatherByCoordinates(locationByCity.getLat(), locationByCity.getLon());
    }

    private boolean validateCoordinates(double lat, double lon) {
        return (lat >= -90 && lat <= 90) && (lon >= -180 && lon <= 180);
    }

    private WeatherInfo calculateAverageWeather(WeatherInfo weatherInfoStack, WeatherInfo weatherInfoFile) {
        double averageTemperature = (weatherInfoStack.getTemperature() + weatherInfoFile.getTemperature()) / 2;
        double averagePressure = (weatherInfoStack.getPressure() + weatherInfoFile.getPressure()) / 2;
        double averageHumidity = (weatherInfoStack.getHumidity() + weatherInfoFile.getHumidity()) / 2;
        double averageWindSpeed = (weatherInfoStack.getWindSpeed() + weatherInfoFile.getWindSpeed()) / 2;

        return new WeatherInfo(
                weatherInfoStack.getCity(),
                weatherInfoStack.getCountry(),
                weatherInfoStack.getDateTime(),
                averageTemperature,
                averagePressure,
                averageHumidity,
                weatherInfoStack.getWindDirection(),
                averageWindSpeed
        );
    }
}
