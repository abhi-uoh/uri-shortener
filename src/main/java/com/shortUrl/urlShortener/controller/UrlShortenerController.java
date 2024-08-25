package com.shortUrl.urlShortener.controller;

import com.shortUrl.urlShortener.service.UrlShortenerService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

import static com.shortUrl.urlShortener.constant.urlConstant.METRICS_LIMIT;

@RestController
@RequestMapping("/shorten/url/")
public class UrlShortenerController {

    @Autowired
    private UrlShortenerService urlShortenerService;


    @PostMapping
    public ResponseEntity<String> shortenUrl(@RequestBody String originalUrl) {
        String shortUrl = urlShortenerService.shortenUrl(originalUrl);
        return ResponseEntity.ok(shortUrl);
    }

    @GetMapping("/redirect/{shortUrl}")
    public void redirect(@PathVariable String shortUrl, HttpServletResponse response) throws IOException {
        String originalUrl = urlShortenerService.getOriginalUrl(shortUrl);
        if (originalUrl != null) {
            response.sendRedirect(originalUrl);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "URL not found");
        }
    }

    @GetMapping("/metrics")
    public ResponseEntity<Map<String, Integer>> getTopDomains() {
        Map<String, Integer> topDomains = urlShortenerService.getTopDomains(METRICS_LIMIT);
        return ResponseEntity.ok(topDomains);
    }

}





