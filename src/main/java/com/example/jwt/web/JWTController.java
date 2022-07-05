package com.example.jwt.web;

import com.example.jwt.service.IRedisService;
import com.example.jwt.util.JWTUtil;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.NumericDate;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JWTController {

    @Autowired
    private IRedisService redisServiceImpl;

    @GetMapping("/getJWT/{id}")
    public String getJWT(@PathVariable("id") String id) throws JoseException {
        return JWTUtil.createJWT(id, 30);
    }

    @GetMapping("/checkJWT/{jwt}")
    public String checkJWT(@PathVariable("jwt") String jwt) throws MalformedClaimException {
        JwtClaims jwtClaims = JWTUtil.checkJWT(jwt);
        if (jwtClaims == null|| redisServiceImpl.check(jwtClaims.getJwtId())) {
            return "check fail";
        }
        return jwtClaims.toString();
    }

    @GetMapping("/limitJWT/{jwtId}")
    public String LimitJWT(@PathVariable("jwtId") String jwtId){
        redisServiceImpl.set(jwtId,30L);
        return "success";
    }

}
