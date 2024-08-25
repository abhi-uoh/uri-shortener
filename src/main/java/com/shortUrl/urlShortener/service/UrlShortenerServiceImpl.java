package com.shortUrl.urlShortener.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UrlShortenerServiceImpl implements UrlShortenerService {
    private final ConcurrentHashMap<String, String> concurrentUrlMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Integer> countDomainMap = new ConcurrentHashMap<>();

    @Value("${url.shortener.domain}")
    private String domain;

    @Override
    public String shortenUrl(String originalUrl) {
        if (concurrentUrlMap.containsKey(originalUrl)) {
            return concurrentUrlMap.get(originalUrl);
        } else {
            String shortUrl = generateShortUrl(originalUrl);
            concurrentUrlMap.put(originalUrl, shortUrl);
            // Update the domain count
            updateDomainCount(originalUrl);
            return shortUrl;
        }
    }

    @Override
    public String getOriginalUrl(String shortUrl) {
        for (Map.Entry<String, String> entry : concurrentUrlMap.entrySet()) {
            if (entry.getValue().equals(domain + shortUrl)) {
                return entry.getKey();
            }
        }
        System.out.println("No original URL found for: " + shortUrl);
        return null;
    }

    private String generateShortUrl(String originalUrl) {
        return domain + Integer.toHexString(originalUrl.hashCode());
    }

    private void updateDomainCount(String originalUrl) {
        String domainName = extractDomainName(originalUrl);
        if (domainName != null) {
            countDomainMap.merge(domainName, 1, Integer::sum);
        }
    }

    private String extractDomainName(String url) {
        try {
            URI uri = new URI(url);
            return uri.getHost();
        } catch (URISyntaxException e) {
            return null;
        }
    }

    @Override
    public Map<String, Integer> getTopDomains(int limit) {
        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(countDomainMap.entrySet());

        entryList.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        Map<String, Integer> topDomains = new LinkedHashMap<>();

        for (int i = 0; i < Math.min(limit, entryList.size()); i++) {
            Map.Entry<String, Integer> entry = entryList.get(i);
            topDomains.put(entry.getKey(), entry.getValue());
        }

        return topDomains;
    }

}
