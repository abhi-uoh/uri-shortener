package com.shortUrl.urlShortener.service;


public interface UrlShortenerService {

    String shortenUrl(String originalUrl);
    String getOriginalUrl(String shortUrl);


}