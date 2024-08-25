package com.shortUrl.urlShortener;

import com.shortUrl.urlShortener.controller.UrlShortenerController;
import com.shortUrl.urlShortener.service.UrlShortenerService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class UrlShortenerApplicationTests {

    @Mock
    private UrlShortenerService urlShortenerService;

    @InjectMocks
    private UrlShortenerController urlShortenerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testShortenUrl() {
        String originalUrl = "https://www.flipkart.com/mobile-phones-store";
        String shortUrl = "http://localhost:8080/shorten/url/62689973";

        when(urlShortenerService.shortenUrl(originalUrl)).thenReturn(shortUrl);

        ResponseEntity<String> response = urlShortenerController.shortenUrl(originalUrl);

        assertEquals(HttpStatus.OK, HttpStatus.valueOf(response.getStatusCode().value()));
        assertEquals(shortUrl, response.getBody());
    }


    @Test
    void testRedirect() throws IOException {
        String shortUrl = "62689973";
        String originalUrl = "https://www.flipkart.com/mobile-phones-store";

        HttpServletResponse response = mock(HttpServletResponse.class);

        when(urlShortenerService.getOriginalUrl(shortUrl)).thenReturn(originalUrl);

        urlShortenerController.redirect(shortUrl, response);

        verify(response).sendRedirect(originalUrl);
    }

    @Test
    void testRedirectNotFound() throws IOException {
        String shortUrl = "62689973";

        HttpServletResponse response = mock(HttpServletResponse.class);

        when(urlShortenerService.getOriginalUrl(shortUrl)).thenReturn(null);

        urlShortenerController.redirect(shortUrl, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND, "URL not found");
    }


    @Test
    void testGetTopDomains() {
        Map<String, Integer> topDomains = Map.of("https://www.flipkart.com", 9, "www.linkedin.com", 5, "www.jocata.com", 4);

        when(urlShortenerService.getTopDomains(3)).thenReturn(topDomains);

        ResponseEntity<Map<String, Integer>> response = urlShortenerController.getTopDomains();

        assertEquals(HttpStatus.OK, HttpStatus.valueOf(response.getStatusCode().value()));
        assertEquals(topDomains, response.getBody());
    }

}
