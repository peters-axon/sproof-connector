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
import com.sproof.sign.api.v1.client.MemberSignatures;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
/**
 * Member
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2026-03-25T13:40:51.024343900+01:00[Europe/Vienna]")

public class Member {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("email")
  private String email = null;

  @JsonProperty("firstName")
  private String firstName = null;

  @JsonProperty("lastName")
  private String lastName = null;

  @JsonProperty("lastActivityAt")
  private OffsetDateTime lastActivityAt = null;

  @JsonProperty("createdAt")
  private OffsetDateTime createdAt = null;

  @JsonProperty("signed")
  private Boolean signed = null;

  @JsonProperty("isAdmin")
  private Boolean isAdmin = null;

  @JsonProperty("isSigner")
  private Boolean isSigner = null;

  @JsonProperty("isApprover")
  private Boolean isApprover = null;

  @JsonProperty("isViewer")
  private Boolean isViewer = null;

  @JsonProperty("approvedAt")
  private OffsetDateTime approvedAt = null;

  @JsonProperty("viewedAt")
  private OffsetDateTime viewedAt = null;

  @JsonProperty("signaturePosition")
  private AnyOfMemberSignaturePosition signaturePosition = null;

  @JsonProperty("signedAt")
  private OffsetDateTime signedAt = null;

  @JsonProperty("signingOrder")
  private BigDecimal signingOrder = null;

  @JsonProperty("declinedAt")
  private OffsetDateTime declinedAt = null;

  @JsonProperty("signatures")
  private List<MemberSignatures> signatures = new ArrayList<>();

  public Member id(String id) {
    this.id = id;
    return this;
  }

   /**
   * The member ID can be used to open the document within the sproof sign editor. A more detailed description can be found [here](#section/Using-the-member-ID)
   * @return id
  **/
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Member email(String email) {
    this.email = email;
    return this;
  }

   /**
   * Email of document owner
   * @return email
  **/
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Member firstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

   /**
   * Firstname of document owner
   * @return firstName
  **/
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public Member lastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

   /**
   * Lastname of document owner
   * @return lastName
  **/
  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public Member lastActivityAt(OffsetDateTime lastActivityAt) {
    this.lastActivityAt = lastActivityAt;
    return this;
  }

   /**
   * Date and time of last activity (ISO 8601 datetime string)
   * @return lastActivityAt
  **/
  public OffsetDateTime getLastActivityAt() {
    return lastActivityAt;
  }

  public void setLastActivityAt(OffsetDateTime lastActivityAt) {
    this.lastActivityAt = lastActivityAt;
  }

  public Member createdAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

   /**
   * Date and time of document upload (ISO 8601 datetime string)
   * @return createdAt
  **/
  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public Member signed(Boolean signed) {
    this.signed = signed;
    return this;
  }

   /**
   * Indicates whether the document was signed by the owner
   * @return signed
  **/
  public Boolean isSigned() {
    return signed;
  }

  public void setSigned(Boolean signed) {
    this.signed = signed;
  }

  public Member isAdmin(Boolean isAdmin) {
    this.isAdmin = isAdmin;
    return this;
  }

   /**
   * Indicates whether the member has Admin status
   * @return isAdmin
  **/
  public Boolean isIsAdmin() {
    return isAdmin;
  }

  public void setIsAdmin(Boolean isAdmin) {
    this.isAdmin = isAdmin;
  }

  public Member isSigner(Boolean isSigner) {
    this.isSigner = isSigner;
    return this;
  }

   /**
   * Indicates whether the member is a signer
   * @return isSigner
  **/
  public Boolean isIsSigner() {
    return isSigner;
  }

  public void setIsSigner(Boolean isSigner) {
    this.isSigner = isSigner;
  }

  public Member isApprover(Boolean isApprover) {
    this.isApprover = isApprover;
    return this;
  }

   /**
   * Indicates whether the member is an approver
   * @return isApprover
  **/
  public Boolean isIsApprover() {
    return isApprover;
  }

  public void setIsApprover(Boolean isApprover) {
    this.isApprover = isApprover;
  }

  public Member isViewer(Boolean isViewer) {
    this.isViewer = isViewer;
    return this;
  }

   /**
   * Indicates whether the member is a viewer
   * @return isViewer
  **/
  public Boolean isIsViewer() {
    return isViewer;
  }

  public void setIsViewer(Boolean isViewer) {
    this.isViewer = isViewer;
  }

  public Member approvedAt(OffsetDateTime approvedAt) {
    this.approvedAt = approvedAt;
    return this;
  }

   /**
   * Only if isApprover is true. Date and time when the member was approved (ISO 8601 datetime string)
   * @return approvedAt
  **/
  public OffsetDateTime getApprovedAt() {
    return approvedAt;
  }

  public void setApprovedAt(OffsetDateTime approvedAt) {
    this.approvedAt = approvedAt;
  }

  public Member viewedAt(OffsetDateTime viewedAt) {
    this.viewedAt = viewedAt;
    return this;
  }

   /**
   * Only if isViewer is true. Date and time when the member viewed the document (ISO 8601 datetime string)
   * @return viewedAt
  **/
  public OffsetDateTime getViewedAt() {
    return viewedAt;
  }

  public void setViewedAt(OffsetDateTime viewedAt) {
    this.viewedAt = viewedAt;
  }

  public Member signaturePosition(AnyOfMemberSignaturePosition signaturePosition) {
    this.signaturePosition = signaturePosition;
    return this;
  }

   /**
   * The signature position(s) for this member. Based on what is sent in the request you can expect either a single position object or an array of position objects.
   * @return signaturePosition
  **/
  public AnyOfMemberSignaturePosition getSignaturePosition() {
    return signaturePosition;
  }

  public void setSignaturePosition(AnyOfMemberSignaturePosition signaturePosition) {
    this.signaturePosition = signaturePosition;
  }

  public Member signedAt(OffsetDateTime signedAt) {
    this.signedAt = signedAt;
    return this;
  }

   /**
   * Date and time of the signature (ISO 8601 datetime string)
   * @return signedAt
  **/
  public OffsetDateTime getSignedAt() {
    return signedAt;
  }

  public void setSignedAt(OffsetDateTime signedAt) {
    this.signedAt = signedAt;
  }

  public Member signingOrder(BigDecimal signingOrder) {
    this.signingOrder = signingOrder;
    return this;
  }

   /**
   * The order in which this member should sign
   * @return signingOrder
  **/
  public BigDecimal getSigningOrder() {
    return signingOrder;
  }

  public void setSigningOrder(BigDecimal signingOrder) {
    this.signingOrder = signingOrder;
  }

  public Member declinedAt(OffsetDateTime declinedAt) {
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

  public Member signatures(List<MemberSignatures> signatures) {
    this.signatures = signatures;
    return this;
  }

  public Member addSignaturesItem(MemberSignatures signaturesItem) {
    this.signatures.add(signaturesItem);
    return this;
  }

   /**
   * Array of signatures
   * @return signatures
  **/
  public List<MemberSignatures> getSignatures() {
    return signatures;
  }

  public void setSignatures(List<MemberSignatures> signatures) {
    this.signatures = signatures;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Member _member = (Member) o;
    return Objects.equals(this.id, _member.id) &&
        Objects.equals(this.email, _member.email) &&
        Objects.equals(this.firstName, _member.firstName) &&
        Objects.equals(this.lastName, _member.lastName) &&
        Objects.equals(this.lastActivityAt, _member.lastActivityAt) &&
        Objects.equals(this.createdAt, _member.createdAt) &&
        Objects.equals(this.signed, _member.signed) &&
        Objects.equals(this.isAdmin, _member.isAdmin) &&
        Objects.equals(this.isSigner, _member.isSigner) &&
        Objects.equals(this.isApprover, _member.isApprover) &&
        Objects.equals(this.isViewer, _member.isViewer) &&
        Objects.equals(this.approvedAt, _member.approvedAt) &&
        Objects.equals(this.viewedAt, _member.viewedAt) &&
        Objects.equals(this.signaturePosition, _member.signaturePosition) &&
        Objects.equals(this.signedAt, _member.signedAt) &&
        Objects.equals(this.signingOrder, _member.signingOrder) &&
        Objects.equals(this.declinedAt, _member.declinedAt) &&
        Objects.equals(this.signatures, _member.signatures);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, email, firstName, lastName, lastActivityAt, createdAt, signed, isAdmin, isSigner, isApprover, isViewer, approvedAt, viewedAt, signaturePosition, signedAt, signingOrder, declinedAt, signatures);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Member {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
    sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
    sb.append("    lastActivityAt: ").append(toIndentedString(lastActivityAt)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    signed: ").append(toIndentedString(signed)).append("\n");
    sb.append("    isAdmin: ").append(toIndentedString(isAdmin)).append("\n");
    sb.append("    isSigner: ").append(toIndentedString(isSigner)).append("\n");
    sb.append("    isApprover: ").append(toIndentedString(isApprover)).append("\n");
    sb.append("    isViewer: ").append(toIndentedString(isViewer)).append("\n");
    sb.append("    approvedAt: ").append(toIndentedString(approvedAt)).append("\n");
    sb.append("    viewedAt: ").append(toIndentedString(viewedAt)).append("\n");
    sb.append("    signaturePosition: ").append(toIndentedString(signaturePosition)).append("\n");
    sb.append("    signedAt: ").append(toIndentedString(signedAt)).append("\n");
    sb.append("    signingOrder: ").append(toIndentedString(signingOrder)).append("\n");
    sb.append("    declinedAt: ").append(toIndentedString(declinedAt)).append("\n");
    sb.append("    signatures: ").append(toIndentedString(signatures)).append("\n");
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
