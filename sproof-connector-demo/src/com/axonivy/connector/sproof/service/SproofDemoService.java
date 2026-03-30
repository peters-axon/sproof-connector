package com.axonivy.connector.sproof.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.aspose.pdf.Color;
import com.aspose.pdf.Document;
import com.aspose.pdf.FontRepository;
import com.aspose.pdf.TextFragment;
import com.sproof.sign.api.v1.client.CreateSignatureRequest;
import com.sproof.sign.api.v1.client.Signer;

import ch.ivyteam.ivy.addons.docfactory.aspose.AsposeProduct;
import ch.ivyteam.ivy.addons.docfactory.aspose.LicenseLoader;
import ch.ivyteam.ivy.bpm.error.BpmError;
import ch.ivyteam.ivy.environment.Ivy;

public class SproofDemoService {
	private static final SproofDemoService INSTANCE = new SproofDemoService();


	static {
		try {
			LicenseLoader.loadLicenseforProduct(AsposeProduct.PDF);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static final String LOREMIPSUM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
			+ "Integer nec odio. Praesent libero. Sed cursus ante dapibus diam. Sed nisi. "
			+ "Nulla quis sem at nibh elementum imperdiet. Duis sagittis ipsum. Praesent mauris. "
			+ "Fusce nec tellus sed augue semper porta. Mauris massa. "
			+ "Vestibulum lacinia arcu eget nulla.\n\n"
			+ "Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. "
			+ "Curabitur sodales ligula in libero. Sed dignissim lacinia nunc. Curabitur tortor. "
			+ "Pellentesque nibh. Aenean quam. In scelerisque sem at dolor. Maecenas mattis. "
			+ "Sed convallis tristique sem. Proin ut ligula vel nunc egestas porttitor.";

	public static SproofDemoService get() {
		return INSTANCE;
	}

	public StreamedContent createStreamedContent(String name, byte[] content) {
		return DefaultStreamedContent.builder()
				.contentType("application/pdf")
				.name(name)
				.stream(() -> new ByteArrayInputStream(content))
				.build();
	}

	public byte[] createDocument() {
		try (var doc = new Document()) {
			var page = doc.getPages().add();

			page.getParagraphs().add(text("Helvetica", 24, Color.getNavy(), "Document"));
			page.getParagraphs().add(text("Times", 12, null, LOREMIPSUM));

			try (var os = new ByteArrayOutputStream()) {
				doc.save(os);

				return os.toByteArray();
			}
		}
		catch (Exception e) {
			throw BpmError.create("createDocument").withCause(e).build();
		}
	}

	public byte[] createDocumentWithPlaceholders(String signer1Email, String signer1FirstName, String signer1LastName, String signer2Email, String signer2FirstName, String signer2LastName) {
		try (var doc = new Document()) {
			var page = doc.getPages().add();

			page.getParagraphs().add(text("Helvetica", 24, Color.getNavy(), "Document with Placeholders"));
			page.getParagraphs().add(text("Times", 12, null, LOREMIPSUM));

			page.getParagraphs().add(text(null, null, null, ""));

			var signers = createSigners(signer1Email, signer1FirstName, signer1LastName, signer2Email, signer2FirstName, signer2LastName);

			for (var signer : signers) {
				page.getParagraphs().add(text("Helvetica", 128, null, ""));
				page.getParagraphs().add(text("Helvetica", 6, Color.getLightGray(), "{sproof{%s, %s, %s, %d, true}sproof}".formatted(signer.getFirstName(), signer.getLastName(), signer.getEmail(), signer.getSigningOrder())));
				page.getParagraphs().add(text("Helvetica-Bold", null, null, "Signare %d".formatted(signer.getSigningOrder())));
			}

			try (var os = new ByteArrayOutputStream()) {
				doc.save(os);

				return os.toByteArray();
			}
		}
		catch (Exception e) {
			throw BpmError.create("createDocument").withCause(e).build();
		}
	}

	protected TextFragment text(String font, Integer size, Color color, String text) {
		var textFragment = new TextFragment(text);
		if(font != null) {
			textFragment.getTextState().setFont(FontRepository.findFont(font));
		}
		if(size != null) {
			textFragment.getTextState().setFontSize(size);
		}
		if(color != null) {
			textFragment.getTextState().setForegroundColor(color);
		}

		return textFragment;
	}

	/**
	 * Create a list of signers.
	 * 
	 * @param signer1Email
	 * @param signer1FirstName
	 * @param signer1LastName
	 * @param signer2Email
	 * @param signer2FirstName
	 * @param signer2LastName
	 * @return
	 */
	public List<Signer> createSigners(String signer1Email, String signer1FirstName, String signer1LastName,
			String signer2Email, String signer2FirstName, String signer2LastName) {
		var signers = new ArrayList<Signer>();

		var order = 1;

		if(StringUtils.isNoneBlank(signer1Email, signer1FirstName, signer1LastName)) {
			signers.add(createSigner(signer1Email, signer1FirstName, signer1LastName, order++));
		}

		if(StringUtils.isNoneBlank(signer2Email, signer2FirstName, signer2LastName)) {
			signers.add(createSigner(signer2Email, signer2FirstName, signer2LastName, order++));
		}

		return signers;
	}

	protected Signer createSigner(String email, String firstName, String lastName, int signingOrder) {
		return new Signer()
				.firstName(StringUtils.isNotEmpty(firstName) ? firstName : "Firstname")
				.lastName(StringUtils.isNotEmpty(lastName) ? lastName : "Lastname")
				.email(email)
				.signingOrder(signingOrder);
	}

	public void check(CreateSignatureRequest rq) {
		Ivy.log().info("Req: {0}", rq);
	}
}
