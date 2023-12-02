package org.weather.data.model;

public class Location extends Entity {
    private final double lat;
    private final double lon;

    public Location(String city, String country, double lat, double lon) {
        super(city, country);
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    @Override
    public String toString() {
        return "Location{" +
                "lat=" + lat +
                ", lon=" + lon +
                "} " + super.toString();
        //Żeby wyświetlić UUID, city i country musimy zawołać .toString() z klasy, która rozszerzamy korzystając z super

    }
}
