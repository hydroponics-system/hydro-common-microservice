package com.hydro.common.jwt.utility;

import static com.hydro.common.factory.data.HydroSystemFactoryData.*;
import static com.hydro.common.factory.data.UserFactoryData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hydro.common.dictionary.data.User;
import com.hydro.common.dictionary.enums.WebRole;
import com.hydro.common.environment.AppEnvironmentService;;

/**
 * Test Class for the JwtHolder.
 * 
 * @author Sam Butler
 * @since August 23, 2022
 */
@ExtendWith(MockitoExtension.class)
public class JwtHolderTest {

    @Mock
    private AppEnvironmentService appEnvironmentService;

    @InjectMocks
    private JwtHolder jwtHolder;

    @InjectMocks
    private JwtTokenUtil tokenUtil;

    @BeforeEach
    public void setup() {
        when(appEnvironmentService.getSigningKey()).thenReturn("test-local");
        jwtHolder.setToken(tokenUtil.generateToken(userData()));
    }

    @AfterEach
    public void tearDown() {
        jwtHolder.clearToken();
    }

    @Test
    public void testGetDataWithMultiThreads() throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(3);

        for(User userData : getUserList()) {
            service.execute(() -> {
                try {
                    multiThreadTest(userData);
                }
                catch(InterruptedException e) {}
                latch.countDown();
            });
        }
        latch.await();

        // Confirm 8 times. The JwtPair and JwtTokenUtil call it both 4 times each
        verify(appEnvironmentService, times(8)).getSigningKey();
    }

    @Test
    public void testGetUserId() {
        assertEquals(12, jwtHolder.getUserId());
    }

    @Test
    public void testGetUserEmail() {
        assertEquals("test@user.com", jwtHolder.getEmail());
    }

    @Test
    public void testGetUserResetPassword() {
        assertEquals(false, jwtHolder.getResetPassword());
    }

    @Test
    public void testGetUserWebRole() {
        assertEquals(WebRole.ADMIN, jwtHolder.getWebRole());
    }

    @Test
    public void testParseTokenERROR() {
        assertNull(jwtHolder.parse("INVALID"), "Returned data should be null");
    }

    @Test
    public void testParseUserOnSystemTokenException() {
        jwtHolder.setToken(tokenUtil.generateToken(hydroSystem()));

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> jwtHolder.getUser());
        assertEquals("Jwt is not of type User!", e.getMessage(), "Exception Message");
    }

    @Test
    public void testParseSystemOnUserTokenException() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> jwtHolder.getSystem());
        assertEquals("Jwt is not of type Hydro System!", e.getMessage(), "Exception Message");
    }

    private synchronized void multiThreadTest(User u) throws InterruptedException {
        jwtHolder.setToken(tokenUtil.generateToken(u));
        wait(100);
        assertEquals(u.getId(), jwtHolder.getUserId(), "User Id");

        wait(100);
        assertEquals("test@user.com", jwtHolder.getEmail());

        wait(100);
        assertEquals(WebRole.ADMIN, jwtHolder.getWebRole());

        jwtHolder.clearToken();
    }

    private List<User> getUserList() {
        User user1 = userData();
        user1.setId(1);
        User user2 = userData();
        user2.setId(2);
        User user3 = userData();
        user3.setId(3);
        return Arrays.asList(user1, user2, user3);
    }
}
