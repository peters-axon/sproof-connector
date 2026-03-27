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
import com.sproof.sign.api.v1.client.MemberSignaturePositionResponse;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
/**
 * MemberResponseSchema
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2026-03-25T13:40:51.024343900+01:00[Europe/Vienna]")

public class MemberResponseSchema {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("documentId")
  private String documentId = null;

  @JsonProperty("email")
  private String email = null;

  @JsonProperty("firstName")
  private String firstName = null;

  @JsonProperty("lastName")
  private String lastName = null;

  @JsonProperty("isAdmin")
  private Boolean isAdmin = null;

  @JsonProperty("createdAt")
  private OffsetDateTime createdAt = null;

  @JsonProperty("sentDeleteNotificationAt")
  private OffsetDateTime sentDeleteNotificationAt = null;

  @JsonProperty("manuallyLastEmailSent")
  private OffsetDateTime manuallyLastEmailSent = null;

  @JsonProperty("signaturePosition")
  private MemberSignaturePositionResponse signaturePosition = null;

  @JsonProperty("doNotSendEmails")
  private Boolean doNotSendEmails = null;

  @JsonProperty("declinedAt")
  private OffsetDateTime declinedAt = null;

  @JsonProperty("signingOrder")
  private BigDecimal signingOrder = null;

  @JsonProperty("onDemand")
  private String onDemand = null;

  @JsonProperty("recipientId")
  private String recipientId = null;

  @JsonProperty("signatureTypes")
  private List<String> signatureTypes = null;

  @JsonProperty("signatureTypesActive")
  private Boolean signatureTypesActive = null;

  @JsonProperty("privateMessage")
  private String privateMessage = null;

  @JsonProperty("customId")
  private String customId = null;

  @JsonProperty("useFastlane")
  private Boolean useFastlane = null;

  @JsonProperty("role")
  private String role = null;

  @JsonProperty("approvedAt")
  private OffsetDateTime approvedAt = null;

  @JsonProperty("verificationConfig")
  private Object verificationConfig = null;

  @JsonProperty("phoneNumber")
  private String phoneNumber = null;

  @JsonProperty("signatures")
  private List<String> signatures = null;

  @JsonProperty("signedAt")
  private OffsetDateTime signedAt = null;

  public MemberResponseSchema id(String id) {
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

  public MemberResponseSchema documentId(String documentId) {
    this.documentId = documentId;
    return this;
  }

   /**
   * Get documentId
   * @return documentId
  **/
  public String getDocumentId() {
    return documentId;
  }

  public void setDocumentId(String documentId) {
    this.documentId = documentId;
  }

  public MemberResponseSchema email(String email) {
    this.email = email;
    return this;
  }

   /**
   * Get email
   * @return email
  **/
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public MemberResponseSchema firstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

   /**
   * Get firstName
   * @return firstName
  **/
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public MemberResponseSchema lastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

   /**
   * Get lastName
   * @return lastName
  **/
  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public MemberResponseSchema isAdmin(Boolean isAdmin) {
    this.isAdmin = isAdmin;
    return this;
  }

   /**
   * Get isAdmin
   * @return isAdmin
  **/
  public Boolean isIsAdmin() {
    return isAdmin;
  }

  public void setIsAdmin(Boolean isAdmin) {
    this.isAdmin = isAdmin;
  }

  public MemberResponseSchema createdAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

   /**
   * Date and time when the member was created (ISO 8601 datetime string)
   * @return createdAt
  **/
  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public MemberResponseSchema sentDeleteNotificationAt(OffsetDateTime sentDeleteNotificationAt) {
    this.sentDeleteNotificationAt = sentDeleteNotificationAt;
    return this;
  }

   /**
   * Date and time when delete notification was sent (ISO 8601 datetime string)
   * @return sentDeleteNotificationAt
  **/
  public OffsetDateTime getSentDeleteNotificationAt() {
    return sentDeleteNotificationAt;
  }

  public void setSentDeleteNotificationAt(OffsetDateTime sentDeleteNotificationAt) {
    this.sentDeleteNotificationAt = sentDeleteNotificationAt;
  }

  public MemberResponseSchema manuallyLastEmailSent(OffsetDateTime manuallyLastEmailSent) {
    this.manuallyLastEmailSent = manuallyLastEmailSent;
    return this;
  }

   /**
   * Date and time when the last manual email was sent (ISO 8601 datetime string)
   * @return manuallyLastEmailSent
  **/
  public OffsetDateTime getManuallyLastEmailSent() {
    return manuallyLastEmailSent;
  }

  public void setManuallyLastEmailSent(OffsetDateTime manuallyLastEmailSent) {
    this.manuallyLastEmailSent = manuallyLastEmailSent;
  }

  public MemberResponseSchema signaturePosition(MemberSignaturePositionResponse signaturePosition) {
    this.signaturePosition = signaturePosition;
    return this;
  }

   /**
   * Get signaturePosition
   * @return signaturePosition
  **/
  public MemberSignaturePositionResponse getSignaturePosition() {
    return signaturePosition;
  }

  public void setSignaturePosition(MemberSignaturePositionResponse signaturePosition) {
    this.signaturePosition = signaturePosition;
  }

  public MemberResponseSchema doNotSendEmails(Boolean doNotSendEmails) {
    this.doNotSendEmails = doNotSendEmails;
    return this;
  }

   /**
   * Get doNotSendEmails
   * @return doNotSendEmails
  **/
  public Boolean isDoNotSendEmails() {
    return doNotSendEmails;
  }

  public void setDoNotSendEmails(Boolean doNotSendEmails) {
    this.doNotSendEmails = doNotSendEmails;
  }

  public MemberResponseSchema declinedAt(OffsetDateTime declinedAt) {
    this.declinedAt = declinedAt;
    return this;
  }

   /**
   * Date and time when the member declined (ISO 8601 datetime string)
   * @return declinedAt
  **/
  public OffsetDateTime getDeclinedAt() {
    return declinedAt;
  }

  public void setDeclinedAt(OffsetDateTime declinedAt) {
    this.declinedAt = declinedAt;
  }

  public MemberResponseSchema signingOrder(BigDecimal signingOrder) {
    this.signingOrder = signingOrder;
    return this;
  }

   /**
   * Get signingOrder
   * @return signingOrder
  **/
  public BigDecimal getSigningOrder() {
    return signingOrder;
  }

  public void setSigningOrder(BigDecimal signingOrder) {
    this.signingOrder = signingOrder;
  }

  public MemberResponseSchema onDemand(String onDemand) {
    this.onDemand = onDemand;
    return this;
  }

   /**
   * Get onDemand
   * @return onDemand
  **/
  public String getOnDemand() {
    return onDemand;
  }

  public void setOnDemand(String onDemand) {
    this.onDemand = onDemand;
  }

  public MemberResponseSchema recipientId(String recipientId) {
    this.recipientId = recipientId;
    return this;
  }

   /**
   * Get recipientId
   * @return recipientId
  **/
  public String getRecipientId() {
    return recipientId;
  }

  public void setRecipientId(String recipientId) {
    this.recipientId = recipientId;
  }

  public MemberResponseSchema signatureTypes(List<String> signatureTypes) {
    this.signatureTypes = signatureTypes;
    return this;
  }

  public MemberResponseSchema addSignatureTypesItem(String signatureTypesItem) {
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

  public MemberResponseSchema signatureTypesActive(Boolean signatureTypesActive) {
    this.signatureTypesActive = signatureTypesActive;
    return this;
  }

   /**
   * Get signatureTypesActive
   * @return signatureTypesActive
  **/
  public Boolean isSignatureTypesActive() {
    return signatureTypesActive;
  }

  public void setSignatureTypesActive(Boolean signatureTypesActive) {
    this.signatureTypesActive = signatureTypesActive;
  }

  public MemberResponseSchema privateMessage(String privateMessage) {
    this.privateMessage = privateMessage;
    return this;
  }

   /**
   * Get privateMessage
   * @return privateMessage
  **/
  public String getPrivateMessage() {
    return privateMessage;
  }

  public void setPrivateMessage(String privateMessage) {
    this.privateMessage = privateMessage;
  }

  public MemberResponseSchema customId(String customId) {
    this.customId = customId;
    return this;
  }

   /**
   * Get customId
   * @return customId
  **/
  public String getCustomId() {
    return customId;
  }

  public void setCustomId(String customId) {
    this.customId = customId;
  }

  public MemberResponseSchema useFastlane(Boolean useFastlane) {
    this.useFastlane = useFastlane;
    return this;
  }

   /**
   * Get useFastlane
   * @return useFastlane
  **/
  public Boolean isUseFastlane() {
    return useFastlane;
  }

  public void setUseFastlane(Boolean useFastlane) {
    this.useFastlane = useFastlane;
  }

  public MemberResponseSchema role(String role) {
    this.role = role;
    return this;
  }

   /**
   * Get role
   * @return role
  **/
  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public MemberResponseSchema approvedAt(OffsetDateTime approvedAt) {
    this.approvedAt = approvedAt;
    return this;
  }

   /**
   * Date and time when the member was approved (ISO 8601 datetime string)
   * @return approvedAt
  **/
  public OffsetDateTime getApprovedAt() {
    return approvedAt;
  }

  public void setApprovedAt(OffsetDateTime approvedAt) {
    this.approvedAt = approvedAt;
  }

  public MemberResponseSchema verificationConfig(Object verificationConfig) {
    this.verificationConfig = verificationConfig;
    return this;
  }

   /**
   * Get verificationConfig
   * @return verificationConfig
  **/
  public Object getVerificationConfig() {
    return verificationConfig;
  }

  public void setVerificationConfig(Object verificationConfig) {
    this.verificationConfig = verificationConfig;
  }

  public MemberResponseSchema phoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
    return this;
  }

   /**
   * Get phoneNumber
   * @return phoneNumber
  **/
  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public MemberResponseSchema signatures(List<String> signatures) {
    this.signatures = signatures;
    return this;
  }

  public MemberResponseSchema addSignaturesItem(String signaturesItem) {
    if (this.signatures == null) {
      this.signatures = new ArrayList<>();
    }
    this.signatures.add(signaturesItem);
    return this;
  }

   /**
   * Get signatures
   * @return signatures
  **/
  public List<String> getSignatures() {
    return signatures;
  }

  public void setSignatures(List<String> signatures) {
    this.signatures = signatures;
  }

  public MemberResponseSchema signedAt(OffsetDateTime signedAt) {
    this.signedAt = signedAt;
    return this;
  }

   /**
   * Date and time when the member signed (ISO 8601 datetime string)
   * @return signedAt
  **/
  public OffsetDateTime getSignedAt() {
    return signedAt;
  }

  public void setSignedAt(OffsetDateTime signedAt) {
    this.signedAt = signedAt;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MemberResponseSchema memberResponseSchema = (MemberResponseSchema) o;
    return Objects.equals(this.id, memberResponseSchema.id) &&
        Objects.equals(this.documentId, memberResponseSchema.documentId) &&
        Objects.equals(this.email, memberResponseSchema.email) &&
        Objects.equals(this.firstName, memberResponseSchema.firstName) &&
        Objects.equals(this.lastName, memberResponseSchema.lastName) &&
        Objects.equals(this.isAdmin, memberResponseSchema.isAdmin) &&
        Objects.equals(this.createdAt, memberResponseSchema.createdAt) &&
        Objects.equals(this.sentDeleteNotificationAt, memberResponseSchema.sentDeleteNotificationAt) &&
        Objects.equals(this.manuallyLastEmailSent, memberResponseSchema.manuallyLastEmailSent) &&
        Objects.equals(this.signaturePosition, memberResponseSchema.signaturePosition) &&
        Objects.equals(this.doNotSendEmails, memberResponseSchema.doNotSendEmails) &&
        Objects.equals(this.declinedAt, memberResponseSchema.declinedAt) &&
        Objects.equals(this.signingOrder, memberResponseSchema.signingOrder) &&
        Objects.equals(this.onDemand, memberResponseSchema.onDemand) &&
        Objects.equals(this.recipientId, memberResponseSchema.recipientId) &&
        Objects.equals(this.signatureTypes, memberResponseSchema.signatureTypes) &&
        Objects.equals(this.signatureTypesActive, memberResponseSchema.signatureTypesActive) &&
        Objects.equals(this.privateMessage, memberResponseSchema.privateMessage) &&
        Objects.equals(this.customId, memberResponseSchema.customId) &&
        Objects.equals(this.useFastlane, memberResponseSchema.useFastlane) &&
        Objects.equals(this.role, memberResponseSchema.role) &&
        Objects.equals(this.approvedAt, memberResponseSchema.approvedAt) &&
        Objects.equals(this.verificationConfig, memberResponseSchema.verificationConfig) &&
        Objects.equals(this.phoneNumber, memberResponseSchema.phoneNumber) &&
        Objects.equals(this.signatures, memberResponseSchema.signatures) &&
        Objects.equals(this.signedAt, memberResponseSchema.signedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, documentId, email, firstName, lastName, isAdmin, createdAt, sentDeleteNotificationAt, manuallyLastEmailSent, signaturePosition, doNotSendEmails, declinedAt, signingOrder, onDemand, recipientId, signatureTypes, signatureTypesActive, privateMessage, customId, useFastlane, role, approvedAt, verificationConfig, phoneNumber, signatures, signedAt);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MemberResponseSchema {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    documentId: ").append(toIndentedString(documentId)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
    sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
    sb.append("    isAdmin: ").append(toIndentedString(isAdmin)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    sentDeleteNotificationAt: ").append(toIndentedString(sentDeleteNotificationAt)).append("\n");
    sb.append("    manuallyLastEmailSent: ").append(toIndentedString(manuallyLastEmailSent)).append("\n");
    sb.append("    signaturePosition: ").append(toIndentedString(signaturePosition)).append("\n");
    sb.append("    doNotSendEmails: ").append(toIndentedString(doNotSendEmails)).append("\n");
    sb.append("    declinedAt: ").append(toIndentedString(declinedAt)).append("\n");
    sb.append("    signingOrder: ").append(toIndentedString(signingOrder)).append("\n");
    sb.append("    onDemand: ").append(toIndentedString(onDemand)).append("\n");
    sb.append("    recipientId: ").append(toIndentedString(recipientId)).append("\n");
    sb.append("    signatureTypes: ").append(toIndentedString(signatureTypes)).append("\n");
    sb.append("    signatureTypesActive: ").append(toIndentedString(signatureTypesActive)).append("\n");
    sb.append("    privateMessage: ").append(toIndentedString(privateMessage)).append("\n");
    sb.append("    customId: ").append(toIndentedString(customId)).append("\n");
    sb.append("    useFastlane: ").append(toIndentedString(useFastlane)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    approvedAt: ").append(toIndentedString(approvedAt)).append("\n");
    sb.append("    verificationConfig: ").append(toIndentedString(verificationConfig)).append("\n");
    sb.append("    phoneNumber: ").append(toIndentedString(phoneNumber)).append("\n");
    sb.append("    signatures: ").append(toIndentedString(signatures)).append("\n");
    sb.append("    signedAt: ").append(toIndentedString(signedAt)).append("\n");
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
