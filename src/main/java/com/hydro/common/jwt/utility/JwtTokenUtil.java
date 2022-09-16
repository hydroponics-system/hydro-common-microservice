package com.hydro.common.jwt.utility;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hydro.common.dictionary.data.HydroSystem;
import com.hydro.common.dictionary.data.User;
import com.hydro.common.dictionary.enums.WebRole;
import com.hydro.common.environment.AppEnvironmentService;
import com.hydro.common.jwt.domain.HydroJwtClaims;
import com.hydro.common.jwt.domain.JwtType;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Token util to create and manage jwt tokens.
 * 
 * @author Sam Butler
 * @since July 31, 2021
 */
@Service
public class JwtTokenUtil implements Serializable {

    public static final long JWT_TOKEN_USER_VALIDITY = 18000000; // 5 hours
    private static final long JWT_TOKEN_SYSTEM_VALIDITY = 86400000; // 24 hours

    @Autowired
    private AppEnvironmentService appEnvironmentService;

    /**
     * Pulls the expiration date from a given token
     * 
     * @param token - The token being inspected
     * @return A Date object
     */
    public LocalDateTime getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration).toInstant().atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    /**
     * Get Specfic claims from a token based on the passed in resolver
     * 
     * @param <T>            - Object type
     * @param token          - Token to be inspected
     * @param claimsResolver - Claims resolver
     * @return The generic type passed in of the claims
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Pulls all the claims off of a given token
     * 
     * @param token - The token to inspect and pull the claims from
     * @return Claims object is returned
     */
    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(appEnvironmentService.getSigningKey()).parseClaimsJws(token).getBody();
    }

    /**
     * Checks if the given token is expired
     * 
     * @param token - Token to pull the expiration date from
     * @return Returns a boolean object of true, false, or null
     */
    public Boolean isTokenExpired(String token) {
        final LocalDateTime expiration = getExpirationDateFromToken(token);
        return expiration.isBefore(LocalDateTime.now());
    }

    /**
     * generate token for reset password as false by default.
     * 
     * @param user - User info to be added to the token
     * @return String of the new JWT token
     * @throws Exception
     */
    public String generateToken(User user) {
        return generateToken(user, false);
    }

    /**
     * Sets up the fields to be added to the token. Also takes in a reset param that
     * will say if the token is for a reset password.
     * 
     * @param user  User info to be added to the token
     * @param reset If this is a reset password token.
     * @return String of the new JWT token
     * @throws Exception
     */
    public String generateToken(User user, boolean reset) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(HydroJwtClaims.USER_ID, user.getId());
        claims.put(HydroJwtClaims.FIRST_NAME, user.getFirstName());
        claims.put(HydroJwtClaims.LAST_NAME, user.getLastName());
        claims.put(HydroJwtClaims.EMAIL, user.getEmail());
        claims.put(HydroJwtClaims.WEB_ROLE, user.getWebRole());
        claims.put(HydroJwtClaims.ENVIRONMENT, appEnvironmentService.getEnvironment());
        claims.put(HydroJwtClaims.JWT_TYPE, JwtType.WEB);
        claims.put(HydroJwtClaims.PASSWORD_RESET, reset);

        return doGenerateToken(claims, JWT_TOKEN_USER_VALIDITY);
    }

    /**
     * Generate token for a hydro system.
     * 
     * @param system System info to be added to the token
     * @return String of the new JWT token
     * @throws Exception
     */
    public String generateToken(HydroSystem system) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(HydroJwtClaims.ID, system.getId());
        claims.put(HydroJwtClaims.UUID, system.getUuid());
        claims.put(HydroJwtClaims.PART_NUMBER, system.getPartNumber());
        claims.put(HydroJwtClaims.NAME, system.getName());
        claims.put(HydroJwtClaims.OWNER_USER_ID, system.getOwnerUserId());
        claims.put(HydroJwtClaims.ENVIRONMENT, appEnvironmentService.getEnvironment());
        claims.put(HydroJwtClaims.WEB_ROLE, WebRole.SYSTEM);
        claims.put(HydroJwtClaims.JWT_TYPE, JwtType.SYSTEM);

        return doGenerateToken(claims, JWT_TOKEN_SYSTEM_VALIDITY);
    }

    /**
     * Generate a token based on the given Claims and subject
     * 
     * @param claims  - The claims/fields to be added to the token
     * @param subject - The main subject to be added to the field
     * @return String of the generated JWT token
     */
    private String doGenerateToken(Map<String, Object> claims, long validity) {
        return Jwts.builder().setClaims(claims).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + validity))
                .signWith(SignatureAlgorithm.HS512, appEnvironmentService.getSigningKey()).compact();
    }
}
