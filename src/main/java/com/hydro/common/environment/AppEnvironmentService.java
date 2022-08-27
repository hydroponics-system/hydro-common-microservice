package com.hydro.common.environment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hydro.common.dictionary.enums.Environment;

/**
 * Information about the application environment.
 * 
 * @author Sam Butler
 * @since July 25, 2022
 */
@Service
public class AppEnvironmentService {
    private static final String ACTIVE_PROFILE = "APP_ENVIRONMENT";
    private static final String SIGNING_KEY = "JWT_SIGNING_KEY";

    @Value("${security.signing-key:#{null}}")
    private String LOCAL_SIGNING_KEY;

    /**
     * Gets the current active profile environment.
     *
     * @return string of the environment currently running
     */
    public Environment getEnvironment() {
        String env = System.getenv(ACTIVE_PROFILE);
        return env != null ? Environment.valueOf(env) : Environment.LOCAL;
    }

    /**
     * Gets the signing key for jwt tokens.
     * 
     * @return String of the signing key to use.
     */
    public String getSigningKey() {
        return LOCAL_SIGNING_KEY != null ? LOCAL_SIGNING_KEY : System.getenv(SIGNING_KEY);
    }
}