package com.shorturlapp.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.stereotype.Service;

import com.shorturlapp.model.ShortUrl;
import com.shorturlapp.repository.ShortUrlRepository;

@Service
public class ShortUrlService {
    private final ShortUrlRepository repository;
    private final UrlValidator urlValidator = new UrlValidator(new String[]{"http", "https"});

    public ShortUrlService(ShortUrlRepository repository) {
        this.repository = repository;
    }

    public ShortUrl shortenUrl(String originalUrl) {
        // Validar URL
        if (!urlValidator.isValid(originalUrl)) {
            throw new IllegalArgumentException("URL no válida");
        }

        // Generar código corto único
        String shortCode;
        do {
            shortCode = RandomStringUtils.randomAlphanumeric(6);
        } while (repository.existsByShortCode(shortCode));

        // Guardar en la base de datos
        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setOriginalUrl(originalUrl);
        shortUrl.setShortCode(shortCode);
        return repository.save(shortUrl);
    }

    public String getOriginalUrl(String shortCode) {
        return repository.findByShortCode(shortCode)
                .map(ShortUrl::getOriginalUrl)
                .orElseThrow(() -> new IllegalArgumentException("Código no encontrado"));
    }

    public void incrementClicks(String shortCode) {
        ShortUrl shortUrl = repository.findByShortCode(shortCode)
                .orElseThrow(() -> new IllegalArgumentException("Código no encontrado"));
        shortUrl.setClicks(shortUrl.getClicks() + 1);
        repository.save(shortUrl);
    }
}