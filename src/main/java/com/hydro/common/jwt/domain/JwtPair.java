package com.hydro.common.jwt.domain;

import com.hydro.common.environment.AppEnvironmentService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

/**
 * Jwt Pair class for storing the token and claims.
 * 
 * @author Sam Butler
 * @since August 22, 2022
 */
public final class JwtPair {
    private final String token;

    private final Claims claimSet;

    public JwtPair(String token, AppEnvironmentService appEnvironmentService) {
        this.token = token;
        this.claimSet = (Claims) Jwts.parser().setSigningKey(appEnvironmentService.getSigningKey()).parse(token)
                .getBody();
    }

    public String getToken() {
        return token;
    }

    public Claims getClaimSet() {
        return claimSet;
    }
}
