package com.axonivy.connector.sproof;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import org.junit.jupiter.api.Test;

import com.axonivy.connector.sproof.rest.SproofFeature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sproof.sign.api.v1.client.EnvelopeResponse;
import com.sproof.sign.api.v1.client.SignaturePositionResponse;
import com.sproof.sign.api.v1.client.SproofDocument;

/**
 * Test the corrected parsing.
 */
public class SproofMappingTest {
	private static final ObjectMapper MAPPER;

	static {
		MAPPER = new ObjectMapper()
				.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
				.registerModule(new JavaTimeModule())
				.registerModule(SproofFeature.SPROOF_MODULE);
	}

	@Test
	public void testParsingSigRqRsp1() throws JsonMappingException, JsonProcessingException {
		var envRsp = MAPPER.readValue(SproofMessages.SIG_RQ_RSP_1, EnvelopeResponse.class);
		assertThat(envRsp).isNotNull();
	}

	@Test
	public void testParsingSigRqRsp1_documentFields() throws JsonMappingException, JsonProcessingException {
		var envRsp = MAPPER.readValue(SproofMessages.SIG_RQ_RSP_1, EnvelopeResponse.class);
		var doc = envRsp.getDocuments().get(0);
		assertThat(doc.getState()).isEqualTo("pending");
		assertThat(doc.getMembers()).hasSize(2);
		assertThat(doc.getCreatedAt()).isEqualTo(OffsetDateTime.parse("2026-03-27T16:43:52.213Z"));
	}

	@Test
	public void testParsingSigRqRsp1_signerEmailsAndOrder() throws JsonMappingException, JsonProcessingException {
		var envRsp = MAPPER.readValue(SproofMessages.SIG_RQ_RSP_1, EnvelopeResponse.class);
		var members = envRsp.getDocuments().get(0).getMembers();
		assertThat(members).extracting("email")
				.containsExactlyInAnyOrder(
						"signer1@sprooftest.axonivy.com",
						"signer2@sprooftest.axonivy.com");
		assertThat(members).extracting("signingOrder")
				.containsExactlyInAnyOrder(BigDecimal.ONE, new BigDecimal("2"));
	}

	/**
	 * signaturePosition in SIG_RQ_RSP_1 is sent as a JSON array — the custom
	 * deserializer must unwrap it to a single {@link SignaturePositionResponse}.
	 */
	@Test
	public void testParsingSigRqRsp1_signaturePositionAsArray() throws JsonMappingException, JsonProcessingException {
		var envRsp = MAPPER.readValue(SproofMessages.SIG_RQ_RSP_1, EnvelopeResponse.class);
		var signer1 = envRsp.getDocuments().get(0).getMembers().stream()
				.filter(m -> "signer1@sprooftest.axonivy.com".equals(m.getEmail()))
				.findFirst().orElseThrow();
		var pos = (SignaturePositionResponse) signer1.getSignaturePosition();
		assertThat(pos).isNotNull();
		assertThat(pos.getX()).isEqualByComparingTo("0.151");
		assertThat(pos.getY()).isEqualByComparingTo("0.23400003");
		assertThat(pos.getPage()).isEqualTo(0);
	}

	@Test
	public void testParsingGetDocRsp1() throws JsonMappingException, JsonProcessingException {
		var envRsp = MAPPER.readValue(SproofMessages.GET_DOC_RSP_1, EnvelopeResponse.class);
		assertThat(envRsp).isNotNull();
	}

	/**
	 * GET_DOC_RSP_1 is a {@link SproofDocument} response — parse it with the correct type
	 * and verify state and member count.
	 */
	@Test
	public void testParsingGetDocRsp1_asSproofDocument() throws JsonMappingException, JsonProcessingException {
		var doc = MAPPER.readValue(SproofMessages.GET_DOC_RSP_1, SproofDocument.class);
		assertThat(doc.getName()).isEqualTo("sign-fixed");
		assertThat(doc.getState()).isEqualTo("pending");
		assertThat(doc.getMembers()).hasSize(2);
		assertThat(doc.isAllSignersSigned()).isFalse();
	}

	/**
	 * signaturePosition in GET_DOC_RSP_1 is sent as a plain JSON object — the custom
	 * deserializer must handle this form as well.
	 */
	@Test
	public void testParsingGetDocRsp1_signaturePositionAsObject() throws JsonMappingException, JsonProcessingException {
		var doc = MAPPER.readValue(SproofMessages.GET_DOC_RSP_1, SproofDocument.class);
		var signer2 = doc.getMembers().stream()
				.filter(m -> "signer2@sprooftest.axonivy.com".equals(m.getEmail()))
				.findFirst().orElseThrow();
		var pos = (SignaturePositionResponse) signer2.getSignaturePosition();
		assertThat(pos).isNotNull();
		assertThat(pos.getX()).isEqualByComparingTo("0.3");
		assertThat(pos.getY()).isEqualByComparingTo("0.8");
		assertThat(pos.getPage()).isEqualTo(0);
	}

	@Test
	public void testParsingGetDocRsp2() throws JsonMappingException, JsonProcessingException {
		var envRsp = MAPPER.readValue(SproofMessages.GET_DOC_RSP_2, EnvelopeResponse.class);
		assertThat(envRsp).isNotNull();
	}

	/**
	 * GET_DOC_RSP_2 contains one member with {@code "signaturePosition": null}
	 * (already signed) — must not cause a NullPointerException.
	 */
	@Test
	public void testParsingGetDocRsp2_signaturePositionNull() throws JsonMappingException, JsonProcessingException {
		var doc = MAPPER.readValue(SproofMessages.GET_DOC_RSP_2, SproofDocument.class);
		var signedMember = doc.getMembers().stream()
				.filter(m -> Boolean.TRUE.equals(m.isSigned()))
				.findFirst().orElseThrow();
		assertThat(signedMember.getSignaturePosition()).isNull();
		assertThat(signedMember.getSignedAt())
				.isEqualTo(OffsetDateTime.parse("2026-04-02T15:19:25.942Z"));
	}

	/**
	 * GET_DOC_RSP_2 contains a signed member with a recorded signature entry.
	 */
	@Test
	public void testParsingGetDocRsp2_signatureEntry() throws JsonMappingException, JsonProcessingException {
		var doc = MAPPER.readValue(SproofMessages.GET_DOC_RSP_2, SproofDocument.class);
		var signedMember = doc.getMembers().stream()
				.filter(m -> Boolean.TRUE.equals(m.isSigned()))
				.findFirst().orElseThrow();
		assertThat(signedMember.getSignatures()).hasSize(1);
		assertThat(signedMember.getSignatures().get(0).getSignatureType()).isEqualTo("aes_sproof");
	}
}
