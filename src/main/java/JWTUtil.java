import com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithm;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.lang.JoseException;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
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

    public static RsaJsonWebKey rsaJsonWebKey = RsaJsonWebKeyUtil.getInstance();


    public static String createJWT(long UserId) throws JoseException {
        return createJWT(UserId, 30);
    }

    public static String createJWT(long UserId, long minutes) throws JoseException {
        // JWT的中间部分
        JwtClaims claims = new JwtClaims();
        // 令牌id
        claims.setJwtId(String.valueOf(UUID.randomUUID()));
        // 令牌是谁发的
        claims.setIssuer("auto");
        // 令牌发给了谁
        claims.setAudience(String.valueOf(UserId));
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

        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setKey(rsaJsonWebKey.getKey());
        jws.setKeyIdHeaderValue(rsaJsonWebKey.getKeyId());

        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_PSS_USING_SHA256);
        String jwt = jws.getCompactSerialization();
        System.out.println(jwt);
        return jwt;
    }



}
