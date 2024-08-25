package com.shortUrl.urlShortener.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UrlShortenerServiceImpl implements UrlShortenerService {
    private final ConcurrentHashMap<String, String> concurrentUrlMap = new ConcurrentHashMap<>();
    @Value("${url.shortener.domain}")
    private String domain;

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

    public String getOriginalUrl(String shortUrl) {
        String fullShortUrl = domain + shortUrl;

        for (Map.Entry<String, String> entry : concurrentUrlMap.entrySet()) {
            if (entry.getValue().equals(fullShortUrl)) {
                return entry.getKey();
            }
        }
        System.out.println("No original URL found for: " + fullShortUrl);
        return null;
    }
}


