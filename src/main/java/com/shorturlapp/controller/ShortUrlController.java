package com.shorturlapp.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.shorturlapp.service.ShortUrlService;

@RestController
@RequestMapping("/api")
public class ShortUrlController {
    private final ShortUrlService service;

    public ShortUrlController(ShortUrlService service) {
        this.service = service;
    }

    @PostMapping("/shorten")
    public ResponseEntity<String> shortenUrl(@RequestBody Map<String, String> request) {
    String originalUrl = request.get("url");
        try {
            String shortCode = service.shortenUrl(originalUrl).getShortCode();
            return ResponseEntity.ok("http://localhost:8080/api/" + shortCode);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {
        try {
            String originalUrl = service.getOriginalUrl(shortCode);
            service.incrementClicks(shortCode);
            return ResponseEntity.status(HttpStatus.FOUND).header("Location", originalUrl).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}