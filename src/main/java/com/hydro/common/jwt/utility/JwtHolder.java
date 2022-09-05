package com.hydro.common.jwt.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.hydro.common.dictionary.data.HydroSystem;
import com.hydro.common.dictionary.data.PartNumber;
import com.hydro.common.dictionary.data.User;
import com.hydro.common.dictionary.enums.Environment;
import com.hydro.common.dictionary.enums.WebRole;
import com.hydro.common.environment.AppEnvironmentService;
import com.hydro.common.jwt.domain.HydroJwtClaims;
import com.hydro.common.jwt.domain.JwtPair;
import com.hydro.common.jwt.domain.JwtType;

import io.jsonwebtoken.Claims;

/**
 * JwtHolder class to store authentication token in a thread local instance to
 * be accessed. Although the JWT is held in a static thread local, the methods
 * are non-static so that JwtHolder can be mocked in tests.
 * 
 * @author Sam Butler
 * @since August 8, 2020
 */
@Service
public class JwtHolder {
	private static final ThreadLocal<JwtPair> TOKEN = new ThreadLocal<>();

	@Autowired
	private AppEnvironmentService appEnvironmentService;

	/**
	 * Set the token on the current thread local instance.
	 * 
	 * @param token The token to store.
	 */
	public void setToken(String token) {
		JwtPair pair = new JwtPair(token, appEnvironmentService);
		TOKEN.set(pair);
	}

	/**
	 * Clears the token from the current thread local instance.
	 */
	public void clearToken() {
		TOKEN.remove();
	}

	/**
	 * Gets the current JwtPair from the thread local instance.
	 * 
	 * @return {@link JwtPair} of the thread local.
	 */
	public JwtPair getPair() {
		return TOKEN.get();
	}

	/**
	 * Gets the claims from the token.
	 * 
	 * @return {@link Claims} object.
	 */
	public Claims getClaims() {
		return getPair().getClaimSet();
	}

	/**
	 * Parse the claims from the given token and for the given key value pair.
	 * 
	 * @param key The key to find.
	 * @return {@link Object} of the found key.
	 */
	public Object parse(String key) {
		return getClaims().get(key);
	}

	/**
	 * Checks to see if the token is stored locally on the current instance. It will
	 * check if the jwt pair is null to confirm this.
	 * 
	 * @return Boolean of the token status.
	 */
	public boolean isTokenAvaiable() {
		JwtPair pair = TOKEN.get();
		return pair != null;
	}

	/**
	 * Will get the environment from the instance token.
	 * 
	 * @return {@link Environment} object.
	 */
	public Environment getEnvironment() {
		return Environment.valueOf(parse(HydroJwtClaims.ENVIRONMENT).toString());
	}

	/**
	 * Gets the jwt type of the token.
	 * 
	 * @return {@link JwtType} of the token.
	 */
	public JwtType getJwtType() {
		return JwtType.valueOf(parse(HydroJwtClaims.JWT_TYPE).toString());
	}

	/**
	 * Get the current user Id.
	 * 
	 * @return int of the userId from the current token
	 */
	public int getUserId() {
		return Integer.parseInt(parse(HydroJwtClaims.USER_ID).toString());
	}

	/**
	 * Get the current email.
	 * 
	 * @return String of the email from the current token
	 */
	public String getEmail() {
		return parse(HydroJwtClaims.EMAIL).toString();
	}

	/**
	 * Get the current webRole.
	 * 
	 * @return String of the webRole from the current token
	 */
	public WebRole getWebRole() {
		return WebRole.valueOf(parse(HydroJwtClaims.WEB_ROLE).toString());
	}

	/**
	 * Gets the reset password status.
	 * 
	 * @return int of the userId from the current token
	 */
	public boolean getResetPassword() {
		return Boolean.parseBoolean(parse(HydroJwtClaims.PASSWORD_RESET).toString());
	}

	/**
	 * Will get a user object from the current user jwt. It will only peform this
	 * action if the jwt token is a user web authenticated token.
	 * 
	 * @return {@link User} object.
	 */
	public User getUser() {
		Assert.isTrue(getJwtType().equals(JwtType.WEB), "Jwt is not of type User!");

		User currentUser = new User();
		currentUser.setId(getUserId());
		currentUser.setEmail(getEmail());
		currentUser.setWebRole(getWebRole());
		currentUser.setFirstName(parse(HydroJwtClaims.FIRST_NAME).toString());
		currentUser.setLastName(parse(HydroJwtClaims.LAST_NAME).toString());
		return currentUser;
	}

	/**
	 * Will get a Hydro System object from the current system jwt. It will only
	 * peform this action if the jwt token is a system authenticated token.
	 * 
	 * @return {@link HydroSystem} object.
	 */
	public HydroSystem getSystem() {
		Assert.isTrue(getJwtType().equals(JwtType.SYSTEM), "Jwt is not of type Hydro System!");

		HydroSystem currentSystem = new HydroSystem();
		currentSystem.setId(Integer.parseInt(parse(HydroJwtClaims.ID).toString()));
		currentSystem.setUuid(parse(HydroJwtClaims.UUID).toString());
		currentSystem.setPartNumber(new PartNumber(parse(HydroJwtClaims.PART_NUMBER).toString()));
		currentSystem.setName(parse(HydroJwtClaims.NAME).toString());
		return currentSystem;
	}
}
