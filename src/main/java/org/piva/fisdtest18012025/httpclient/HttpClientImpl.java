package org.piva.fisdtest18012025.httpclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;


public class HttpClientImpl implements HttpClient {

    @Override
    public String get(String url, Map<String, String> headers, Map<String, String> params) {
        HttpURLConnection connection;
        try {
            String urlWithParams = setUrlWithParams(url, params);
            connection = (HttpURLConnection) URI.create(urlWithParams).toURL().openConnection();
            connection.setRequestMethod("GET");
            setHeaders(connection, headers);
            connection.setDoOutput(true);
            connection.disconnect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return readResponse(connection);
    }

    @Override
    public String post(String url, Map<String, String> headers, Map<String, String> data) {
        HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) URI.create(url).toURL().openConnection();
            connection.setRequestMethod("POST");
            setHeaders(connection, headers);
            connection.setDoOutput(true);
            sendJson(connection, convertMapToJsonString(data));
            connection.disconnect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return readResponse(connection);
    }

    @Override
    public String put(String url, Map<String, String> headers, Map<String, String> data) {
        HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) URI.create(url).toURL().openConnection();
            connection.setRequestMethod("PUT");
            setHeaders(connection, headers);
            connection.setDoOutput(true);
            sendJson(connection, convertMapToJsonString(data));
            connection.disconnect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return readResponse(connection);
    }

    @Override
    public String delete(String url, Map<String, String> headers, Map<String, String> data) {
        HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) URI.create(url).toURL().openConnection();
            connection.setRequestMethod("DELETE");
            setHeaders(connection, headers);
            connection.setDoOutput(true);
            sendJson(connection, convertMapToJsonString(data));
            connection.disconnect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return readResponse(connection);
    }

    private static String convertMapToJsonString(Map<String, String> data) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private static void setHeaders(HttpURLConnection connection, Map<String, String> headers) {
        headers.keySet().forEach(key -> connection.setRequestProperty(key, headers.get(key)));
    }

    private static String setUrlWithParams(String url, Map<String, String> params) {
        StringBuilder urlBuilder = new StringBuilder(url);
        urlBuilder.append("?");
        params.keySet().forEach(key -> urlBuilder.append("&").append(key).append("=").append(params.get(key)));
        return urlBuilder.toString();
    }

    private static void sendJson(HttpURLConnection connection, String jsonInput) throws IOException {
        try (OutputStream outputStream = connection.getOutputStream()) {
            byte[] input = jsonInput.getBytes(StandardCharsets.UTF_8);
            outputStream.write(input, 0, input.length);
        }
    }

    private static String readResponse(HttpURLConnection connection) {
        if (connection != null) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder content = new StringBuilder();
                String input;
                while ((input = reader.readLine()) != null) {
                    content.append(input);
                }
                return content.toString();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}
