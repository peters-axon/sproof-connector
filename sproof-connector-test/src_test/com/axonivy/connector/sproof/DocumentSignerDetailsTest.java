package com.axonivy.connector.sproof;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.axonivy.connector.sproof.client.DocumentSignerDetails;
import com.sproof.sign.api.v1.client.Sender;
import com.sproof.sign.api.v1.client.SignaturePosition;

public class DocumentSignerDetailsTest {

	@Test
	public void defaultRoleIsSigner() {
		var details = new DocumentSignerDetails();
		assertThat(details.getRole()).isEqualTo(Sender.RoleEnum.SIGNER.getValue());
	}

	@Test
	public void addSignaturePosition_returnsThis() {
		var details = new DocumentSignerDetails();
		var result = details.addSignaturePosition(new SignaturePosition());
		assertThat(result).isSameAs(details);
	}

	@Test
	public void addSignaturePosition_appendsToList() {
		var details = new DocumentSignerDetails();
		var pos1 = new SignaturePosition().page(0).x(new java.math.BigDecimal("0.1"));
		var pos2 = new SignaturePosition().page(1).x(new java.math.BigDecimal("0.5"));
		details.addSignaturePosition(pos1).addSignaturePosition(pos2);
		assertThat(details.getSignaturePositions()).containsExactly(pos1, pos2);
	}

	@Test
	public void setSignaturePositions_null_thenAdd_initializesList() {
		var details = new DocumentSignerDetails();
		details.setSignaturePositions(null);
		var pos = new SignaturePosition();
		details.addSignaturePosition(pos);
		assertThat(details.getSignaturePositions()).containsExactly(pos);
	}

	@Test
	public void setRole_changesRole() {
		var details = new DocumentSignerDetails();
		details.setRole(Sender.RoleEnum.NONE.getValue());
		assertThat(details.getRole()).isEqualTo(Sender.RoleEnum.NONE.getValue());
	}
}
