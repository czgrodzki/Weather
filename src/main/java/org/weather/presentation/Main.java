package org.weather.presentation;

import org.weather.data.model.Location;
import org.weather.data.model.WeatherInfo;
import org.weather.logic.service.LocationService;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static LocationService locationService;

    public static void main(String[] args) {
        locationService = new LocationService();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Dodaj lokalizację");
            System.out.println("2. Wyświetl dostępne miasta");
            System.out.println("3. Wyświetl uśrednioną prognozę pogody");
            System.out.println("4. Wyświetl prognozę pogody z pliku");
            System.out.println("5. Wyświetl prognozę pogody ze współrzędnych");
            System.out.println("0. Wyjdź");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Przestawiamy kursor do następnej linii

            switch (choice) {
                case 1:
                    addLocationFromConsole(scanner);
                    break;
                case 2:
                    displayAvailableCities();
                    break;
                case 3:
                    displayAverageWeather(scanner);
                    break;
                case 4:
                    displayWeatherFromFile(scanner);
                    break;
                case 5:
                    displayWeatherByCoordinates(scanner);
                    break;
                case 0:
                    System.out.println("Do widzenia!");
                    // Inny sposób zamknięcia aplikacji. Zamiast puszczać "flow" programu do końca, wywołujemy zamknięcie
                    System.exit(0);
                default:
                    System.out.println("Nieprawidłowy wybór. Spróbuj ponownie.");
            }
        }
    }

    // Metody, które wykorzystują serwis LocationService wykonania założeń konkretnych funkcjonalności konkretnych

    private static void addLocationFromConsole(Scanner scanner) {
        System.out.println("Podaj nazwę miasta:");
        String city = scanner.nextLine();

        System.out.println("Podaj nazwę kraju:");
        String country = scanner.nextLine();

        System.out.println("Podaj szerokość geograficzną (lat):");
        double lat = scanner.nextDouble();

        System.out.println("Podaj długość geograficzną (lon):");
        double lon = scanner.nextDouble();

        locationService.addLocation(city, country, lat, lon);
        System.out.println("Lokalizacja dodana pomyślnie.");
    }

    private static void displayAvailableCities() {
        List<Location> locations = locationService.getAvailableLocations();
        if (locations.isEmpty()) {
            System.out.println("Brak dostępnych lokalizacji.");
        } else {
            System.out.println("Dostępne miasta:");
            for (Location location : locations) {
                System.out.println(location.getCity() + ", " + location.getCountry());
            }
        }
    }

    private static void displayAverageWeather(Scanner scanner) {
        System.out.println("Podaj nazwę miasta, dla którego chcesz prognozę:");
        String city = scanner.nextLine();

        WeatherInfo averageWeather = locationService.getAverageWeather(city);
        if (averageWeather != null) {
            System.out.println("Uśredniona prognoza pogody:");
            System.out.println(averageWeather);
        } else {
            System.out.println("Brak prognozy dla podanego miasta.");
        }
    }

    private static void displayWeatherFromFile(Scanner scanner) {
        System.out.println("Podaj nazwę miasta, dla którego chcesz prognozę z pliku:");
        String city = scanner.nextLine();

        WeatherInfo weatherFromFile = locationService.getWeatherFromFile(city);
        if (weatherFromFile != null) {
            System.out.println("Prognoza pogody z pliku:");
            System.out.println(weatherFromFile);
        } else {
            System.out.println("Brak prognozy w pliku dla podanego miasta.");
            locationService.getAverageWeather(city);
        }
    }

    private static void displayWeatherByCoordinates(Scanner scanner) {
        System.out.println("Podaj nazwę miasta, dla którego chcesz prognozę ze współrzędnych:");
        String city = scanner.nextLine();
        final WeatherInfo weatherByCoordinates = locationService.getWeatherByCoordinates(city);
        if (weatherByCoordinates != null) {
            System.out.println("Prognoza pogody ze współrzędnych:");
            System.out.println(weatherByCoordinates);
        } else {
            System.out.println("Nie udało się pobrać pogody ze współrzędnych");
        }
    }
}