# URL Shortener Spring Boot Project

## Introduction
This project is a simple URL shortener service that will accept a URL as an argument over a REST API and return a shortened URL as a result.The project also includes a metrics API to track the most frequently shortened domains.

## Features
- Accepts a URL and returns a shortened version.
- Redirects the user to the original URL when accessing the shortened URL.
- Provides the top 3 most frequently shortened domains.
- Dockerized application for easy deployment(optional).

## Technologies Used
- **Java 17**
- **Spring Boot 3.3.3**
- **Maven**