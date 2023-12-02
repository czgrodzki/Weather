package org.weather.data.repository;

import org.weather.data.model.Location;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class LocationFileRepositoryImpl implements LocationRepository {
    private static final String FILE_PATH = "locations.csv";

    @Override
    public void addLocation(Location location) {
        List<Location> locations = loadLocationsFromFile();
        locations.add(location);
        saveLocations(locations);
    }

    @Override
    public List<Location> getLocations() {
        return loadLocationsFromFile();
    }

    @Override
    public Location getLocationByCity(String city) {
        return loadLocationsFromFile().stream()
                .filter(location -> location.getCity().equalsIgnoreCase(city))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void saveLocations(List<Location> locations) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (Location location : locations) {
                writer.println(locationToCsvString(location));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Location> loadLocationsFromFile() {
        List<Location> locations = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Location location = csvStringToLocation(line);
                if (location != null) {
                    locations.add(location);
                }
            }
        } catch (IOException e) {
           e.printStackTrace();
        }
        return locations;
    }

    private String locationToCsvString(Location location) {
        return String.format("%s,%s,%.2f,%.2f",
                location.getCity(),
                location.getCountry(),
                location.getLat(),
                location.getLon());
    }

    private Location csvStringToLocation(String csvString) {
        String[] fields = csvString.split(",");
        if (fields.length == 4) {
            return new Location(
                    fields[0],
                    fields[1],
                    Double.parseDouble(fields[2]),
                    Double.parseDouble(fields[3])
            );
        } else {
            System.out.println("CSV format is incorrect");
            return null;
        }
    }
}
