package com.shorturlapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shorturlapp.model.ShortUrl;



public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {
    Optional<ShortUrl> findByShortCode(String shortCode);
    boolean existsByShortCode(String shortCode);
}