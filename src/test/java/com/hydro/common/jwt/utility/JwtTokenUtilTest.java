package com.hydro.common.jwt.utility;

import static com.hydro.common.factory.data.UserFactoryData.userData;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hydro.common.dictionary.enums.Environment;
import com.hydro.common.dictionary.enums.WebRole;
import com.hydro.common.environment.AppEnvironmentService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Test class for the Jwt Token Util.
 * 
 * @author Sam Butler
 * @since August 23, 2022
 */
@ExtendWith(MockitoExtension.class)
public class JwtTokenUtilTest {

    @Mock
    private AppEnvironmentService appEnvironmentService;

    @InjectMocks
    private JwtTokenUtil jwtTokenUtil;

    @Test
    public void testGenerateToken() {
        when(appEnvironmentService.getSigningKey()).thenReturn("test-local-key");
        String token = jwtTokenUtil.generateToken(userData());

        assertNotNull(token, "Token should not be null");
        assertEquals(2, token.chars().filter(c -> c == '.').count(), "Confirm token contains two periods");
    }

    @Test
    public void testGetAllClaimsFromToken() {
        when(appEnvironmentService.getSigningKey()).thenReturn("test-local-key");
        when(appEnvironmentService.getEnvironment()).thenReturn(Environment.LOCAL);

        Claims claims = jwtTokenUtil.getAllClaimsFromToken(jwtTokenUtil.generateToken(userData()));

        assertEquals(12, claims.get("userId"), "User Id");
        assertEquals("Test", claims.get("firstName"), "User Id");
        assertEquals("User", claims.get("lastName"), "User Id");
        assertEquals("test@user.com", claims.get("email"), "User Id");
        assertEquals(WebRole.ADMIN.toString(), claims.get("webRole"), "User Id");
        assertEquals(Environment.LOCAL.toString(), claims.get("env"), "User Id");
        assertEquals(false, claims.get("passwordReset"), "User Id");
    }

    @Test
    public void testIsTokenExpiredValid() {
        when(appEnvironmentService.getSigningKey()).thenReturn("test-local-key");
        String token = jwtTokenUtil.generateToken(userData());
        assertFalse(jwtTokenUtil.isTokenExpired(token), "Token not expired");
    }

    @Test
    public void testIsTokenExpiredInvalid() {
        when(appEnvironmentService.getSigningKey()).thenReturn("test-local-key");
        String expiredToken = Jwts.builder().setClaims(new HashMap<>())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() - 18000000))
                .signWith(SignatureAlgorithm.HS512, "test-local-key").compact();
        assertTrue(jwtTokenUtil.isTokenExpired(expiredToken), "Token expired");
    }

    @Test
    public void testGetExpirationDateFromToken() {
        when(appEnvironmentService.getSigningKey()).thenReturn("test-local-key");
        String token = Jwts.builder().setClaims(new HashMap<>()).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(180000000)).signWith(SignatureAlgorithm.HS512, "test-local-key").compact();
        LocalDateTime tokenDate = jwtTokenUtil.getExpirationDateFromToken(token);

        assertNotNull(tokenDate, "Token Expiration Time");
    }
}
