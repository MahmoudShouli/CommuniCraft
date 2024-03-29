package com.aswe.communicraft.security;

import com.aswe.communicraft.models.entities.UserEntity;

import com.aswe.communicraft.repositories.UserRepository;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.util.Base64;
import java.util.Date;
import java.util.Optional;


@Component
public class JwtUtils implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtils.class);


    @Value("${communicraft.app.jwtSecret}")
    private String jwtSecret;
    @Value("${communicraft.app.jwtExpirationMs}")
    private int jwtExpirationMs;
    private UserRepository userRepository;
    @Autowired
    public void setRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Generate jwt token from user information
     */
    public String generateTokenFromUserDetails(UserDetailsImpl userDetails) {
        LOGGER.debug("generateTokenFromUserDetails :: Generating token from user details: " + userDetails);
        Claims claims = Jwts.claims().setSubject(userDetails.getUsername());
        claims.put("id", userDetails.getId());
        claims.put("email", userDetails.getEmail());
        claims.put("craft", userDetails.getCraft());
        claims.put("role", userDetails.getRole());



        byte[] secretKeyBytes = Base64.getDecoder().decode(jwtSecret);
        SecretKey secretKey = new SecretKeySpec(secretKeyBytes, "HmacSHA256");



        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extract username from jwt token subject based on the jwt secret
     */
    public String getUserNameFromJwtToken(String token) {
        LOGGER.debug("getUserNameFromJwtToken :: Getting username from token..");
        token = token.substring(7);
        return Jwts.parserBuilder().setSigningKey(Base64.getDecoder().decode(jwtSecret)).build().parseClaimsJws(token.split("\\|")[0]).getBody().getSubject();
    }

    /**
     * Extract userId from jwt token claims based on the jwt secret
     */
    public Integer getIdFromJwtToken(String token) {
        LOGGER.debug("getIdFromJwtToken :: Getting id from token");
        token = token.substring(7);
        return Jwts.parserBuilder().setSigningKey(Base64.getDecoder().decode(jwtSecret)).build().parseClaimsJws(token.split("\\|")[0]).getBody().get("id", Integer.class);
    }
    /**
     * Extract userRole from jwt token claims based on the jwt secret
     */
    public String getRoleFromJwtToken(String token) {
        LOGGER.debug("getRoleFromJwtToken :: Getting role from token");
        token = token.substring(7);
        return Jwts.parserBuilder().setSigningKey(Base64.getDecoder().decode(jwtSecret)).build().parseClaimsJws(token.split("\\|")[0]).getBody().get("role", String.class);
    }




    public String getCraftFromJwtToken(String token) {
        LOGGER.debug("getCraftFromJwtToken :: Getting craft from token");
        token = token.substring(7);
        return Jwts.parserBuilder().setSigningKey(Base64.getDecoder().decode(jwtSecret)).build().parseClaimsJws(token.split("\\|")[0]).getBody().get("craft", String.class);
    }

    public boolean validateJwtToken(String authToken) {
        try {
            authToken = authToken.substring(7);
            Jwts.parserBuilder().setSigningKey(Base64.getDecoder().decode(jwtSecret)).build().parseClaimsJws(authToken);
            LOGGER.info("validateJwtToken :: Token validated");
            return true;
        } catch (MalformedJwtException e) {
            LOGGER.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            LOGGER.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            LOGGER.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }






    /**
     *  Load the user from repository and convert to UserDetails data
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> users = userRepository.findByUserName(username);

        if (users.isEmpty())
            return null;

        UserEntity userEntity = users.get();

        return UserDetailsImpl.build(userEntity);
    }
}
