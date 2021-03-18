package com.caddie.voice.y1.handler;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {

    private final String secretKey;

    private long accessExpireTime = (60 * 60 * 1000L) * 48;
    private long refreshExpireTime =  ((60 * 60 * 1000L) * 24) * 60;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        this.secretKey = secretKey;
    }

    public String createToken(String id) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + accessExpireTime);
        return Jwts.builder()
                .setSubject(""+id)
                .setClaims(createClaims(id))
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String createRefreshToken(String id) {

        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshExpireTime);

        return Jwts.builder()
                .setSubject(""+id)
                .setClaims(createClaims(id))
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    private static Map<String, Object> createClaims(String id) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", id);
        //claims.put("pwd", pwd);
        return claims;
    }

    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public String getUserId(String token) {
        Claims claims =  Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();

        return "" + claims.get("id");
    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("token");
    }

}
