package com.beaverbyte.financial_tracker_application.security.jwt;

import java.security.Key;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.beaverbyte.financial_tracker_application.security.UserDetailsImpl;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

  @Value("${JWT_SECRET}")
  private String jwtSecret;

  @Value("${JWT_EXPIRATION_MS}")
  private int jwtExpirationMs;

  // Generates JSON web token from username, date, expiration, secret
  public String generateJwtToken(Authentication authentication) {

    UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

    return Jwts.builder()
        .subject((userPrincipal.getUsername()))
        .issuedAt(new Date())
        .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
        .signWith(key(), SignatureAlgorithm.HS256)
        .compact();
  }
  
  // Key is decoded here
  private Key key() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
  }

  // Provide username from JSON Web Token to allow for User Searching elsewhere
  public String getUserNameFromJwtToken(String token) {
    return Jwts.parser().setSigningKey(key()).build()
               .parseClaimsJws(token).getBody().getSubject();
  }

  /**
   * Parses authToken for validity
   */
  public boolean validateJwtToken(String authToken) {
    try {
      Jwts.parser().setSigningKey(key()).build().parse(authToken);
      return true;
    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      logger.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      logger.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty: {}", e.getMessage());
    }

    return false;
  }
}