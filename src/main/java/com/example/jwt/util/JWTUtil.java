package com.example.jwt.util;

import lombok.extern.slf4j.Slf4j;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;

import java.util.UUID;

@Slf4j
public class JWTUtil {

    private static RsaJsonWebKey rsaJsonWebKey ;



    public String createJWT(String userId) throws JoseException {
        return createJWT(userId, 30, 15);
    }

    public String createJWT(String userId, long minutes) throws JoseException {
        return createJWT(userId, minutes,minutes/2);
    }

    public static String createJWT(String userId, long minutes, long flashMinutes) throws JoseException {
        rsaJsonWebKey = RsaJsonWebKeyUtil.getInstance();
        // JWT的中间部分
        JwtClaims claims = new JwtClaims();
        // 令牌id
        claims.setJwtId(String.valueOf(UUID.randomUUID()));
        // 令牌是谁发的
        claims.setIssuer("auto");
        // 令牌发给了谁
        claims.setAudience("Audience");
        // 发送时间,当前时间
        claims.setIssuedAtToNow();
        // 发送时间，时间戳
//        claims.setIssuedAt();
        // 在此之前无效
        claims.setNotBeforeMinutesInThePast(0);
        // 结束时间, 开始时间的x分钟内
        claims.setExpirationTimeMinutesInTheFuture(minutes);
        // 结束时间，时间戳
//        claims.setExpirationTime();
        claims.setClaim("uid", userId);
        claims.setClaim("fla", flashMinutes);
        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setKey(rsaJsonWebKey.getPrivateKey());
//        jws.setHeader("typ", "JWT");
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
        String jwt = jws.getCompactSerialization();
        log.info("JWTUtil-createJWT jwt={}",jwt);
        return jwt;
    }

    public static JwtClaims checkJWT(String jwt)  {
        rsaJsonWebKey = RsaJsonWebKeyUtil.getInstance();
        JwtConsumer jwtConsumer = new JwtConsumerBuilder().setRequireExpirationTime()  // JWT必须有一个有效期时间
                .setAllowedClockSkewInSeconds(30) // 允许在验证基于时间的令牌时留有一定的余地，以计算时钟偏差。单位/秒
//                .setRequireSubject() // 主题声明
                .setExpectedIssuer("auto") // JWT需要由谁来发布,用来验证 发布人
                .setExpectedAudience("Audience") // JWT的目的是给谁, 用来验证观众
                .setVerificationKey(rsaJsonWebKey.getPublicKey()) // 用公钥验证签名 ,验证秘钥
                .setJwsAlgorithmConstraints(
                        new AlgorithmConstraints(AlgorithmConstraints.ConstraintType.PERMIT,
                                AlgorithmIdentifiers.RSA_USING_SHA256)
                )
                .build();
        try
        {
            return jwtConsumer.processToClaims(jwt);
        }catch (InvalidJwtException e){
            log.info("InvalidJwtException");
            return null;
        }
    }


}
