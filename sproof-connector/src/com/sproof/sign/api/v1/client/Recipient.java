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
import com.sproof.sign.api.v1.client.RecipientDefaultProvider;

import java.util.ArrayList;
import java.util.List;
/**
 * Recipient
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2026-03-25T13:40:51.024343900+01:00[Europe/Vienna]")

public class Recipient {
  @JsonProperty("email")
  private String email = null;

  @JsonProperty("firstName")
  private String firstName = null;

  @JsonProperty("lastName")
  private String lastName = null;

  @JsonProperty("doNotSendEmails")
  private Boolean doNotSendEmails = null;

  @JsonProperty("customId")
  private String customId = null;

  @JsonProperty("useFastlane")
  private Boolean useFastlane = null;

  @JsonProperty("phoneNumber")
  private String phoneNumber = null;

  @JsonProperty("privateMessage")
  private String privateMessage = null;

  @JsonProperty("inPersonMode")
  private Boolean inPersonMode = false;

  @JsonProperty("signatureTypes")
  private List<String> signatureTypes = null;

  @JsonProperty("defaultProvider")
  private RecipientDefaultProvider defaultProvider = null;

  public Recipient email(String email) {
    this.email = email;
    return this;
  }

   /**
   * Email of the recipient.
   * @return email
  **/
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Recipient firstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

   /**
   * First name of the recipient.
   * @return firstName
  **/
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public Recipient lastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

   /**
   * Last name of the recipient.
   * @return lastName
  **/
  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public Recipient doNotSendEmails(Boolean doNotSendEmails) {
    this.doNotSendEmails = doNotSendEmails;
    return this;
  }

   /**
   * If set to &#x60;true&#x60; no email is sent to the recipient.
   * @return doNotSendEmails
  **/
  public Boolean isDoNotSendEmails() {
    return doNotSendEmails;
  }

  public void setDoNotSendEmails(Boolean doNotSendEmails) {
    this.doNotSendEmails = doNotSendEmails;
  }

  public Recipient customId(String customId) {
    this.customId = customId;
    return this;
  }

   /**
   * To be used only when the recipient&#x27;s email address is **unknown** at the time of inviting. This only works when you invite the recipients (not for prepare routes) and you want to use fastlane. The &#x60;customId&#x60; allows you to pre-generate a unique signing link for a recipient before sending an invitation. Must be a unique, alphanumeric string of at least 70 characters.    **Note**: The customId can only be used to build the recipient link to a document. It can not be used for other requests instead of the memberId. For details on URL construction, see [Opening a document with a Custom ID](#tag/fastlane/GET/fastlane/custom/{planId}/{customId})
   * @return customId
  **/
  public String getCustomId() {
    return customId;
  }

  public void setCustomId(String customId) {
    this.customId = customId;
  }

  public Recipient useFastlane(Boolean useFastlane) {
    this.useFastlane = useFastlane;
    return this;
  }

   /**
   * If set to &#x60;&#x60;true&#x60;&#x60; the signer receives a Fastlane link in the invitation email.
   * @return useFastlane
  **/
  public Boolean isUseFastlane() {
    return useFastlane;
  }

  public void setUseFastlane(Boolean useFastlane) {
    this.useFastlane = useFastlane;
  }

  public Recipient phoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
    return this;
  }

   /**
   * The phone number of this recipient. This is used for SMS verification. (eg: +43 699 1234 1234).
   * @return phoneNumber
  **/
  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public Recipient privateMessage(String privateMessage) {
    this.privateMessage = privateMessage;
    return this;
  }

   /**
   * A private message for this specific recipient.
   * @return privateMessage
  **/
  public String getPrivateMessage() {
    return privateMessage;
  }

  public void setPrivateMessage(String privateMessage) {
    this.privateMessage = privateMessage;
  }

  public Recipient inPersonMode(Boolean inPersonMode) {
    this.inPersonMode = inPersonMode;
    return this;
  }

   /**
   * When this option is enabled, the recipient of the signature request will be required to sign the document in person and no email is sent. This mode is only available using the SES signature type
   * @return inPersonMode
  **/
  public Boolean isInPersonMode() {
    return inPersonMode;
  }

  public void setInPersonMode(Boolean inPersonMode) {
    this.inPersonMode = inPersonMode;
  }

  public Recipient signatureTypes(List<String> signatureTypes) {
    this.signatureTypes = signatureTypes;
    return this;
  }

  public Recipient addSignatureTypesItem(String signatureTypesItem) {
    if (this.signatureTypes == null) {
      this.signatureTypes = new ArrayList<>();
    }
    this.signatureTypes.add(signatureTypesItem);
    return this;
  }

   /**
   * The types of signatures that are allowed for this recipient. Options: [&#x27;advanced&#x27;, &#x27;advancedPlus&#x27;, &#x27;qualified&#x27;, &#x27;qualifiedPlusIdent&#x27;, &#x27;simple&#x27;]
   * @return signatureTypes
  **/
  public List<String> getSignatureTypes() {
    return signatureTypes;
  }

  public void setSignatureTypes(List<String> signatureTypes) {
    this.signatureTypes = signatureTypes;
  }

  public Recipient defaultProvider(RecipientDefaultProvider defaultProvider) {
    this.defaultProvider = defaultProvider;
    return this;
  }

   /**
   * Get defaultProvider
   * @return defaultProvider
  **/
  public RecipientDefaultProvider getDefaultProvider() {
    return defaultProvider;
  }

  public void setDefaultProvider(RecipientDefaultProvider defaultProvider) {
    this.defaultProvider = defaultProvider;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Recipient recipient = (Recipient) o;
    return Objects.equals(this.email, recipient.email) &&
        Objects.equals(this.firstName, recipient.firstName) &&
        Objects.equals(this.lastName, recipient.lastName) &&
        Objects.equals(this.doNotSendEmails, recipient.doNotSendEmails) &&
        Objects.equals(this.customId, recipient.customId) &&
        Objects.equals(this.useFastlane, recipient.useFastlane) &&
        Objects.equals(this.phoneNumber, recipient.phoneNumber) &&
        Objects.equals(this.privateMessage, recipient.privateMessage) &&
        Objects.equals(this.inPersonMode, recipient.inPersonMode) &&
        Objects.equals(this.signatureTypes, recipient.signatureTypes) &&
        Objects.equals(this.defaultProvider, recipient.defaultProvider);
  }

  @Override
  public int hashCode() {
    return Objects.hash(email, firstName, lastName, doNotSendEmails, customId, useFastlane, phoneNumber, privateMessage, inPersonMode, signatureTypes, defaultProvider);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Recipient {\n");
    
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
    sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
    sb.append("    doNotSendEmails: ").append(toIndentedString(doNotSendEmails)).append("\n");
    sb.append("    customId: ").append(toIndentedString(customId)).append("\n");
    sb.append("    useFastlane: ").append(toIndentedString(useFastlane)).append("\n");
    sb.append("    phoneNumber: ").append(toIndentedString(phoneNumber)).append("\n");
    sb.append("    privateMessage: ").append(toIndentedString(privateMessage)).append("\n");
    sb.append("    inPersonMode: ").append(toIndentedString(inPersonMode)).append("\n");
    sb.append("    signatureTypes: ").append(toIndentedString(signatureTypes)).append("\n");
    sb.append("    defaultProvider: ").append(toIndentedString(defaultProvider)).append("\n");
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
