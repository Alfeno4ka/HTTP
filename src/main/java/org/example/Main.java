package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static final String REMOTE_SERVICE_URI = "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";

    public static void main(String[] args) {

        try (CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                        .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                        .build())
                .build()) {
            HttpGet request = new HttpGet(REMOTE_SERVICE_URI);
            request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                byte[] responseBody = response.getEntity().getContent().readAllBytes();
                String json = new String(responseBody);
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    List<FactsAboutCats> factsAboutCats = objectMapper.readValue(json, new TypeReference<List<FactsAboutCats>>() {
                    });
                    List<FactsAboutCats> factsAboutCats1 = factsAboutCats.stream().filter(value -> value.getUpvotes() == null).collect(Collectors.toList());
                    for (FactsAboutCats factsAboutCat : factsAboutCats1) {
                        System.out.println("ID: " + factsAboutCat.getId());
                        System.out.println("Text: " + factsAboutCat.getText());
                        System.out.println("Type: " + factsAboutCat.getType());
                        System.out.println("User: " + factsAboutCat.getUser());
                        System.out.println("Upvotes: " + factsAboutCat.getUpvotes());
                        System.out.println();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}