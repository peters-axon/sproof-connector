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
import com.sproof.sign.api.v1.client.AttributeEnum;
import com.sproof.sign.api.v1.client.IdentData;
import com.sproof.sign.api.v1.client.IdentResponseConfig;
import com.sproof.sign.api.v1.client.IdentTypeEnum;
import com.sproof.sign.api.v1.client.LanguageEnum;
import com.sproof.sign.api.v1.client.NationalProviderEnum;
import com.sproof.sign.api.v1.client.StatusEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
/**
 * IdentResponse
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2026-03-25T13:40:51.024343900+01:00[Europe/Vienna]")

public class IdentResponse {
  @JsonProperty("id")
  private UUID id = null;

  @JsonProperty("attributes")
  private List<AttributeEnum> attributes = new ArrayList<>();

  @JsonProperty("config")
  private IdentResponseConfig config = null;

  @JsonProperty("identTypes")
  private List<IdentTypeEnum> identTypes = new ArrayList<>();

  @JsonProperty("callback")
  private String callback = null;

  @JsonProperty("returnUrl")
  private String returnUrl = null;

  @JsonProperty("returnUrlLabel")
  private String returnUrlLabel = null;

  @JsonProperty("brandingDomain")
  private String brandingDomain = null;

  @JsonProperty("selectedNationalProvider")
  private NationalProviderEnum selectedNationalProvider = null;

  @JsonProperty("canChangeNationalProvider")
  private Boolean canChangeNationalProvider = true;

  @JsonProperty("metadata")
  private Map<String, Object> metadata = new HashMap<>();

  @JsonProperty("additionalChecks")
  private List<String> additionalChecks = new ArrayList<>();

  @JsonProperty("data")
  private IdentData data = null;

  @JsonProperty("status")
  private StatusEnum status = null;

  @JsonProperty("errorCode")
  private String errorCode = null;

  @JsonProperty("errorMessage")
  private String errorMessage = null;

  @JsonProperty("additionalChecks_data")
  private Object additionalChecksData = null;

  @JsonProperty("additionalChecks_error")
  private Object additionalChecksError = null;

  @JsonProperty("language")
  private LanguageEnum language = null;

  @JsonProperty("createdAt")
  private String createdAt = null;

  @JsonProperty("updatedAt")
  private String updatedAt = null;

  public IdentResponse id(UUID id) {
    this.id = id;
    return this;
  }

   /**
   * Get id
   * @return id
  **/
  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public IdentResponse attributes(List<AttributeEnum> attributes) {
    this.attributes = attributes;
    return this;
  }

  public IdentResponse addAttributesItem(AttributeEnum attributesItem) {
    this.attributes.add(attributesItem);
    return this;
  }

   /**
   * Get attributes
   * @return attributes
  **/
  public List<AttributeEnum> getAttributes() {
    return attributes;
  }

  public void setAttributes(List<AttributeEnum> attributes) {
    this.attributes = attributes;
  }

  public IdentResponse config(IdentResponseConfig config) {
    this.config = config;
    return this;
  }

   /**
   * Get config
   * @return config
  **/
  public IdentResponseConfig getConfig() {
    return config;
  }

  public void setConfig(IdentResponseConfig config) {
    this.config = config;
  }

  public IdentResponse identTypes(List<IdentTypeEnum> identTypes) {
    this.identTypes = identTypes;
    return this;
  }

  public IdentResponse addIdentTypesItem(IdentTypeEnum identTypesItem) {
    this.identTypes.add(identTypesItem);
    return this;
  }

   /**
   * Get identTypes
   * @return identTypes
  **/
  public List<IdentTypeEnum> getIdentTypes() {
    return identTypes;
  }

  public void setIdentTypes(List<IdentTypeEnum> identTypes) {
    this.identTypes = identTypes;
  }

  public IdentResponse callback(String callback) {
    this.callback = callback;
    return this;
  }

   /**
   * Get callback
   * @return callback
  **/
  public String getCallback() {
    return callback;
  }

  public void setCallback(String callback) {
    this.callback = callback;
  }

  public IdentResponse returnUrl(String returnUrl) {
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

  public IdentResponse returnUrlLabel(String returnUrlLabel) {
    this.returnUrlLabel = returnUrlLabel;
    return this;
  }

   /**
   * Get returnUrlLabel
   * @return returnUrlLabel
  **/
  public String getReturnUrlLabel() {
    return returnUrlLabel;
  }

  public void setReturnUrlLabel(String returnUrlLabel) {
    this.returnUrlLabel = returnUrlLabel;
  }

  public IdentResponse brandingDomain(String brandingDomain) {
    this.brandingDomain = brandingDomain;
    return this;
  }

   /**
   * Get brandingDomain
   * @return brandingDomain
  **/
  public String getBrandingDomain() {
    return brandingDomain;
  }

  public void setBrandingDomain(String brandingDomain) {
    this.brandingDomain = brandingDomain;
  }

  public IdentResponse selectedNationalProvider(NationalProviderEnum selectedNationalProvider) {
    this.selectedNationalProvider = selectedNationalProvider;
    return this;
  }

   /**
   * Get selectedNationalProvider
   * @return selectedNationalProvider
  **/
  public NationalProviderEnum getSelectedNationalProvider() {
    return selectedNationalProvider;
  }

  public void setSelectedNationalProvider(NationalProviderEnum selectedNationalProvider) {
    this.selectedNationalProvider = selectedNationalProvider;
  }

  public IdentResponse canChangeNationalProvider(Boolean canChangeNationalProvider) {
    this.canChangeNationalProvider = canChangeNationalProvider;
    return this;
  }

   /**
   * Get canChangeNationalProvider
   * @return canChangeNationalProvider
  **/
  public Boolean isCanChangeNationalProvider() {
    return canChangeNationalProvider;
  }

  public void setCanChangeNationalProvider(Boolean canChangeNationalProvider) {
    this.canChangeNationalProvider = canChangeNationalProvider;
  }

  public IdentResponse metadata(Map<String, Object> metadata) {
    this.metadata = metadata;
    return this;
  }

  public IdentResponse putMetadataItem(String key, Object metadataItem) {
    this.metadata.put(key, metadataItem);
    return this;
  }

   /**
   * Get metadata
   * @return metadata
  **/
  public Map<String, Object> getMetadata() {
    return metadata;
  }

  public void setMetadata(Map<String, Object> metadata) {
    this.metadata = metadata;
  }

  public IdentResponse additionalChecks(List<String> additionalChecks) {
    this.additionalChecks = additionalChecks;
    return this;
  }

  public IdentResponse addAdditionalChecksItem(String additionalChecksItem) {
    this.additionalChecks.add(additionalChecksItem);
    return this;
  }

   /**
   * Get additionalChecks
   * @return additionalChecks
  **/
  public List<String> getAdditionalChecks() {
    return additionalChecks;
  }

  public void setAdditionalChecks(List<String> additionalChecks) {
    this.additionalChecks = additionalChecks;
  }

  public IdentResponse data(IdentData data) {
    this.data = data;
    return this;
  }

   /**
   * Get data
   * @return data
  **/
  public IdentData getData() {
    return data;
  }

  public void setData(IdentData data) {
    this.data = data;
  }

  public IdentResponse status(StatusEnum status) {
    this.status = status;
    return this;
  }

   /**
   * Get status
   * @return status
  **/
  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public IdentResponse errorCode(String errorCode) {
    this.errorCode = errorCode;
    return this;
  }

   /**
   * Get errorCode
   * @return errorCode
  **/
  public String getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }

  public IdentResponse errorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
    return this;
  }

   /**
   * Get errorMessage
   * @return errorMessage
  **/
  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public IdentResponse additionalChecksData(Object additionalChecksData) {
    this.additionalChecksData = additionalChecksData;
    return this;
  }

   /**
   * Get additionalChecksData
   * @return additionalChecksData
  **/
  public Object getAdditionalChecksData() {
    return additionalChecksData;
  }

  public void setAdditionalChecksData(Object additionalChecksData) {
    this.additionalChecksData = additionalChecksData;
  }

  public IdentResponse additionalChecksError(Object additionalChecksError) {
    this.additionalChecksError = additionalChecksError;
    return this;
  }

   /**
   * Get additionalChecksError
   * @return additionalChecksError
  **/
  public Object getAdditionalChecksError() {
    return additionalChecksError;
  }

  public void setAdditionalChecksError(Object additionalChecksError) {
    this.additionalChecksError = additionalChecksError;
  }

  public IdentResponse language(LanguageEnum language) {
    this.language = language;
    return this;
  }

   /**
   * Get language
   * @return language
  **/
  public LanguageEnum getLanguage() {
    return language;
  }

  public void setLanguage(LanguageEnum language) {
    this.language = language;
  }

  public IdentResponse createdAt(String createdAt) {
    this.createdAt = createdAt;
    return this;
  }

   /**
   * Get createdAt
   * @return createdAt
  **/
  public String getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  public IdentResponse updatedAt(String updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

   /**
   * Get updatedAt
   * @return updatedAt
  **/
  public String getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(String updatedAt) {
    this.updatedAt = updatedAt;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IdentResponse identResponse = (IdentResponse) o;
    return Objects.equals(this.id, identResponse.id) &&
        Objects.equals(this.attributes, identResponse.attributes) &&
        Objects.equals(this.config, identResponse.config) &&
        Objects.equals(this.identTypes, identResponse.identTypes) &&
        Objects.equals(this.callback, identResponse.callback) &&
        Objects.equals(this.returnUrl, identResponse.returnUrl) &&
        Objects.equals(this.returnUrlLabel, identResponse.returnUrlLabel) &&
        Objects.equals(this.brandingDomain, identResponse.brandingDomain) &&
        Objects.equals(this.selectedNationalProvider, identResponse.selectedNationalProvider) &&
        Objects.equals(this.canChangeNationalProvider, identResponse.canChangeNationalProvider) &&
        Objects.equals(this.metadata, identResponse.metadata) &&
        Objects.equals(this.additionalChecks, identResponse.additionalChecks) &&
        Objects.equals(this.data, identResponse.data) &&
        Objects.equals(this.status, identResponse.status) &&
        Objects.equals(this.errorCode, identResponse.errorCode) &&
        Objects.equals(this.errorMessage, identResponse.errorMessage) &&
        Objects.equals(this.additionalChecksData, identResponse.additionalChecksData) &&
        Objects.equals(this.additionalChecksError, identResponse.additionalChecksError) &&
        Objects.equals(this.language, identResponse.language) &&
        Objects.equals(this.createdAt, identResponse.createdAt) &&
        Objects.equals(this.updatedAt, identResponse.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, attributes, config, identTypes, callback, returnUrl, returnUrlLabel, brandingDomain, selectedNationalProvider, canChangeNationalProvider, metadata, additionalChecks, data, status, errorCode, errorMessage, additionalChecksData, additionalChecksError, language, createdAt, updatedAt);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IdentResponse {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    attributes: ").append(toIndentedString(attributes)).append("\n");
    sb.append("    config: ").append(toIndentedString(config)).append("\n");
    sb.append("    identTypes: ").append(toIndentedString(identTypes)).append("\n");
    sb.append("    callback: ").append(toIndentedString(callback)).append("\n");
    sb.append("    returnUrl: ").append(toIndentedString(returnUrl)).append("\n");
    sb.append("    returnUrlLabel: ").append(toIndentedString(returnUrlLabel)).append("\n");
    sb.append("    brandingDomain: ").append(toIndentedString(brandingDomain)).append("\n");
    sb.append("    selectedNationalProvider: ").append(toIndentedString(selectedNationalProvider)).append("\n");
    sb.append("    canChangeNationalProvider: ").append(toIndentedString(canChangeNationalProvider)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
    sb.append("    additionalChecks: ").append(toIndentedString(additionalChecks)).append("\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    errorCode: ").append(toIndentedString(errorCode)).append("\n");
    sb.append("    errorMessage: ").append(toIndentedString(errorMessage)).append("\n");
    sb.append("    additionalChecksData: ").append(toIndentedString(additionalChecksData)).append("\n");
    sb.append("    additionalChecksError: ").append(toIndentedString(additionalChecksError)).append("\n");
    sb.append("    language: ").append(toIndentedString(language)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
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
