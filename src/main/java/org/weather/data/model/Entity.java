package org.weather.data.model;

import java.util.UUID;

public abstract class Entity {

    private final UUID uuid;
    private final String city;
    private final String country;

    public Entity(String city, String country) {
        this.uuid = UUID.nameUUIDFromBytes((city + country).getBytes());
        this.city = city;
        this.country = country;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "uuid=" + uuid +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                '}';
    }

}
