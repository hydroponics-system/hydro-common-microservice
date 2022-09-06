package com.hydro.common.jwt.domain;

/**
 * Global jwt claim fields.
 * 
 * @author Sam Butler
 * @since August 22,2022
 */
public abstract class HydroJwtClaims {
    // User JWT Claims
    public static final String USER_ID = "userId";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String EMAIL = "email";
    public static final String WEB_ROLE = "webRole";
    public static final String ENVIRONMENT = "env";
    public static final String PASSWORD_RESET = "passwordReset";

    // System JWT Claims
    public static final String ID = "id";
    public static final String UUID = "uuid";
    public static final String PART_NUMBER = "partNumber";
    public static final String NAME = "name";
    public static final String OWNER_USER_ID = "ownerUserId";

    // All Claims
    public static final String JWT_TYPE = "jwtType";
}
