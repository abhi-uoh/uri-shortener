package com.shortUrl.urlShortener.service.Impl;

import com.shortUrl.urlShortener.service.UrlShortenerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.shortUrl.urlShortener.utils.URLShortenerLogger.*;

@Service
public class UrlShortenerServiceImpl implements UrlShortenerService {
    private final ConcurrentHashMap<String, String> concurrentUrlMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Integer> countDomainMap = new ConcurrentHashMap<>();

    @Value("${url.shortener.domain}")
    private String domain;


    //method to short the url
    @Override
    public String shortenUrl(String originalUrl) {
        if (originalUrl == null || originalUrl.isEmpty()) {
            ERROR_LOGGER.error("originalUrl is null or empty");
            throw new IllegalArgumentException("URL cannot be null or empty");
        }
        DEBUG_LOGGER.debug("Received originalUrl: {}", originalUrl);

        if (concurrentUrlMap.containsKey(originalUrl)) {
            INFO_LOGGER.info("URL {} is already shortened. Returning same shortened URL.", originalUrl);
            return concurrentUrlMap.get(originalUrl);
        } else {
            String shortUrl = generateShortUrl(originalUrl);
            concurrentUrlMap.put(originalUrl, shortUrl);
            INFO_LOGGER.info("URL {} has been shortened to {}", originalUrl, shortUrl);

            // Update the domain count
            updateDomainCount(originalUrl);
            DEBUG_LOGGER.debug("Domain count updated for URL: {}", originalUrl);

            return shortUrl;
        }
    }


    //method to redirect to original url
    @Override
    public String getOriginalUrl(String shortUrl) {
        if (shortUrl == null || shortUrl.isEmpty()) {
            ERROR_LOGGER.error("Short URL cannot be null or empty.");
            throw new IllegalArgumentException("Short URL cannot be null or empty");
        }

        DEBUG_LOGGER.debug("Received request to redirect to original URL for short URL: {}", shortUrl);

        for (Map.Entry<String, String> entry : concurrentUrlMap.entrySet()) {
            if (entry.getValue().equals(domain + shortUrl)) {
                INFO_LOGGER.info("Original URL found for short URL: {}", shortUrl);
                return entry.getKey();
            }
        }

        INFO_LOGGER.info("No original URL found for short URL: {}", shortUrl);
        return null;
    }


    private String generateShortUrl(String originalUrl) {

        String shortUrl = domain + Integer.toHexString(originalUrl.hashCode());
        DEBUG_LOGGER.debug("Generated short URL: {} for original URL: {}", shortUrl, originalUrl);

        return shortUrl;
    }


    private void updateDomainCount(String originalUrl) {
        DEBUG_LOGGER.debug("Updating domain count for original URL: {}", originalUrl);

        String domainName = extractDomainName(originalUrl);
        if (domainName != null) {
            countDomainMap.merge(domainName, 1, Integer::sum);
            INFO_LOGGER.info("Domain count updated for domain: {}", domainName);
        } else {
            DEBUG_LOGGER.debug("No domain name extracted from URL: {}", originalUrl);
        }
    }


    private String extractDomainName(String url) {
        try {
            URI uri = new URI(url);
            String host = uri.getHost();
            DEBUG_LOGGER.debug("Extracted domain name: {} from URL:{}", host, url);
            return host;
        } catch (URISyntaxException e) {
            ERROR_LOGGER.error("Failed to extract domain name from URL: {} {}", url, e);
            return null;
        }
    }

    //method to get top domains
    @Override
    public Map<String, Integer> getTopDomains(int limit) {
        INFO_LOGGER.info("Received request to get top domains");

        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(countDomainMap.entrySet());
        entryList.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        DEBUG_LOGGER.debug("Sorted domain entries by count.");

        Map<String, Integer> topDomains = new LinkedHashMap<>();

        for (int i = 0; i < Math.min(limit, entryList.size()); i++) {
            Map.Entry<String, Integer> entry = entryList.get(i);
            topDomains.put(entry.getKey(), entry.getValue());
            DEBUG_LOGGER.debug("Added domain: {} with count: {} to top domains list.", entry.getKey(), entry.getValue());
        }

        INFO_LOGGER.info("getTopDomains method ends ");
        return topDomains;
    }


}
