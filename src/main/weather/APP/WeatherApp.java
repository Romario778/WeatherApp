package main.weather.APP;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;

public class WeatherApp {

    // API ключ, полученный на сайте openweathermap.org
    private static final String API_KEY = "61b009e9774f3ac0787a27eb53fe7381";
    private static final String API_URL = "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите название города:");
        String cityName = scanner.nextLine();

        try {
            // Формирование URL для запроса погоды
            String apiUrl = String.format(API_URL, cityName, API_KEY);

            // Отправка запроса и получение результата
            String jsonResponse = sendHttpRequest(apiUrl);

            if (jsonResponse != null) {
                // Обработка ответа
                parseAndDisplayWeather(jsonResponse);
            } else {
                System.out.println("Не удалось получить данные о погоде.");
            }
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }

        scanner.close();
    }

    // Метод для отправки HTTP запроса
    private static String sendHttpRequest(String apiUrl) throws Exception {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Проверка кода ответа
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();
            return response.toString();
        } else {
            System.out.println("Ошибка в запросе: " + responseCode);
            return null;
        }
    }

    // Метод для обработки и вывода данных о погоде
    private static void parseAndDisplayWeather(String jsonResponse) {
        JSONObject jsonObject = new JSONObject(jsonResponse);

        // Извлекаем данные о температуре, погоде и др.
        JSONObject main = jsonObject.getJSONObject("main");
        double temp = main.getDouble("temp");
        int humidity = main.getInt("humidity");

        JSONObject wind = jsonObject.getJSONObject("wind");
        double windSpeed = wind.getDouble("speed");

        JSONObject weather = jsonObject.getJSONArray("weather").getJSONObject(0);
        String description = weather.getString("description");

        String cityName = jsonObject.getString("name");

        // Вывод данных
        System.out.println("Погода в городе: " + cityName);
        System.out.println("Температура: " + temp + "°C");
        System.out.println("Ветер: " + windSpeed + "М/С");
        System.out.println("Влажность: " + humidity + "%");
        System.out.println("Описание: " + description);
    }
}
