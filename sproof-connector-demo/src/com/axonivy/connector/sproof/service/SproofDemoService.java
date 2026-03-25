package com.axonivy.connector.sproof.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.aspose.pdf.Color;
import com.aspose.pdf.Document;
import com.aspose.pdf.FontRepository;
import com.aspose.pdf.TextFragment;

import ch.ivyteam.ivy.addons.docfactory.aspose.AsposeProduct;
import ch.ivyteam.ivy.addons.docfactory.aspose.LicenseLoader;
import ch.ivyteam.ivy.bpm.error.BpmError;

public class SproofDemoService {
	private static final SproofDemoService INSTANCE = new SproofDemoService();
	
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
			LicenseLoader.loadLicenseforProduct(AsposeProduct.PDF);
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
	
	public byte[] createDocumentWithPlaceholders(String firstname1, String lastname1, String signer1, String firstname2, String lastname2, String signer2) {
		try (var doc = new Document()) {
			LicenseLoader.loadLicenseforProduct(AsposeProduct.PDF);
			var page = doc.getPages().add();

			page.getParagraphs().add(text("Helvetica", 24, Color.getNavy(), "Document with Placeholders"));
			page.getParagraphs().add(text("Times", 12, null, LOREMIPSUM));

			page.getParagraphs().add(text(null, null, null, ""));

			var signers = new ArrayList<Signer>();
			if(StringUtils.isNotBlank(signer1)) {
				signers.add(Signer.create(firstname1, lastname1, signer1));
			}
			if(StringUtils.isNotBlank(signer2)) {
				signers.add(Signer.create(firstname2, lastname2, signer2));
			}
			
			var order = 1;
			for (var signer : signers) {
				page.getParagraphs().add(text("Helvetica-Bold", null, null, "Signare %d".formatted(order)));
				page.getParagraphs().add(text("Helvetica", 64, null, ""));
				page.getParagraphs().add(text("Helvetica", 6, Color.getLightGray(), "{sproof{%s, %s, %s, %d, true}sproof}".formatted(signer.firstname(), signer.lastname(), signer.email(), order)));
				
				order++;
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
	
	protected record Signer(String firstname, String lastname, String email) {
		protected static Signer create(String firstname, String lastname, String email) {
			return new Signer(
					StringUtils.isNotEmpty(firstname) ? firstname : "Firstname",
					StringUtils.isNotEmpty(lastname) ? lastname : "Lastname",
					email
					);
		}
	}
}
