package com.axonivy.connector.sproof;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Base64;

import org.junit.jupiter.api.Test;

import com.axonivy.connector.sproof.rest.SproofFeature;
import com.axonivy.connector.sproof.service.SproofService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sproof.sign.api.v1.client.Document;

public class SproofDocumentDataTest {
	private static final ObjectMapper MAPPER;

	static {
		MAPPER = new ObjectMapper()
				.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
				.registerModule(new JavaTimeModule())
				.registerModule(SproofFeature.SPROOF_MODULE);
	}

	@Test
	public void createDocumentData_encodesContentAsBase64() {
		byte[] content = "Hello sproof!".getBytes();
		var doc = SproofService.get().createDocumentData("contract.pdf", content);
		assertThat(doc.getData()).isEqualTo(Base64.getEncoder().encodeToString(content));
	}

	@Test
	public void createDocumentData_preservesFileName() {
		var doc = SproofService.get().createDocumentData("my-file.pdf", new byte[0]);
		assertThat(doc.getFileName()).isEqualTo("my-file.pdf");
	}

	@Test
	public void createDocumentData_emptyContent_encodesToEmptyBase64() {
		var doc = SproofService.get().createDocumentData("empty.pdf", new byte[0]);
		assertThat(doc.getData()).isEqualTo(Base64.getEncoder().encodeToString(new byte[0]));
	}

	/**
	 * Null fields (e.g. label, boxes) must not be serialized — sproof API rejects
	 * unknown/null fields in some contexts.
	 */
	@Test
	public void nullFieldsNotIncludedInSerialization() throws JsonProcessingException {
		byte[] content = "test".getBytes();
		var doc = SproofService.get().createDocumentData("test.pdf", content);
		var json = MAPPER.writeValueAsString(doc);
		assertThat(json).doesNotContain("\"label\"");
		assertThat(json).doesNotContain("\"boxes\"");
		assertThat(json).contains("\"mimetype\":\"application/pdf\"");
	}

	/**
	 * Fields that were set must appear in the serialized output.
	 */
	@Test
	public void setFieldsAreIncludedInSerialization() throws JsonProcessingException {
		byte[] content = "test".getBytes();
		var doc = SproofService.get().createDocumentData("test.pdf", content);
		var json = MAPPER.writeValueAsString(doc);
		assertThat(json).contains("\"fileName\"");
		assertThat(json).contains("\"data\"");
	}

	/**
	 * Verify round-trip: serialize a Document and deserialize it back.
	 */
	@Test
	public void documentRoundTrip() throws JsonProcessingException {
		byte[] content = "round-trip".getBytes();
		var original = SproofService.get().createDocumentData("round-trip.pdf", content);
		var json = MAPPER.writeValueAsString(original);
		var restored = MAPPER.readValue(json, Document.class);
		assertThat(restored.getFileName()).isEqualTo("round-trip.pdf");
		assertThat(restored.getData()).isEqualTo(Base64.getEncoder().encodeToString(content));
	}
}
