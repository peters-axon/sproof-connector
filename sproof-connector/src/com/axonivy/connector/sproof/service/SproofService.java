package com.axonivy.connector.sproof.service;

import java.util.Base64;

import com.sproof.sign.api.v1.client.CreateSignatureRequest;
import com.sproof.sign.api.v1.client.Document;
import com.sproof.sign.api.v1.client.EnvelopeData;
import com.sproof.sign.api.v1.client.InviteData;
import com.sproof.sign.api.v1.client.Sender;

import ch.ivyteam.ivy.environment.Ivy;

public class SproofService {
	private static final String SPROOF_CONNECTOR_API_TOKEN = "com.axonivy.connector.sproof.apiToken";
	private static final String SPROOF_CONNECTOR_SENDER_EMAIL = "com.axonivy.connector.sproof.senderEmail";
	private static final String SPROOF_CONNECTOR_SENDER_FIRST_NAME = "com.axonivy.connector.sproof.senderFirstName";
	private static final String SPROOF_CONNECTOR_SENDER_LAST_NAME = "com.axonivy.connector.sproof.senderLastName";
	private static final SproofService INSTANCE = new SproofService();

	public static SproofService get() {
		return INSTANCE;
	}

	/**
	 * Get the Sproof API Token.
	 * 
	 * @return
	 */
	public String getApiToken() {
		return Ivy.var().get(SPROOF_CONNECTOR_API_TOKEN);
	}

	/**
	 * Get the sender's email address.
	 * 
	 * @return
	 */
	public String getSenderEmail() {
		return Ivy.var().get(SPROOF_CONNECTOR_SENDER_EMAIL);
	}

	/**
	 * Get the sender's first name.
	 * 
	 * @return
	 */

	public String getSenderFirstname() {
		return Ivy.var().get(SPROOF_CONNECTOR_SENDER_FIRST_NAME);
	}
	/**
	 * Get the sender's last name.
	 * 
	 * @return
	 */
	public String getSenderLastname() {
		return Ivy.var().get(SPROOF_CONNECTOR_SENDER_LAST_NAME);
	}

	/**
	 * Prepare a standard signature request.
	 * 
	 * @return
	 */
	public CreateSignatureRequest createCreateSignatureRequest() {
		return new CreateSignatureRequest()
				.token(getApiToken())
				.inviteData(
						new InviteData()
						.sender(
								new Sender()
								.email(getSenderEmail())
								.firstName(getSenderFirstname())
								.lastName(getSenderLastname())
								)

						)
				.envelopeData(new EnvelopeData());
	}

	/**
	 * Prepare a document.
	 * 
	 * @param fileName
	 * @param fileContent
	 * @return
	 */
	public Document createDocumentData(String fileName, byte[] fileContent) {
		return new Document()
				.fileName(fileName)
				.data(Base64.getEncoder().encodeToString(fileContent));
	}

	public static void main(String[] args) {

	}
}
