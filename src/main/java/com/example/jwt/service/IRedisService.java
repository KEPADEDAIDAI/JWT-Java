package com.example.jwt.service;

import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public interface IRedisService {

    void set(String key);

    void set(String key, Long timeout);

    void set(String key, Duration timeout);

    boolean check(String key);

    boolean delete(String key);
}
