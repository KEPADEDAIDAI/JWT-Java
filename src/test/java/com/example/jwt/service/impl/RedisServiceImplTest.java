package com.example.jwt.service.impl;

import com.example.jwt.service.IRedisService;
import com.example.jwt.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.lang.JoseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class RedisServiceImplTest {

    @Autowired
    private IRedisService redisServiceImpl;

    @Test
    void set() throws JoseException, MalformedClaimException {

        String jwt = JWTUtil.createJWT("123456", 30);
        System.out.println(jwt);
        JwtClaims jwtClaims = JWTUtil.checkJWT(jwt);
        assert jwtClaims != null;
        String jwtId = jwtClaims.getJwtId();
        boolean hasJwtId = redisServiceImpl.check(jwtId);
        log.info("first check jwtId, {}", hasJwtId);
        redisServiceImpl.set(jwtId, 30L);
        boolean hasJwtId2 = redisServiceImpl.check(jwtId);
        log.info("second check jwtId,{}", hasJwtId2);
    }

}