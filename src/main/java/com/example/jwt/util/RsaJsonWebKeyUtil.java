package com.example.jwt.util;

import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;

import java.util.Random;

class RsaJsonWebKeyUtil {
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
