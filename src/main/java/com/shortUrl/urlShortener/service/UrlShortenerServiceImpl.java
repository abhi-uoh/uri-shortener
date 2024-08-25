package com.shortUrl.urlShortener.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class UrlShortenerServiceImpl implements UrlShortenerService {
    private final ConcurrentHashMap<String, String> concurrentUrlMap = new ConcurrentHashMap<>();
    private final String domain = "http://localhost:8080/shorten/";

    public String shortenUrl(String originalUrl) {
        if (concurrentUrlMap.containsKey(originalUrl)) {
            String existingShortUrl = concurrentUrlMap.get(originalUrl);
            return existingShortUrl;
        } else {
            String shortUrl = generateShortUrl(originalUrl);
            concurrentUrlMap.put(originalUrl, shortUrl);
            return shortUrl;
        }
    }


    private String generateShortUrl(String originalUrl) {
        String shortUrl = domain + Integer.toHexString(originalUrl.hashCode());
        return shortUrl;
    }
}


