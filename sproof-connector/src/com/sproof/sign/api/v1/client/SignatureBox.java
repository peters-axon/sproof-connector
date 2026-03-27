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

import java.math.BigDecimal;
/**
 * SignatureBox
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2026-03-25T13:40:51.024343900+01:00[Europe/Vienna]")

public class SignatureBox {
  @JsonProperty("page")
  private BigDecimal page = new BigDecimal(0);

  @JsonProperty("pageWidth")
  private BigDecimal pageWidth = null;

  @JsonProperty("pageHeight")
  private BigDecimal pageHeight = null;

  @JsonProperty("type")
  private String type = null;

  @JsonProperty("maxWidht")
  private BigDecimal maxWidht = null;

  @JsonProperty("minWidht")
  private BigDecimal minWidht = null;

  @JsonProperty("maxHeight")
  private BigDecimal maxHeight = null;

  @JsonProperty("minHeight")
  private BigDecimal minHeight = null;

  @JsonProperty("x")
  private BigDecimal x = null;

  @JsonProperty("y")
  private BigDecimal y = null;

  @JsonProperty("width")
  private BigDecimal width = null;

  @JsonProperty("height")
  private BigDecimal height = null;

  @JsonProperty("resizeable")
  private Boolean resizeable = null;

  @JsonProperty("styleId")
  private String styleId = null;

  @JsonProperty("keepRatio")
  private Boolean keepRatio = null;

  @JsonProperty("selectedSigningOption")
  private String selectedSigningOption = null;

  @JsonProperty("value")
  private String value = null;

  @JsonProperty("moveable")
  private Boolean moveable = null;

  @JsonProperty("signatureType")
  private String signatureType = null;

  @JsonProperty("fdaConfig")
  private Object fdaConfig = null;

  public SignatureBox page(BigDecimal page) {
    this.page = page;
    return this;
  }

   /**
   * Page number, starting by 0
   * @return page
  **/
  public BigDecimal getPage() {
    return page;
  }

  public void setPage(BigDecimal page) {
    this.page = page;
  }

  public SignatureBox pageWidth(BigDecimal pageWidth) {
    this.pageWidth = pageWidth;
    return this;
  }

   /**
   * Get pageWidth
   * @return pageWidth
  **/
  public BigDecimal getPageWidth() {
    return pageWidth;
  }

  public void setPageWidth(BigDecimal pageWidth) {
    this.pageWidth = pageWidth;
  }

  public SignatureBox pageHeight(BigDecimal pageHeight) {
    this.pageHeight = pageHeight;
    return this;
  }

   /**
   * Get pageHeight
   * @return pageHeight
  **/
  public BigDecimal getPageHeight() {
    return pageHeight;
  }

  public void setPageHeight(BigDecimal pageHeight) {
    this.pageHeight = pageHeight;
  }

  public SignatureBox type(String type) {
    this.type = type;
    return this;
  }

   /**
   * Get type
   * @return type
  **/
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public SignatureBox maxWidht(BigDecimal maxWidht) {
    this.maxWidht = maxWidht;
    return this;
  }

   /**
   * Get maxWidht
   * @return maxWidht
  **/
  public BigDecimal getMaxWidht() {
    return maxWidht;
  }

  public void setMaxWidht(BigDecimal maxWidht) {
    this.maxWidht = maxWidht;
  }

  public SignatureBox minWidht(BigDecimal minWidht) {
    this.minWidht = minWidht;
    return this;
  }

   /**
   * Get minWidht
   * @return minWidht
  **/
  public BigDecimal getMinWidht() {
    return minWidht;
  }

  public void setMinWidht(BigDecimal minWidht) {
    this.minWidht = minWidht;
  }

  public SignatureBox maxHeight(BigDecimal maxHeight) {
    this.maxHeight = maxHeight;
    return this;
  }

   /**
   * Get maxHeight
   * @return maxHeight
  **/
  public BigDecimal getMaxHeight() {
    return maxHeight;
  }

  public void setMaxHeight(BigDecimal maxHeight) {
    this.maxHeight = maxHeight;
  }

  public SignatureBox minHeight(BigDecimal minHeight) {
    this.minHeight = minHeight;
    return this;
  }

   /**
   * Get minHeight
   * @return minHeight
  **/
  public BigDecimal getMinHeight() {
    return minHeight;
  }

  public void setMinHeight(BigDecimal minHeight) {
    this.minHeight = minHeight;
  }

  public SignatureBox x(BigDecimal x) {
    this.x = x;
    return this;
  }

   /**
   * x coordinate of the signatures&#x27; top left position
   * minimum: 0
   * maximum: 1
   * @return x
  **/
  public BigDecimal getX() {
    return x;
  }

  public void setX(BigDecimal x) {
    this.x = x;
  }

  public SignatureBox y(BigDecimal y) {
    this.y = y;
    return this;
  }

   /**
   * y coordinate of the signatures&#x27; top left position
   * minimum: 0
   * maximum: 1
   * @return y
  **/
  public BigDecimal getY() {
    return y;
  }

  public void setY(BigDecimal y) {
    this.y = y;
  }

  public SignatureBox width(BigDecimal width) {
    this.width = width;
    return this;
  }

   /**
   * width of the signature
   * minimum: 0
   * maximum: 1
   * @return width
  **/
  public BigDecimal getWidth() {
    return width;
  }

  public void setWidth(BigDecimal width) {
    this.width = width;
  }

  public SignatureBox height(BigDecimal height) {
    this.height = height;
    return this;
  }

   /**
   * height of the signature
   * minimum: 0
   * maximum: 1
   * @return height
  **/
  public BigDecimal getHeight() {
    return height;
  }

  public void setHeight(BigDecimal height) {
    this.height = height;
  }

  public SignatureBox resizeable(Boolean resizeable) {
    this.resizeable = resizeable;
    return this;
  }

   /**
   * Get resizeable
   * @return resizeable
  **/
  public Boolean isResizeable() {
    return resizeable;
  }

  public void setResizeable(Boolean resizeable) {
    this.resizeable = resizeable;
  }

  public SignatureBox styleId(String styleId) {
    this.styleId = styleId;
    return this;
  }

   /**
   * Get styleId
   * @return styleId
  **/
  public String getStyleId() {
    return styleId;
  }

  public void setStyleId(String styleId) {
    this.styleId = styleId;
  }

  public SignatureBox keepRatio(Boolean keepRatio) {
    this.keepRatio = keepRatio;
    return this;
  }

   /**
   * Get keepRatio
   * @return keepRatio
  **/
  public Boolean isKeepRatio() {
    return keepRatio;
  }

  public void setKeepRatio(Boolean keepRatio) {
    this.keepRatio = keepRatio;
  }

  public SignatureBox selectedSigningOption(String selectedSigningOption) {
    this.selectedSigningOption = selectedSigningOption;
    return this;
  }

   /**
   * Get selectedSigningOption
   * @return selectedSigningOption
  **/
  public String getSelectedSigningOption() {
    return selectedSigningOption;
  }

  public void setSelectedSigningOption(String selectedSigningOption) {
    this.selectedSigningOption = selectedSigningOption;
  }

  public SignatureBox value(String value) {
    this.value = value;
    return this;
  }

   /**
   * Base64 Encoded PNG File which represents the signature or seal
   * @return value
  **/
  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public SignatureBox moveable(Boolean moveable) {
    this.moveable = moveable;
    return this;
  }

   /**
   * Get moveable
   * @return moveable
  **/
  public Boolean isMoveable() {
    return moveable;
  }

  public void setMoveable(Boolean moveable) {
    this.moveable = moveable;
  }

  public SignatureBox signatureType(String signatureType) {
    this.signatureType = signatureType;
    return this;
  }

   /**
   * Signature type
   * @return signatureType
  **/
  public String getSignatureType() {
    return signatureType;
  }

  public void setSignatureType(String signatureType) {
    this.signatureType = signatureType;
  }

  public SignatureBox fdaConfig(Object fdaConfig) {
    this.fdaConfig = fdaConfig;
    return this;
  }

   /**
   * Get fdaConfig
   * @return fdaConfig
  **/
  public Object getFdaConfig() {
    return fdaConfig;
  }

  public void setFdaConfig(Object fdaConfig) {
    this.fdaConfig = fdaConfig;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SignatureBox _signatureBox = (SignatureBox) o;
    return Objects.equals(this.page, _signatureBox.page) &&
        Objects.equals(this.pageWidth, _signatureBox.pageWidth) &&
        Objects.equals(this.pageHeight, _signatureBox.pageHeight) &&
        Objects.equals(this.type, _signatureBox.type) &&
        Objects.equals(this.maxWidht, _signatureBox.maxWidht) &&
        Objects.equals(this.minWidht, _signatureBox.minWidht) &&
        Objects.equals(this.maxHeight, _signatureBox.maxHeight) &&
        Objects.equals(this.minHeight, _signatureBox.minHeight) &&
        Objects.equals(this.x, _signatureBox.x) &&
        Objects.equals(this.y, _signatureBox.y) &&
        Objects.equals(this.width, _signatureBox.width) &&
        Objects.equals(this.height, _signatureBox.height) &&
        Objects.equals(this.resizeable, _signatureBox.resizeable) &&
        Objects.equals(this.styleId, _signatureBox.styleId) &&
        Objects.equals(this.keepRatio, _signatureBox.keepRatio) &&
        Objects.equals(this.selectedSigningOption, _signatureBox.selectedSigningOption) &&
        Objects.equals(this.value, _signatureBox.value) &&
        Objects.equals(this.moveable, _signatureBox.moveable) &&
        Objects.equals(this.signatureType, _signatureBox.signatureType) &&
        Objects.equals(this.fdaConfig, _signatureBox.fdaConfig);
  }

  @Override
  public int hashCode() {
    return Objects.hash(page, pageWidth, pageHeight, type, maxWidht, minWidht, maxHeight, minHeight, x, y, width, height, resizeable, styleId, keepRatio, selectedSigningOption, value, moveable, signatureType, fdaConfig);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SignatureBox {\n");
    
    sb.append("    page: ").append(toIndentedString(page)).append("\n");
    sb.append("    pageWidth: ").append(toIndentedString(pageWidth)).append("\n");
    sb.append("    pageHeight: ").append(toIndentedString(pageHeight)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    maxWidht: ").append(toIndentedString(maxWidht)).append("\n");
    sb.append("    minWidht: ").append(toIndentedString(minWidht)).append("\n");
    sb.append("    maxHeight: ").append(toIndentedString(maxHeight)).append("\n");
    sb.append("    minHeight: ").append(toIndentedString(minHeight)).append("\n");
    sb.append("    x: ").append(toIndentedString(x)).append("\n");
    sb.append("    y: ").append(toIndentedString(y)).append("\n");
    sb.append("    width: ").append(toIndentedString(width)).append("\n");
    sb.append("    height: ").append(toIndentedString(height)).append("\n");
    sb.append("    resizeable: ").append(toIndentedString(resizeable)).append("\n");
    sb.append("    styleId: ").append(toIndentedString(styleId)).append("\n");
    sb.append("    keepRatio: ").append(toIndentedString(keepRatio)).append("\n");
    sb.append("    selectedSigningOption: ").append(toIndentedString(selectedSigningOption)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
    sb.append("    moveable: ").append(toIndentedString(moveable)).append("\n");
    sb.append("    signatureType: ").append(toIndentedString(signatureType)).append("\n");
    sb.append("    fdaConfig: ").append(toIndentedString(fdaConfig)).append("\n");
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
