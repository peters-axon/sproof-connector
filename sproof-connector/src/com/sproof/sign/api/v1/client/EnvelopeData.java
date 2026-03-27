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
import com.sproof.sign.api.v1.client.DocumentData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
/**
 * Can create either one document or multiple documents in an envelope. If more than one document is sent, the folderName is required.
 */
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2026-03-25T13:40:51.024343900+01:00[Europe/Vienna]")

public class EnvelopeData {
  @JsonProperty("usePlaceholders")
  private Boolean usePlaceholders = null;

  @JsonProperty("inPersonSigning")
  private Boolean inPersonSigning = null;

  @JsonProperty("folderName")
  private String folderName = null;

  @JsonProperty("callbackUrl")
  private String callbackUrl = null;

  @JsonProperty("returnUrl")
  private String returnUrl = null;

  @JsonProperty("returnBtnText")
  private String returnBtnText = null;

  @JsonProperty("documentDataArray")
  private List<DocumentData> documentDataArray = new ArrayList<>();

  @JsonProperty("useFastlane")
  private Boolean useFastlane = null;

  @JsonProperty("fastlaneProfileId")
  private UUID fastlaneProfileId = null;

  public EnvelopeData usePlaceholders(Boolean usePlaceholders) {
    this.usePlaceholders = usePlaceholders;
    return this;
  }

   /**
   * If true, text placeholders/formFields stored in the document   wil be used for inviting recipients.
   * @return usePlaceholders
  **/
  public Boolean isUsePlaceholders() {
    return usePlaceholders;
  }

  public void setUsePlaceholders(Boolean usePlaceholders) {
    this.usePlaceholders = usePlaceholders;
  }

  public EnvelopeData inPersonSigning(Boolean inPersonSigning) {
    this.inPersonSigning = inPersonSigning;
    return this;
  }

   /**
   * When true, the recipient gets a popup where he needs to manually draw a signature
   * @return inPersonSigning
  **/
  public Boolean isInPersonSigning() {
    return inPersonSigning;
  }

  public void setInPersonSigning(Boolean inPersonSigning) {
    this.inPersonSigning = inPersonSigning;
  }

  public EnvelopeData folderName(String folderName) {
    this.folderName = folderName;
    return this;
  }

   /**
   * If given, an envelope will be created containing all documents in documentDataArray. (Required if more than one document is sent)
   * @return folderName
  **/
  public String getFolderName() {
    return folderName;
  }

  public void setFolderName(String folderName) {
    this.folderName = folderName;
  }

  public EnvelopeData callbackUrl(String callbackUrl) {
    this.callbackUrl = callbackUrl;
    return this;
  }

   /**
   * A POST request, including the document, will be sent to this URL when a new signature is created or when a recipient declines the document. In case of failure, a retry mechanism is in place to ensure the request is retried.
   * @return callbackUrl
  **/
  public String getCallbackUrl() {
    return callbackUrl;
  }

  public void setCallbackUrl(String callbackUrl) {
    this.callbackUrl = callbackUrl;
  }

  public EnvelopeData returnUrl(String returnUrl) {
    this.returnUrl = returnUrl;
    return this;
  }

   /**
   * The link of the return button after signing a document
   * @return returnUrl
  **/
  public String getReturnUrl() {
    return returnUrl;
  }

  public void setReturnUrl(String returnUrl) {
    this.returnUrl = returnUrl;
  }

  public EnvelopeData returnBtnText(String returnBtnText) {
    this.returnBtnText = returnBtnText;
    return this;
  }

   /**
   * The text of the return button after signing a document
   * @return returnBtnText
  **/
  public String getReturnBtnText() {
    return returnBtnText;
  }

  public void setReturnBtnText(String returnBtnText) {
    this.returnBtnText = returnBtnText;
  }

  public EnvelopeData documentDataArray(List<DocumentData> documentDataArray) {
    this.documentDataArray = documentDataArray;
    return this;
  }

  public EnvelopeData addDocumentDataArrayItem(DocumentData documentDataArrayItem) {
    this.documentDataArray.add(documentDataArrayItem);
    return this;
  }

   /**
   * Array of the data of each document, if more than one document is sent, the folderName is required.
   * @return documentDataArray
  **/
  public List<DocumentData> getDocumentDataArray() {
    return documentDataArray;
  }

  public void setDocumentDataArray(List<DocumentData> documentDataArray) {
    this.documentDataArray = documentDataArray;
  }

  public EnvelopeData useFastlane(Boolean useFastlane) {
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

  public EnvelopeData fastlaneProfileId(UUID fastlaneProfileId) {
    this.fastlaneProfileId = fastlaneProfileId;
    return this;
  }

   /**
   * Specify the Fastlane profile which should be used with this document. (useFastlane must be set to true).
   * @return fastlaneProfileId
  **/
  public UUID getFastlaneProfileId() {
    return fastlaneProfileId;
  }

  public void setFastlaneProfileId(UUID fastlaneProfileId) {
    this.fastlaneProfileId = fastlaneProfileId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EnvelopeData envelopeData = (EnvelopeData) o;
    return Objects.equals(this.usePlaceholders, envelopeData.usePlaceholders) &&
        Objects.equals(this.inPersonSigning, envelopeData.inPersonSigning) &&
        Objects.equals(this.folderName, envelopeData.folderName) &&
        Objects.equals(this.callbackUrl, envelopeData.callbackUrl) &&
        Objects.equals(this.returnUrl, envelopeData.returnUrl) &&
        Objects.equals(this.returnBtnText, envelopeData.returnBtnText) &&
        Objects.equals(this.documentDataArray, envelopeData.documentDataArray) &&
        Objects.equals(this.useFastlane, envelopeData.useFastlane) &&
        Objects.equals(this.fastlaneProfileId, envelopeData.fastlaneProfileId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(usePlaceholders, inPersonSigning, folderName, callbackUrl, returnUrl, returnBtnText, documentDataArray, useFastlane, fastlaneProfileId);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EnvelopeData {\n");
    
    sb.append("    usePlaceholders: ").append(toIndentedString(usePlaceholders)).append("\n");
    sb.append("    inPersonSigning: ").append(toIndentedString(inPersonSigning)).append("\n");
    sb.append("    folderName: ").append(toIndentedString(folderName)).append("\n");
    sb.append("    callbackUrl: ").append(toIndentedString(callbackUrl)).append("\n");
    sb.append("    returnUrl: ").append(toIndentedString(returnUrl)).append("\n");
    sb.append("    returnBtnText: ").append(toIndentedString(returnBtnText)).append("\n");
    sb.append("    documentDataArray: ").append(toIndentedString(documentDataArray)).append("\n");
    sb.append("    useFastlane: ").append(toIndentedString(useFastlane)).append("\n");
    sb.append("    fastlaneProfileId: ").append(toIndentedString(fastlaneProfileId)).append("\n");
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
