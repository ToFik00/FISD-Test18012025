package org.piva.fisdtest18012025.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import org.piva.fisdtest18012025.httpclient.HttpClient;
import org.piva.fisdtest18012025.httpclient.HttpClientImpl;
import org.piva.fisdtest18012025.json.entity.exchange.ExchangeRates;
import org.piva.fisdtest18012025.json.entity.weather.WeatherTemp;
import org.piva.fisdtest18012025.util.PropertiesReader;

import java.util.Map;

public class CommandManager {
    public String generateResponse(String userInput) {
        String command = userInput.substring(0, userInput.indexOf(" ")).toLowerCase();
        String[] args = userInput.substring(userInput.indexOf(" ") + 1).split(" ");
        return switch (Command.getByValue(command)) {
            case LIST -> listCommand();
            case WEATHER -> weatherCommand(args[0]);
            case EXCHANGE -> exchangeCommand(args[0]);
            case QUIT -> quitCommand();
            case null -> "I don't understand that command. Try '/list' for a list of commands.";
        };
    }

    private String listCommand() {
        return Command.getAllCommandsValues().toString();
    }

    private String weatherCommand(String city) {
        if (city == null) {
            return "No weather command with city found.";
        }
        PropertiesReader propertiesReader = new PropertiesReader();

        String apiKey = propertiesReader.getProperty("api.key.weather");
        String apiUrl = propertiesReader.getProperty("api.url.weather");

        HttpClient httpClient = new HttpClientImpl();
        String json = httpClient.get(apiUrl, Map.of("Content-Type", "application/json"),
                Map.of("q", city, "appid", apiKey));
        ObjectMapper mapper = new ObjectMapper();
        WeatherTemp weatherTemp;
        try {
            weatherTemp = mapper.readValue(json, WeatherTemp.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        int result = (int) (weatherTemp.getMain().getTemp() - 273.15);

        return "Temperature in the " + city + ": " + result + "°C";
    }

    private String exchangeCommand(String currency) {
        if (currency == null) {
            return "No exchange command with currency found.";
        }
        PropertiesReader propertiesReader = new PropertiesReader();

        String rub = "RUB";
        String apiKey = propertiesReader.getProperty("api.key.currency");
        String apiUrl = propertiesReader.getProperty("api.url.currency");

        HttpClient httpClient = new HttpClientImpl();
        String json = httpClient.get(apiUrl, Map.of("Content-Type", "application/json"),
                Map.of("app_id", apiKey, "symbols", rub + "," + currency));
        ObjectMapper mapper = new ObjectMapper();
        ExchangeRates exchangeRates;
        try {
            exchangeRates = mapper.readValue(json, ExchangeRates.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Map<String, Double> rates = exchangeRates.getRates();

        if (rates.containsKey(currency)) {
            return  "Rate " + currency + ": " + rates.get(rub);
        } else {
            return "Currency " + currency + " not found.";
        }
    }

    private String quitCommand() {
        Platform.exit();
        return null;
    }
}
