import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;

import java.util.Random;
import java.util.UUID;

class RsaJsonWebKeyUtil{
    private static volatile RsaJsonWebKey rsaJsonWebKey = null;

    public static RsaJsonWebKey getInstance() {
        if (rsaJsonWebKey == null) {
            synchronized (RsaJsonWebKey.class) {
                if (rsaJsonWebKey == null) {
                    try {
                        rsaJsonWebKey = RsaJwkGenerator.generateJwk(2048);
                        rsaJsonWebKey.setKeyId(String.valueOf(new Random().nextLong()));
                    } catch (Exception e) {
                        return null;
                    }
                }
            }
        }
        return rsaJsonWebKey;
    }
}
public class JWTUtil {

    private static final RsaJsonWebKey rsaJsonWebKey = RsaJsonWebKeyUtil.getInstance();


    public static String createJWT(long userId) throws JoseException {
        return createJWT(userId, 30);
    }

    public static String createJWT(long userId, long minutes) throws JoseException {
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
        claims.setClaim("userId", String.valueOf(userId));
        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setKey(rsaJsonWebKey.getPrivateKey());
        jws.setKeyIdHeaderValue(rsaJsonWebKey.getKeyId());

        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
        String jwt = jws.getCompactSerialization();
        System.out.println(jwt);
        return jwt;
    }

    public static String checkJWT(String jwt)  {
        JwtConsumer jwtConsumer = new JwtConsumerBuilder().setRequireExpirationTime()  // JWT必须有一个有效期时间
                .setAllowedClockSkewInSeconds(30) // 允许在验证基于时间的令牌时留有一定的余地，以计算时钟偏差。单位/秒
//                .setRequireSubject() // 主题声明
                .setExpectedIssuer("auto") // JWT需要由谁来发布,用来验证 发布人
                .setExpectedAudience("Audience") // JWT的目的是给谁, 用来验证观众
                .setVerificationKey(rsaJsonWebKey.getKey()) // 用公钥验证签名 ,验证秘钥
                .setJwsAlgorithmConstraints(
                        new AlgorithmConstraints(AlgorithmConstraints.ConstraintType.PERMIT,
                                AlgorithmIdentifiers.RSA_USING_SHA256)
                )
                .build();
        try
        {
            JwtClaims jwtClaims = jwtConsumer.processToClaims(jwt);
            System.out.println("success!");
            return jwtClaims.getClaimValueAsString("userId");
        }catch (InvalidJwtException e){
            System.out.println("InvalidJwtException");
            return null;
        }
    }


}
