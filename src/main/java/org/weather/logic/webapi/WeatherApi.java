package org.weather.logic.webapi;

import org.weather.data.model.WeatherInfo;

public interface WeatherApi {
    WeatherInfo getWeather(String city);


}
