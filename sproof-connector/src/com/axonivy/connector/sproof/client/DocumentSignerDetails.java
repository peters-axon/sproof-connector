package com.axonivy.connector.sproof.client;

import java.util.ArrayList;
import java.util.List;

import com.sproof.sign.api.v1.client.DocumentRecipientDetails;
import com.sproof.sign.api.v1.client.Sender;
import com.sproof.sign.api.v1.client.SignaturePosition;

public class DocumentSignerDetails implements DocumentRecipientDetails {
	private String role = Sender.RoleEnum.SIGNER.getValue();
	private List<SignaturePosition> signaturePositions = new ArrayList<>();

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public List<SignaturePosition> getSignaturePositions() {
		return signaturePositions;
	}

	public void setSignaturePositions(List<SignaturePosition> signaturePositions) {
		this.signaturePositions = signaturePositions;
	}

	public DocumentSignerDetails addSignaturePosition(SignaturePosition signaturePosition) {
		if(signaturePositions == null) {
			signaturePositions = new ArrayList<>();
		}
		signaturePositions.add(signaturePosition);
		return this;
	}
}