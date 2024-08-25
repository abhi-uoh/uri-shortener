package com.shortUrl.urlShortener.controller;

import com.shortUrl.urlShortener.service.UrlShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shorten")
public class UrlShortenerController {

    @Autowired
    private UrlShortenerService urlShortenerService;


    @PostMapping
    public ResponseEntity<String> shortenUrl(@RequestBody String originalUrl) {
        String shortUrl = urlShortenerService.shortenUrl(originalUrl);
        return ResponseEntity.ok(shortUrl);
    }

}





