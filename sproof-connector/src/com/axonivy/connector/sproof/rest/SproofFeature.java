package com.axonivy.connector.sproof.rest;

import java.io.IOException;

import javax.ws.rs.Priorities;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.sproof.sign.api.v1.client.AnyOfMemberSignaturePosition;
import com.sproof.sign.api.v1.client.DocumentRecipientDetails;
import com.sproof.sign.api.v1.client.MemberSignaturePositionResponse;
import com.sproof.sign.api.v1.client.SignaturePositionResponse;

import ch.ivyteam.ivy.rest.client.mapper.JsonFeature;

/**
 * This feature fixes some openapi problems with a strongly typed language like Java.
 */
public class SproofFeature extends JsonFeature {
	public static SimpleModule SPROOF_MODULE = new SimpleModule()
			.addDeserializer(MemberSignaturePositionResponse.class, new MemberSignaturePositionResponseDeserializer());

	@Override
	public boolean configure(FeatureContext context) {
		var provider = new JacksonJsonProvider() {
			@Override
			public ObjectMapper locateMapper(Class<?> type, MediaType mediaType) {
				var mapper = super.locateMapper(type, mediaType);

				mapper.registerModules(
						SPROOF_MODULE,
						new JavaTimeModule());
				// Do not send null values.
				mapper.setDefaultPropertyInclusion(Include.NON_NULL);
				// Use properties instead of getter. This avoids problems with com.sproof.sign.api.v1.client.EnvelopeResponseDocuments.setDueDate(Boolean) which is not a setter.
				mapper.setVisibility(PropertyAccessor.ALL, Visibility.NONE);
				mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
				// Get rid of type info which is not sent or used by Sproof.
				mapper.addMixIn(DocumentRecipientDetails.class, NoTypeInfoMixIn.class);
				mapper.addMixIn(AnyOfMemberSignaturePosition.class, NoTypeInfoMixIn.class);
				mapper.addMixIn(MemberSignaturePositionResponse.class, NoTypeInfoMixIn.class);

				return mapper;
			}
		};

		configure(provider, context.getConfiguration());
		context.register(provider, Priorities.ENTITY_CODER); 
		return true;
	}

	/**
	 * Parse signaturePosition.
	 * 
	 * This should solve two problems:
	 * 
	 * 1. the signature position is sent as an array
	 * 2. there is no type information sent, so the actual polymorphic type is unknown
	 * 
	 * In this case, there is only one possible "polymorphic" value.
	 */
	public static class MemberSignaturePositionResponseDeserializer extends StdDeserializer<MemberSignaturePositionResponse> {
		private static final long serialVersionUID = 1L;

		protected MemberSignaturePositionResponseDeserializer() {
			super(MemberSignaturePositionResponse.class);
		}

		@Override
		public MemberSignaturePositionResponse deserialize(JsonParser parser, DeserializationContext ctx) throws IOException, JacksonException {
			var node = parser.getCodec().readTree(parser);
			if(node.isArray()) {
				node = node.get(0);
			}
			return parser.getCodec().treeToValue(node, SignaturePositionResponse.class);
		}
	}

	@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
	public abstract static class NoTypeInfoMixIn {}
}
