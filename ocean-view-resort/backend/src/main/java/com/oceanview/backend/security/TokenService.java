package com.oceanview.backend.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {

  @Value("${app.auth.tokenSecret}")
  private String tokenSecret;

  @Value("${app.auth.tokenExpiryMinutes}")
  private int tokenExpiryMinutes;

  public String generateToken(String username) {
    long expiryTime = System.currentTimeMillis() + (tokenExpiryMinutes * 60 * 1000L);
    return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(new Date())
        .setExpiration(new Date(expiryTime))
        .signWith(Keys.hmacShaKeyFor(tokenSecret.getBytes()), SignatureAlgorithm.HS512)
        .compact();
  }

  public String extractUsername(String token) {
    try {
      return Jwts.parserBuilder()
          .setSigningKey(Keys.hmacShaKeyFor(tokenSecret.getBytes()))
          .build()
          .parseClaimsJws(token)
          .getBody()
          .getSubject();
    } catch (Exception e) {
      return null;
    }
  }

  public boolean isTokenValid(String token) {
    try {
      Jwts.parserBuilder()
          .setSigningKey(Keys.hmacShaKeyFor(tokenSecret.getBytes()))
          .build()
          .parseClaimsJws(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
