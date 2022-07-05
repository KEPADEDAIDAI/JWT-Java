package com.example.jwt.service.impl;

import com.example.jwt.service.IRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements IRedisService {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Override
    public void set(String key) {
        ValueOperations<String,String> ops = stringRedisTemplate.opsForValue();
        ops.set(key,"1");
    }

    @Override
    public void set(String key, Long timeout) {
        ValueOperations<String,String> ops = stringRedisTemplate.opsForValue();
        ops.set(key,"1",timeout, TimeUnit.MINUTES);
    }

    @Override
    public void set(String key, Duration timeout) {
        ValueOperations<String,String> ops = stringRedisTemplate.opsForValue();
        ops.set(key,"1",timeout);
    }

    @Override
    public boolean check(String key) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key));
    }

    @Override
    public boolean delete(String key) {
        stringRedisTemplate.delete(key);
        return !check(key);
    }
}
