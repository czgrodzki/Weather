package org.weather.data.repository;

import org.weather.data.model.Location;

import java.util.List;

public interface LocationRepository {

    void addLocation(Location location);
    List<Location> getLocations();
    Location getLocationByCity(String city);
    void saveLocations(List<Location> locations);

}
