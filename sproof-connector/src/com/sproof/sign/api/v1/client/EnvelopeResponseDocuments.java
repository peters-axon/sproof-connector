/*
 * sign.sproof.com API Documentation
 * **Secure digital signatures made easy.** Developed by **sproof** in Salzburg, Austria, this API allows you to integrate legally binding signature workflows directly into your application.   Learn more about our mission at [sproof.com](https://sproof.com).  ### 🎓 sproof Academy Accelerate your integration with our [Academy modules](https://sproof.atlassian.net/wiki/spaces/Academy/pages/1178664961/sproof+Sign+Quick+Start+Guide+API). These guides cover essential use cases and implementation steps:  * **[Collecting Signatures](https://sproof.atlassian.net/wiki/spaces/Academy/pages/1178107908/sproof+Sign+Collecting+Signatures+API):** Learn how to send documents, handle real-time status updates via callbacks, and retrieve final signed files. * **[Signature Workflows](https://sproof.atlassian.net/wiki/spaces/Academy/pages/1178075162/sproof+Sign+Collecting+Signatures+incl.+Workflow+API):** Streamline processes by merging dynamic documents with pre-configured workflows. Covers workflow creation, recipient invitations, and automated tracking. * **[Embedded Signing Widget](https://sproof.atlassian.net/wiki/spaces/Academy/pages/1178697759/sproof+Widget+Embed+Signing+API):** Integrate the signing experience directly into your own website or application for a seamless user journey.  ### 🔗 Quick Links * **Base URL:** `https://sign.sproof.com/api/v1/` * **Web App:** [sign.sproof.com](https://sign.sproof.com/) * **Developer Support:** [Join our Discord Server](https://discord.gg/HjWe6MUMJ4)   # Using the member ID The member ID can be used to open the document directly within the sproof sign editor after creating a [signature request](#operation/createSignatureRequest). It can be found in the response object after a successful call.  To open the editor the member ID has to be appended to the URL as follows: `https://sign.sproof.com/#/editor/{memberId}`   ### Javascript code sample:  ``` const requestBody = {     \"token\": \"XXXXXXXX-XXXX-XXXX-XXXXXXXXXXXX\",     \"data\": base64File,     \"callbackUrl\": \"https://webhook.site/abc\",     \"fileName\": \"important_contract\",     \"email\": \"max.mustermann@sproof.com\",     \"firstName\": \"Max\",     \"lastName\": \"Mustermann\", }; const requestOptions = {     'method': 'post',     'headers': { 'Content-Type': 'application/json' },     'body': JSON.stringify(requestBody), }; const response = await fetch(\"https://sign.sproof.com/api/v1/documents\", requestOptions); const responseJson = await response.json();  //open the document in the sproof sign editor window.open(\"https://sign.sproof.com/#/editor/\" + responseJson.member.id); ```    # Embed sproof sign in an iframe **Warning! This only works for qualified electronic signatures, as they provide a second factor for authentication!** The [Create signature request](#operation/createSignatureRequest) call can be used to create a signature request. By default, the sproof sign backend sends out emails to all signers.     - For this use case we set the `doNotSendEmail` property in the signer object of the  request to `true`, to avoid sending extra emails to the signer.   - set the `signatureTypes` to `qualified`, i.e. requires a signature with qualified electronic signature.   - Parse the returned JSON object to get the ID of the created document for the respective signer.   - Display an iframe and set the URL to https://sign.sproof.com/#/editor/{ID}   - Optionally set the parameters `returnUrl` and `returnBtnText` to direct the signer to the next document after signing or to return them to a custom page of your application (see image below)  <img style=\"max-width: 650px; width: 100%; display: block; margin: 2.5rem auto;\" class=\"docImg\" src=\"../img/return_btn_text_iframe.png\" />  # Text Placeholders Overview Text placeholders are used in documents (e.g., Microsoft Word DOCX, Google Docs, PDF) to define signature positions or form fields. These placeholders are recognized and processed during the upload.  ## Key Features - **Customizable Appearance:** You can change the color of text placeholders to make them invisible within the document. - **Size Adjustment:** Prefixing a text placeholder with underscores `_` allows you to adjust its size.  ## Constraints - **Minimum Font Size:** Text placeholders must have a font size of at least 4pt to be recognized. - **Single Line Requirement:** Placeholders must not contain line breaks.  ## Signature Placeholders Signature placeholders specify where a document should be signed by designated individuals. The format for a signature placeholder is as follows:  ``` {sproof{first_name, last_name, email_address, signing_order, [optional] doNotSendEmail}sproof} ```  ### Field Descriptions 1. **first_name**:    - **Description**: The first name of the recipient.   - **Example**: `John`   - **Required**: Yes  2. **last_name**:    - **Description**: The last name of the recipient.   - **Example**: `Doe`   - **Required**: Yes  3. **email_address**:    - **Description**: The email address of the recipient. This address will be used to invite the person to sign the document.   - **Example**: `john.doe@example.com`   - **Required**: Yes  4. **signing_order**:    - **Description**: Specifies the order in which the recipients will sign the document. Recipients with the same order value will be invited to sign simultaneously. If you want the document to be signed sequentially, assign incremental values (e.g., `1`, `2`, `3`, etc.).   - **Example**: `1`   - **Required**: Yes  5. **doNotSendEmail** (Optional):    - **Description**: A flag that indicates whether the recipient should receive an email invitation to sign the document. If set to `true`, the signatory will not receive an email.   - **Example**: `true`   - **Default Value**: `false`   - **Required**: No  ### Example ``` {sproof{John, Doe, john.doe@example.com, 1, true}sproof} ```  ### Usage **To activate signature placeholders, set the `usePlaceholders` flag to `true` in the [Create Signature Request](#operation/createSignatureRequest) API call.**  When a document containing signature placeholders is opened in the sproof sign interface, a dialog opens showing the specified recipients. Recipients can then be invited to the document with one click or edited further in the editor. The `signing_order` parameter controls whether all signatories are invited simultaneously or sequentially.  ![Signature Placeholder Example](../img/placeholder_popup.png)  ## Form Field Placeholders Form field placeholders define where users can enter information such as text, dates, or checkboxes. The format for a form field placeholder is as follows:  ``` {sp{type, [optional] label, [optional] content, [optional] recipient_email, [optional] required}sp} ```  ### Field Descriptions 1. **type**:    - **Description**: Specifies the type of form field to be created.   - **Available Options**:     - `TB` - TextBox     - `IB` - IBAN Field     - `DB` - Date Box     - `CB` - Checkbox - **The checkbox requires only a single underscore (_) and its size depends on the font size of the underscore**   - **Example**: `TB`   - **Required**: Yes  2. **label** (Optional):    - **Description**: Defines the label that will be displayed for the form field. This provides a visual cue to the user about what information is expected.   - **Example**: `Name`   - **Required**: No  3. **content** (Optional):    - **Description**: Prefills the form field with the specified content. This is useful for providing default values or examples.   - **Example**: `John Doe`   - **Required**: No  4. **recipient_email** (Optional):    - **Description**: The email address of the recipient assigned to the form field. If not provided, the field is unassigned and can be filled out by anyone with access to the document.   - **Example**: `john.doe@example.com`   - **Required**: No  5. **required** (Optional):    - **Description**: Indicates whether the form field is mandatory for the user to fill out. If set to `true`, the form cannot be submitted until the field is completed.   - **Example**: `true`   - **Default Value**: `false`   - **Required**: No  ### Example ``` {sp{TB, Name, John Doe, john.doe@example.com, true}sp} ``` ### Usage To include form field placeholders, simply insert them into your document and configure the fields as required.  ## Example Files Download example files to see how placeholders are implemented in practice: - [Signature Placeholder Example File (German)](files/Platzhalter-Demo.docx) - [Signature Placeholder Example File (English)](files/Placeholder-Demo.docx) - [Formfield Example File (English)](files/FormField-Demo.docx)  # Authentication  sproof sign uses an **api key** for authentication. It can be obtained by purchasing an enterprise plan. The key can be found in the settings page of the administrator profile (see image below).  <img style=\"max-width: 650px; width: 100%; display: block; margin: 2.5rem auto;\" class=\"docImg\" src=\"../img/api_keys_dashboard.png\" />  # Changelog ## 17/03/2026 - version 3.0.2  - **Updated**: Improved `POST /folder/{folderId}/documents` description and `DELETE /documents/{memberId}` behavior for single documents and documents in an envelope (sender/recipient handling and the `all` parameter).   **[View Full Changelog](https://sign.sproof.com/api/v1/changelog)** 
 *
 * OpenAPI spec version: 3.0.2
 * Contact: support@sproof.com
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package com.sproof.sign.api.v1.client;

import java.util.Objects;
import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.sproof.sign.api.v1.client.DocumentResponseBox;
import com.sproof.sign.api.v1.client.MemberResponseSchema;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
/**
 * EnvelopeResponseDocuments
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2026-03-25T13:40:51.024343900+01:00[Europe/Vienna]")

public class EnvelopeResponseDocuments {
	@JsonProperty("id")
	private String id = null;

	@JsonProperty("name")
	private String name = null;

	@JsonProperty("language")
	private String language = null;

	@JsonProperty("startSendingReminders")
	private OffsetDateTime startSendingReminders = null;

	@JsonProperty("sendReminder")
	private Boolean sendReminder = null;

	@JsonProperty("reminderSent")
	private Boolean reminderSent = null;

	@JsonProperty("reminderInterval")
	private BigDecimal reminderInterval = null;

	@JsonProperty("createdAt")
	private OffsetDateTime createdAt = null;

	@JsonProperty("callbackUrl")
	private String callbackUrl = null;

	@JsonProperty("returnUrl")
	private String returnUrl = null;

	@JsonProperty("returnBtnText")
	private String returnBtnText = null;

	@JsonProperty("inPersonSigning")
	private Boolean inPersonSigning = null;

	@JsonProperty("signingRound")
	private BigDecimal signingRound = null;

	@JsonProperty("isTemplate")
	private Boolean isTemplate = null;

	@JsonProperty("templateId")
	private String templateId = null;

	@JsonProperty("linkId")
	private String linkId = null;

	@JsonProperty("linkExpiresAt")
	private String linkExpiresAt = null;

	@JsonProperty("derivedFromLink")
	private Boolean derivedFromLink = null;

	@JsonProperty("state")
	private String state = null;

	@JsonProperty("complianceLevel")
	private String complianceLevel = null;

	@JsonProperty("allowForwarding")
	private Boolean allowForwarding = null;

	@JsonProperty("linkExpires")
	private Boolean linkExpires = null;

	@JsonProperty("setDueDate")
	private Boolean setDueDate = null;

	@JsonProperty("dueDate")
	private OffsetDateTime dueDate = null;

	@JsonProperty("focusedSigningMode")
	private Boolean focusedSigningMode = null;

	@JsonProperty("linkMaxSignaturesEnabled")
	private Boolean linkMaxSignaturesEnabled = null;

	@JsonProperty("linkMaxSignatures")
	private BigDecimal linkMaxSignatures = null;

	@JsonProperty("fastlaneProfileId")
	private String fastlaneProfileId = null;

	@JsonProperty("refId")
	private String refId = null;

	@JsonProperty("sendOutFinishedPdf")
	private Boolean sendOutFinishedPdf = null;

	@JsonProperty("signatureTypes")
	private List<String> signatureTypes = null;

	@JsonProperty("members")
	private List<MemberResponseSchema> members = null;

	@JsonProperty("member")
  private AllOfEnvelopeResponseDocumentsMember member = null;

	@JsonProperty("boxes")
	private List<DocumentResponseBox> boxes = null;

	@JsonProperty("overdue")
	private Boolean overdue = null;

	public EnvelopeResponseDocuments id(String id) {
		this.id = id;
		return this;
	}

	/**
	 * Get id
	 * @return id
	 **/
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public EnvelopeResponseDocuments name(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Get name
	 * @return name
	 **/
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public EnvelopeResponseDocuments language(String language) {
		this.language = language;
		return this;
	}

	/**
	 * Get language
	 * @return language
	 **/
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public EnvelopeResponseDocuments startSendingReminders(OffsetDateTime startSendingReminders) {
		this.startSendingReminders = startSendingReminders;
		return this;
	}

	/**
	 * Date and time when reminders start being sent (ISO 8601 datetime string)
	 * @return startSendingReminders
	 **/
	public OffsetDateTime getStartSendingReminders() {
		return startSendingReminders;
	}

	public void setStartSendingReminders(OffsetDateTime startSendingReminders) {
		this.startSendingReminders = startSendingReminders;
	}

	public EnvelopeResponseDocuments sendReminder(Boolean sendReminder) {
		this.sendReminder = sendReminder;
		return this;
	}

	/**
	 * Get sendReminder
	 * @return sendReminder
	 **/
	public Boolean isSendReminder() {
		return sendReminder;
	}

	public void setSendReminder(Boolean sendReminder) {
		this.sendReminder = sendReminder;
	}

	public EnvelopeResponseDocuments reminderSent(Boolean reminderSent) {
		this.reminderSent = reminderSent;
		return this;
	}

	/**
	 * Get reminderSent
	 * @return reminderSent
	 **/
	public Boolean isReminderSent() {
		return reminderSent;
	}

	public void setReminderSent(Boolean reminderSent) {
		this.reminderSent = reminderSent;
	}

	public EnvelopeResponseDocuments reminderInterval(BigDecimal reminderInterval) {
		this.reminderInterval = reminderInterval;
		return this;
	}

	/**
	 * Get reminderInterval
	 * @return reminderInterval
	 **/
	public BigDecimal getReminderInterval() {
		return reminderInterval;
	}

	public void setReminderInterval(BigDecimal reminderInterval) {
		this.reminderInterval = reminderInterval;
	}

	public EnvelopeResponseDocuments createdAt(OffsetDateTime createdAt) {
		this.createdAt = createdAt;
		return this;
	}

	/**
	 * Date and time when the document was created (ISO 8601 datetime string)
	 * @return createdAt
	 **/
	public OffsetDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(OffsetDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public EnvelopeResponseDocuments callbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
		return this;
	}

	/**
	 * Get callbackUrl
	 * @return callbackUrl
	 **/
	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

	public EnvelopeResponseDocuments returnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
		return this;
	}

	/**
	 * Get returnUrl
	 * @return returnUrl
	 **/
	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public EnvelopeResponseDocuments returnBtnText(String returnBtnText) {
		this.returnBtnText = returnBtnText;
		return this;
	}

	/**
	 * Get returnBtnText
	 * @return returnBtnText
	 **/
	public String getReturnBtnText() {
		return returnBtnText;
	}

	public void setReturnBtnText(String returnBtnText) {
		this.returnBtnText = returnBtnText;
	}

	public EnvelopeResponseDocuments inPersonSigning(Boolean inPersonSigning) {
		this.inPersonSigning = inPersonSigning;
		return this;
	}

	/**
	 * Get inPersonSigning
	 * @return inPersonSigning
	 **/
	public Boolean isInPersonSigning() {
		return inPersonSigning;
	}

	public void setInPersonSigning(Boolean inPersonSigning) {
		this.inPersonSigning = inPersonSigning;
	}

	public EnvelopeResponseDocuments signingRound(BigDecimal signingRound) {
		this.signingRound = signingRound;
		return this;
	}

	/**
	 * Get signingRound
	 * @return signingRound
	 **/
	public BigDecimal getSigningRound() {
		return signingRound;
	}

	public void setSigningRound(BigDecimal signingRound) {
		this.signingRound = signingRound;
	}

	public EnvelopeResponseDocuments isTemplate(Boolean isTemplate) {
		this.isTemplate = isTemplate;
		return this;
	}

	/**
	 * Get isTemplate
	 * @return isTemplate
	 **/
	public Boolean isIsTemplate() {
		return isTemplate;
	}

	public void setIsTemplate(Boolean isTemplate) {
		this.isTemplate = isTemplate;
	}

	public EnvelopeResponseDocuments templateId(String templateId) {
		this.templateId = templateId;
		return this;
	}

	/**
	 * Get templateId
	 * @return templateId
	 **/
	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public EnvelopeResponseDocuments linkId(String linkId) {
		this.linkId = linkId;
		return this;
	}

	/**
	 * Get linkId
	 * @return linkId
	 **/
	public String getLinkId() {
		return linkId;
	}

	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}

	public EnvelopeResponseDocuments linkExpiresAt(String linkExpiresAt) {
		this.linkExpiresAt = linkExpiresAt;
		return this;
	}

	/**
	 * Get linkExpiresAt
	 * @return linkExpiresAt
	 **/
	public String getLinkExpiresAt() {
		return linkExpiresAt;
	}

	public void setLinkExpiresAt(String linkExpiresAt) {
		this.linkExpiresAt = linkExpiresAt;
	}

	public EnvelopeResponseDocuments derivedFromLink(Boolean derivedFromLink) {
		this.derivedFromLink = derivedFromLink;
		return this;
	}

	/**
	 * Get derivedFromLink
	 * @return derivedFromLink
	 **/
	public Boolean isDerivedFromLink() {
		return derivedFromLink;
	}

	public void setDerivedFromLink(Boolean derivedFromLink) {
		this.derivedFromLink = derivedFromLink;
	}

	public EnvelopeResponseDocuments state(String state) {
		this.state = state;
		return this;
	}

	/**
	 * Get state
	 * @return state
	 **/
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public EnvelopeResponseDocuments complianceLevel(String complianceLevel) {
		this.complianceLevel = complianceLevel;
		return this;
	}

	/**
	 * Get complianceLevel
	 * @return complianceLevel
	 **/
	public String getComplianceLevel() {
		return complianceLevel;
	}

	public void setComplianceLevel(String complianceLevel) {
		this.complianceLevel = complianceLevel;
	}

	public EnvelopeResponseDocuments allowForwarding(Boolean allowForwarding) {
		this.allowForwarding = allowForwarding;
		return this;
	}

	/**
	 * Get allowForwarding
	 * @return allowForwarding
	 **/
	public Boolean isAllowForwarding() {
		return allowForwarding;
	}

	public void setAllowForwarding(Boolean allowForwarding) {
		this.allowForwarding = allowForwarding;
	}

	public EnvelopeResponseDocuments linkExpires(Boolean linkExpires) {
		this.linkExpires = linkExpires;
		return this;
	}

	/**
	 * Get linkExpires
	 * @return linkExpires
	 **/
	public Boolean isLinkExpires() {
		return linkExpires;
	}

	public void setLinkExpires(Boolean linkExpires) {
		this.linkExpires = linkExpires;
	}

  public EnvelopeResponseDocuments setDueDate(Boolean setDueDate) {
    this.setDueDate = setDueDate;
    return this;
  }

	/**
	 * Get setDueDate
	 * @return setDueDate
	 **/
	public Boolean isSetDueDate() {
		return setDueDate;
	}

	public void setSetDueDate(Boolean setDueDate) {
		this.setDueDate = setDueDate;
	}

	public EnvelopeResponseDocuments dueDate(OffsetDateTime dueDate) {
		this.dueDate = dueDate;
		return this;
	}

	/**
	 * Due date for the document (ISO 8601 datetime string)
	 * @return dueDate
	 **/
	public OffsetDateTime getDueDate() {
		return dueDate;
	}

	public void setDueDate(OffsetDateTime dueDate) {
		this.dueDate = dueDate;
	}

	public EnvelopeResponseDocuments focusedSigningMode(Boolean focusedSigningMode) {
		this.focusedSigningMode = focusedSigningMode;
		return this;
	}

	/**
	 * Get focusedSigningMode
	 * @return focusedSigningMode
	 **/
	public Boolean isFocusedSigningMode() {
		return focusedSigningMode;
	}

	public void setFocusedSigningMode(Boolean focusedSigningMode) {
		this.focusedSigningMode = focusedSigningMode;
	}

	public EnvelopeResponseDocuments linkMaxSignaturesEnabled(Boolean linkMaxSignaturesEnabled) {
		this.linkMaxSignaturesEnabled = linkMaxSignaturesEnabled;
		return this;
	}

	/**
	 * Get linkMaxSignaturesEnabled
	 * @return linkMaxSignaturesEnabled
	 **/
	public Boolean isLinkMaxSignaturesEnabled() {
		return linkMaxSignaturesEnabled;
	}

	public void setLinkMaxSignaturesEnabled(Boolean linkMaxSignaturesEnabled) {
		this.linkMaxSignaturesEnabled = linkMaxSignaturesEnabled;
	}

	public EnvelopeResponseDocuments linkMaxSignatures(BigDecimal linkMaxSignatures) {
		this.linkMaxSignatures = linkMaxSignatures;
		return this;
	}

	/**
	 * Get linkMaxSignatures
	 * @return linkMaxSignatures
	 **/
	public BigDecimal getLinkMaxSignatures() {
		return linkMaxSignatures;
	}

	public void setLinkMaxSignatures(BigDecimal linkMaxSignatures) {
		this.linkMaxSignatures = linkMaxSignatures;
	}

	public EnvelopeResponseDocuments fastlaneProfileId(String fastlaneProfileId) {
		this.fastlaneProfileId = fastlaneProfileId;
		return this;
	}

	/**
	 * Get fastlaneProfileId
	 * @return fastlaneProfileId
	 **/
	public String getFastlaneProfileId() {
		return fastlaneProfileId;
	}

	public void setFastlaneProfileId(String fastlaneProfileId) {
		this.fastlaneProfileId = fastlaneProfileId;
	}

	public EnvelopeResponseDocuments refId(String refId) {
		this.refId = refId;
		return this;
	}

	/**
	 * Get refId
	 * @return refId
	 **/
	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public EnvelopeResponseDocuments sendOutFinishedPdf(Boolean sendOutFinishedPdf) {
		this.sendOutFinishedPdf = sendOutFinishedPdf;
		return this;
	}

	/**
	 * Get sendOutFinishedPdf
	 * @return sendOutFinishedPdf
	 **/
	public Boolean isSendOutFinishedPdf() {
		return sendOutFinishedPdf;
	}

	public void setSendOutFinishedPdf(Boolean sendOutFinishedPdf) {
		this.sendOutFinishedPdf = sendOutFinishedPdf;
	}

	public EnvelopeResponseDocuments signatureTypes(List<String> signatureTypes) {
		this.signatureTypes = signatureTypes;
		return this;
	}

	public EnvelopeResponseDocuments addSignatureTypesItem(String signatureTypesItem) {
		if (this.signatureTypes == null) {
			this.signatureTypes = new ArrayList<>();
		}
		this.signatureTypes.add(signatureTypesItem);
		return this;
	}

	/**
	 * Get signatureTypes
	 * @return signatureTypes
	 **/
	public List<String> getSignatureTypes() {
		return signatureTypes;
	}

	public void setSignatureTypes(List<String> signatureTypes) {
		this.signatureTypes = signatureTypes;
	}

	public EnvelopeResponseDocuments members(List<MemberResponseSchema> members) {
		this.members = members;
		return this;
	}

	public EnvelopeResponseDocuments addMembersItem(MemberResponseSchema membersItem) {
		if (this.members == null) {
			this.members = new ArrayList<>();
		}
		this.members.add(membersItem);
		return this;
	}

	/**
	 * Get members
	 * @return members
	 **/
	public List<MemberResponseSchema> getMembers() {
		return members;
	}

	public void setMembers(List<MemberResponseSchema> members) {
		this.members = members;
	}

	public EnvelopeResponseDocuments member(AllOfEnvelopeResponseDocumentsMember member) {
		this.member = member;
		return this;
	}

	/**
	 * Get member
	 * @return member
	 **/
  public AllOfEnvelopeResponseDocumentsMember getMember() {
		return member;
	}

	public void setMember(AllOfEnvelopeResponseDocumentsMember member) {
		this.member = member;
	}

	public EnvelopeResponseDocuments boxes(List<DocumentResponseBox> boxes) {
		this.boxes = boxes;
		return this;
	}

	public EnvelopeResponseDocuments addBoxesItem(DocumentResponseBox boxesItem) {
		if (this.boxes == null) {
			this.boxes = new ArrayList<>();
		}
		this.boxes.add(boxesItem);
		return this;
	}

	/**
	 * A form field box in the document response. This is only returned if the document has form fields. If the document is signed the document will return boxes with the values that were filled out.
	 * @return boxes
	 **/
	public List<DocumentResponseBox> getBoxes() {
		return boxes;
	}

	public void setBoxes(List<DocumentResponseBox> boxes) {
		this.boxes = boxes;
	}

	public EnvelopeResponseDocuments overdue(Boolean overdue) {
		this.overdue = overdue;
		return this;
	}

	/**
	 * Get overdue
	 * @return overdue
	 **/
	public Boolean isOverdue() {
		return overdue;
	}

	public void setOverdue(Boolean overdue) {
		this.overdue = overdue;
	}


	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		EnvelopeResponseDocuments envelopeResponseDocuments = (EnvelopeResponseDocuments) o;
		return Objects.equals(this.id, envelopeResponseDocuments.id) &&
				Objects.equals(this.name, envelopeResponseDocuments.name) &&
				Objects.equals(this.language, envelopeResponseDocuments.language) &&
				Objects.equals(this.startSendingReminders, envelopeResponseDocuments.startSendingReminders) &&
				Objects.equals(this.sendReminder, envelopeResponseDocuments.sendReminder) &&
				Objects.equals(this.reminderSent, envelopeResponseDocuments.reminderSent) &&
				Objects.equals(this.reminderInterval, envelopeResponseDocuments.reminderInterval) &&
				Objects.equals(this.createdAt, envelopeResponseDocuments.createdAt) &&
				Objects.equals(this.callbackUrl, envelopeResponseDocuments.callbackUrl) &&
				Objects.equals(this.returnUrl, envelopeResponseDocuments.returnUrl) &&
				Objects.equals(this.returnBtnText, envelopeResponseDocuments.returnBtnText) &&
				Objects.equals(this.inPersonSigning, envelopeResponseDocuments.inPersonSigning) &&
				Objects.equals(this.signingRound, envelopeResponseDocuments.signingRound) &&
				Objects.equals(this.isTemplate, envelopeResponseDocuments.isTemplate) &&
				Objects.equals(this.templateId, envelopeResponseDocuments.templateId) &&
				Objects.equals(this.linkId, envelopeResponseDocuments.linkId) &&
				Objects.equals(this.linkExpiresAt, envelopeResponseDocuments.linkExpiresAt) &&
				Objects.equals(this.derivedFromLink, envelopeResponseDocuments.derivedFromLink) &&
				Objects.equals(this.state, envelopeResponseDocuments.state) &&
				Objects.equals(this.complianceLevel, envelopeResponseDocuments.complianceLevel) &&
				Objects.equals(this.allowForwarding, envelopeResponseDocuments.allowForwarding) &&
				Objects.equals(this.linkExpires, envelopeResponseDocuments.linkExpires) &&
				Objects.equals(this.setDueDate, envelopeResponseDocuments.setDueDate) &&
				Objects.equals(this.dueDate, envelopeResponseDocuments.dueDate) &&
				Objects.equals(this.focusedSigningMode, envelopeResponseDocuments.focusedSigningMode) &&
				Objects.equals(this.linkMaxSignaturesEnabled, envelopeResponseDocuments.linkMaxSignaturesEnabled) &&
				Objects.equals(this.linkMaxSignatures, envelopeResponseDocuments.linkMaxSignatures) &&
				Objects.equals(this.fastlaneProfileId, envelopeResponseDocuments.fastlaneProfileId) &&
				Objects.equals(this.refId, envelopeResponseDocuments.refId) &&
				Objects.equals(this.sendOutFinishedPdf, envelopeResponseDocuments.sendOutFinishedPdf) &&
				Objects.equals(this.signatureTypes, envelopeResponseDocuments.signatureTypes) &&
				Objects.equals(this.members, envelopeResponseDocuments.members) &&
				Objects.equals(this.member, envelopeResponseDocuments.member) &&
				Objects.equals(this.boxes, envelopeResponseDocuments.boxes) &&
				Objects.equals(this.overdue, envelopeResponseDocuments.overdue);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, language, startSendingReminders, sendReminder, reminderSent, reminderInterval, createdAt, callbackUrl, returnUrl, returnBtnText, inPersonSigning, signingRound, isTemplate, templateId, linkId, linkExpiresAt, derivedFromLink, state, complianceLevel, allowForwarding, linkExpires, setDueDate, dueDate, focusedSigningMode, linkMaxSignaturesEnabled, linkMaxSignatures, fastlaneProfileId, refId, sendOutFinishedPdf, signatureTypes, members, member, boxes, overdue);
	}


	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class EnvelopeResponseDocuments {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    name: ").append(toIndentedString(name)).append("\n");
		sb.append("    language: ").append(toIndentedString(language)).append("\n");
		sb.append("    startSendingReminders: ").append(toIndentedString(startSendingReminders)).append("\n");
		sb.append("    sendReminder: ").append(toIndentedString(sendReminder)).append("\n");
		sb.append("    reminderSent: ").append(toIndentedString(reminderSent)).append("\n");
		sb.append("    reminderInterval: ").append(toIndentedString(reminderInterval)).append("\n");
		sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
		sb.append("    callbackUrl: ").append(toIndentedString(callbackUrl)).append("\n");
		sb.append("    returnUrl: ").append(toIndentedString(returnUrl)).append("\n");
		sb.append("    returnBtnText: ").append(toIndentedString(returnBtnText)).append("\n");
		sb.append("    inPersonSigning: ").append(toIndentedString(inPersonSigning)).append("\n");
		sb.append("    signingRound: ").append(toIndentedString(signingRound)).append("\n");
		sb.append("    isTemplate: ").append(toIndentedString(isTemplate)).append("\n");
		sb.append("    templateId: ").append(toIndentedString(templateId)).append("\n");
		sb.append("    linkId: ").append(toIndentedString(linkId)).append("\n");
		sb.append("    linkExpiresAt: ").append(toIndentedString(linkExpiresAt)).append("\n");
		sb.append("    derivedFromLink: ").append(toIndentedString(derivedFromLink)).append("\n");
		sb.append("    state: ").append(toIndentedString(state)).append("\n");
		sb.append("    complianceLevel: ").append(toIndentedString(complianceLevel)).append("\n");
		sb.append("    allowForwarding: ").append(toIndentedString(allowForwarding)).append("\n");
		sb.append("    linkExpires: ").append(toIndentedString(linkExpires)).append("\n");
		sb.append("    setDueDate: ").append(toIndentedString(setDueDate)).append("\n");
		sb.append("    dueDate: ").append(toIndentedString(dueDate)).append("\n");
		sb.append("    focusedSigningMode: ").append(toIndentedString(focusedSigningMode)).append("\n");
		sb.append("    linkMaxSignaturesEnabled: ").append(toIndentedString(linkMaxSignaturesEnabled)).append("\n");
		sb.append("    linkMaxSignatures: ").append(toIndentedString(linkMaxSignatures)).append("\n");
		sb.append("    fastlaneProfileId: ").append(toIndentedString(fastlaneProfileId)).append("\n");
		sb.append("    refId: ").append(toIndentedString(refId)).append("\n");
		sb.append("    sendOutFinishedPdf: ").append(toIndentedString(sendOutFinishedPdf)).append("\n");
		sb.append("    signatureTypes: ").append(toIndentedString(signatureTypes)).append("\n");
		sb.append("    members: ").append(toIndentedString(members)).append("\n");
		sb.append("    member: ").append(toIndentedString(member)).append("\n");
		sb.append("    boxes: ").append(toIndentedString(boxes)).append("\n");
		sb.append("    overdue: ").append(toIndentedString(overdue)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}

}
