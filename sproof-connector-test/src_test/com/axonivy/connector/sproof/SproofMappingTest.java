package com.axonivy.connector.sproof;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.axonivy.connector.sproof.rest.SproofFeature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sproof.sign.api.v1.client.EnvelopeResponse;

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
	public void testParsingGetDocRsp1() throws JsonMappingException, JsonProcessingException {
		var envRsp = MAPPER.readValue(SproofMessages.GET_DOC_RSP_1, EnvelopeResponse.class);
		assertThat(envRsp).isNotNull();
	}	

	@Test
	public void testParsingGetDocRsp2() throws JsonMappingException, JsonProcessingException {
		var envRsp = MAPPER.readValue(SproofMessages.GET_DOC_RSP_2, EnvelopeResponse.class);
		assertThat(envRsp).isNotNull();
	}	
}
