package org.weather.data.repository;

import org.weather.data.model.WeatherInfo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WeatherInfoFileRepositoryImpl implements WeatherInfoRepository {

    private static final String FILE_PATH = "weather_info.csv";

    public void saveWeatherInfo(WeatherInfo weatherInfo) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH, true))) {
            // Wyciągam wszystkie obiekty z pliku
            final List<WeatherInfo> weatherInfos = loadWeatherInfosFromFile();
            // Czyścimy plik, żeby był pusty
            Files.write(Path.of(FILE_PATH), new byte[0]);
            // Odfiltrowujemy obiektym, które by nam powodowały duplikaty
            final List<WeatherInfo> updatedWeatherInfos = weatherInfos.stream()
                    .filter(wf -> !wf.getCity().equalsIgnoreCase(weatherInfo.getCity()))
                    .collect(Collectors.toList());
            // Dodajemy bierzącą pogodę dla danej lokalizacji
            updatedWeatherInfos.add(weatherInfo);

            // Zapisujemy wszystkie prognozy do pliku
            updatedWeatherInfos.forEach(wf -> writer.println(weatherInfoToCsvString(wf)));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public WeatherInfo getWeatherInfoByCity(String city) {
        // Pobieramy wszystkie prognozy
        List<WeatherInfo> weatherInfos = loadWeatherInfosFromFile();
        // Robimy stream z listy, filtrujemy obiekty, których nazwa miasta równa się przekazanej do metody nazwie miasta
        // znajdujemy pierwszy bo założenie jest, że się nie duplikują
        return weatherInfos.stream()
                .filter(info -> info.getCity().equalsIgnoreCase(city))
                .findFirst()
                .orElse(null);
    }

    private List<WeatherInfo> loadWeatherInfosFromFile() {
        List<WeatherInfo> weatherInfos = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                WeatherInfo weatherInfo = csvStringToWeatherInfo(line);
                if (weatherInfo != null) {
                    weatherInfos.add(weatherInfo);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return weatherInfos;
    }

    private String weatherInfoToCsvString(WeatherInfo weatherInfo) {
        return String.format("%s,%s,%s,%.2f,%.2f,%.2f,%s,%.2f",
                weatherInfo.getCity(),
                weatherInfo.getCountry(),
                weatherInfo.getDateTime(),
                weatherInfo.getTemperature(),
                weatherInfo.getPressure(),
                weatherInfo.getHumidity(),
                weatherInfo.getWindDirection(),
                weatherInfo.getWindSpeed());
    }

    private WeatherInfo csvStringToWeatherInfo(String csvString) {
        String[] fields = csvString.split(",");
        if (fields.length == 8) {
            return new WeatherInfo(
                    fields[0],
                    fields[1],
                    fields[2],
                    Double.parseDouble(fields[3]),
                    Double.parseDouble(fields[4]),
                    Double.parseDouble(fields[5]),
                    fields[6],
                    Double.parseDouble(fields[7])
            );
        } else {
            System.out.println("CSV format is incorrect");
            return null;
        }
    }

}
