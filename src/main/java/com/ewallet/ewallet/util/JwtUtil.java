package com.ewallet.ewallet.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ewallet.ewallet.model.User;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@ConfigurationProperties(prefix = "jwt")
public class JwtUtil {

    private static Algorithm algorithm = Algorithm.none();
    private static JWTVerifier verifier = JWT.require(algorithm).build();
    private static int expire = 60;
    private static String secret;

    public static String generateToken(User user) {
        //convert user to json
        return JWT.create()
//                .withSubject(user.getId())
////                .withClaim("username", user.getUsername())
//                .withClaim("role", user.getRole())
                .withExpiresAt(Date.from(Instant.now().plus(expire, ChronoUnit.MINUTES)))
                .sign(algorithm);

//        return JWT.create()
//                .withExpiresAt(Instant.from(LocalDateTime.now().plusMinutes(expire))).sign(algorithm);
    }


    public static DecodedJWT verifyToken(String token) {
        try {
            return verifier.verify(token);
        } catch (Exception e) {
            return null;
        }
    }

    @PostConstruct
    public void init() {
        algorithm = Algorithm.none();
        verifier = JWT.require(algorithm).build();
    }

}
