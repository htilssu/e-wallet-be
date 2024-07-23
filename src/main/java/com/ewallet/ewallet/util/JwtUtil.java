package com.ewallet.ewallet.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ewallet.ewallet.partner.Partner;
import com.ewallet.ewallet.user.User;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@ConfigurationProperties(prefix = "jwt")
public class JwtUtil {

    private static final int expire = 60 * 24;
    private static Algorithm algorithm = Algorithm.none();
    private static JWTVerifier verifier = JWT.require(algorithm)
                                             .build();
    private static String secret;

    public static String generateToken(User user) {
        //convert user to json
        return JWT.create()
                  .withSubject(user.getId())
                  .withPayload(ObjectUtil.parseJson(user))
                  .withExpiresAt(Date.from(Instant.now()
                                                  .plus(expire, ChronoUnit.MINUTES)))
                  .sign(algorithm);

    }

    public static DecodedJWT verifyToken(String token) {
        try {
            return verifier.verify(token);
        } catch (Exception e) {
            return null;
        }
    }

    public static String generateToken(Partner newPartner) {
        newPartner.setPassword(null);

        return JWT.create()
                  .withSubject(newPartner.getId())
                  .withPayload(ObjectUtil.parseJson(newPartner))
                  .withExpiresAt(Date.from(Instant.now()
                                                  .plus(expire, ChronoUnit.MINUTES)))
                  .sign(algorithm);
    }

    @PostConstruct
    public void init() {
        algorithm = Algorithm.none();
        verifier = JWT.require(algorithm)
                      .build();
    }

}
