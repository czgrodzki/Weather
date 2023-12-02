package org.weather.data.repository;

import org.weather.data.model.WeatherInfo;

public interface WeatherInfoRepository {

    void saveWeatherInfo(WeatherInfo weatherInfo);

    WeatherInfo getWeatherInfoByCity(String city);
}
