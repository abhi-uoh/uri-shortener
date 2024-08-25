package com.shortUrl.urlShortener.service;


import java.util.Map;

public interface UrlShortenerService {

    String shortenUrl(String originalUrl);
    String getOriginalUrl(String shortUrl);
    Map<String, Integer> getTopDomains(int limit);


}