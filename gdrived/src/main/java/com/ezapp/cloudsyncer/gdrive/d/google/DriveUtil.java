package com.ezapp.cloudsyncer.gdrive.d.google;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import org.apache.logging.log4j.Logger;

import com.ezapp.cloudsyncer.gdrive.d.Main;
import com.ezapp.cloudsyncer.gdrive.d.exceptions.AppGDriveException;
import com.ezapp.cloudsyncer.gdrive.d.log.LogManager;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

/**
 * Utility to build up Drive
 * 
 * Date: May 28th, 2015
 * 
 * @author grangarajan
 *
 */
public class DriveUtil {

	/**
	 * Logger
	 */
	private static final Logger LOGGER = LogManager.getLogger(DriveUtil.class);

	/**
	 * Google App credentials
	 */
	private static final String CLIENT_ID = "157725393237-ld0tohu1nqd93i0eguqu39k093p615ac.apps.googleusercontent.com";
	private static final String CLIENT_SECRET = "Kjon8HZlmkUR5xBG3hb4q1Sn";
	private static final String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";
	private static final String APPLICATION_NAME = "gdrived";

	private HttpTransport httpTransport = new NetHttpTransport();
	private JsonFactory jsonFactory = new JacksonFactory();

	/**
	 * Authorization flow
	 */
	private GoogleAuthorizationCodeFlow flow;

	/**
	 * Credential
	 */
	private Credential accountCredential;

	/**
	 * Drive
	 */
	private static Drive drive;

	public Drive getDrive() {
		if (null == drive) {
			if (null == accountCredential) {
				LOGGER.error("Credential is null!! Add account first!!");
				Main.showErrorMessage("Add account first!!");
			}
			Drive.Builder builder = new Drive.Builder(httpTransport,
					jsonFactory, accountCredential);
			builder.setApplicationName(APPLICATION_NAME);
			drive = builder.build();
			builder = null;
		}
		return drive;
	}

	/**
	 * Get URL for authentication
	 * 
	 * @return authentication URL
	 */
	public String getOAuthHttpURL() {

		flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport,
				jsonFactory, CLIENT_ID, CLIENT_SECRET,
				Collections.singleton(DriveScopes.DRIVE)).setAccessType("offline")
				.setApprovalPrompt("force").build();

		String url = flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI)
				.build();

		return url;
	}

	/**
	 * 
	 * @param userId
	 * @throws AppGDriveException
	 */
	public void reOAauth(String userId) throws AppGDriveException {
		try {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Start reAuth");
			}
			if (null == flow) {
				getOAuthHttpURL();
			}
			accountCredential = flow.loadCredential(userId);
		} catch (IOException e) {
			LOGGER.error(
					"Exception while generating credentials! " + e.getMessage(),
					e);
			throw new AppGDriveException("Invalid authentication token");
		} catch (Exception e) {
			LOGGER.error(
					"Exception while generating credentials! " + e.getMessage(),
					e);
			throw new AppGDriveException(e);
		}
	}

	/**
	 * Builds google credentials
	 * 
	 * @param authorizationCode
	 * @param userId
	 * @throws AppGDriveException
	 */
	public void buildCredentials(String authorizationCode, String userId)
			throws AppGDriveException {
		GoogleTokenResponse tokenResponse;
		try {
			if (null == flow) {
				getOAuthHttpURL();
			}
			tokenResponse = flow.newTokenRequest(authorizationCode)
					.setRedirectUri(REDIRECT_URI).execute();
			accountCredential = flow.createAndStoreCredential(tokenResponse,
					userId);

		} catch (IOException e) {
			LOGGER.error(
					"Exception while generating credentials! " + e.getMessage(),
					e);
			throw new AppGDriveException("Invalid authentication token");
		} finally {
			authorizationCode = null;
			tokenResponse = null;
		}
	}

	/**
	 * Cleans up object and close
	 */
	public void closeCleanUp() {
		drive = null;
		accountCredential = null;
		flow = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		closeCleanUp();
		super.finalize();
	}
}
