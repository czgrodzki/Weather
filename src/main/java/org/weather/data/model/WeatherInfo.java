package org.weather.data.model;

public class WeatherInfo extends Entity {

    private final String dateTime;
    private final double temperature;
    private final double pressure;
    private final double humidity;
    private final String windDirection;
    private final double windSpeed;

    public WeatherInfo(String city, String country, String dateTime, double temperature, double pressure, double humidity, String windDirection, double windSpeed) {
        super(city, country);
        this.dateTime = dateTime;
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.windDirection = windDirection;
        this.windSpeed = windSpeed;
    }

    public String getDateTime() {
        return dateTime;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getPressure() {
        return pressure;
    }

    public double getHumidity() {
        return humidity;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    @Override
    public String toString() {
        return "WeatherInfo{" +
                "dateTime='" + dateTime + '\'' +
                ", temperature=" + temperature +
                ", pressure=" + pressure +
                ", humidity=" + humidity +
                ", windDirection='" + windDirection + '\'' +
                ", windSpeed=" + windSpeed +
                "} " + super.toString();
    }

}
