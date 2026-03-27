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
import com.sproof.sign.api.v1.client.IdentDataMainAddress;
import com.sproof.sign.api.v1.client.IdentDataPassportData;

import java.util.ArrayList;
import java.util.List;
/**
 * IdentData
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2026-03-25T13:40:51.024343900+01:00[Europe/Vienna]")

public class IdentData {
  @JsonProperty("given_name")
  private String givenName = null;

  @JsonProperty("family_name")
  private String familyName = null;

  @JsonProperty("birthdate")
  private String birthdate = null;

  @JsonProperty("main_address")
  private IdentDataMainAddress mainAddress = null;

  @JsonProperty("nationality")
  private List<String> nationality = null;

  @JsonProperty("age_over_14")
  private String ageOver14 = null;

  @JsonProperty("age_over_16")
  private String ageOver16 = null;

  @JsonProperty("age_over_18")
  private String ageOver18 = null;

  @JsonProperty("age_over_21")
  private String ageOver21 = null;

  @JsonProperty("signature_image")
  private String signatureImage = null;

  @JsonProperty("passport_data")
  private IdentDataPassportData passportData = null;

  @JsonProperty("passport_portrait")
  private String passportPortrait = null;

  @JsonProperty("selfie_image")
  private String selfieImage = null;

  @JsonProperty("id_document_image")
  private String idDocumentImage = null;

  @JsonProperty("id_document_pdf")
  private String idDocumentPdf = null;

  public IdentData givenName(String givenName) {
    this.givenName = givenName;
    return this;
  }

   /**
   * First/given name of the End User
   * @return givenName
  **/
  public String getGivenName() {
    return givenName;
  }

  public void setGivenName(String givenName) {
    this.givenName = givenName;
  }

  public IdentData familyName(String familyName) {
    this.familyName = familyName;
    return this;
  }

   /**
   * Last/family name of the End User
   * @return familyName
  **/
  public String getFamilyName() {
    return familyName;
  }

  public void setFamilyName(String familyName) {
    this.familyName = familyName;
  }

  public IdentData birthdate(String birthdate) {
    this.birthdate = birthdate;
    return this;
  }

   /**
   * Date of birth
   * @return birthdate
  **/
  public String getBirthdate() {
    return birthdate;
  }

  public void setBirthdate(String birthdate) {
    this.birthdate = birthdate;
  }

  public IdentData mainAddress(IdentDataMainAddress mainAddress) {
    this.mainAddress = mainAddress;
    return this;
  }

   /**
   * Get mainAddress
   * @return mainAddress
  **/
  public IdentDataMainAddress getMainAddress() {
    return mainAddress;
  }

  public void setMainAddress(IdentDataMainAddress mainAddress) {
    this.mainAddress = mainAddress;
  }

  public IdentData nationality(List<String> nationality) {
    this.nationality = nationality;
    return this;
  }

  public IdentData addNationalityItem(String nationalityItem) {
    if (this.nationality == null) {
      this.nationality = new ArrayList<>();
    }
    this.nationality.add(nationalityItem);
    return this;
  }

   /**
   * ISO 3166-1 alpha-3 country codes (e.g., [\&quot;AUT\&quot;, \&quot;DEU\&quot;])
   * @return nationality
  **/
  public List<String> getNationality() {
    return nationality;
  }

  public void setNationality(List<String> nationality) {
    this.nationality = nationality;
  }

  public IdentData ageOver14(String ageOver14) {
    this.ageOver14 = ageOver14;
    return this;
  }

   /**
   * \&quot;true\&quot; if End User is over 14, \&quot;false\&quot; otherwise
   * @return ageOver14
  **/
  public String getAgeOver14() {
    return ageOver14;
  }

  public void setAgeOver14(String ageOver14) {
    this.ageOver14 = ageOver14;
  }

  public IdentData ageOver16(String ageOver16) {
    this.ageOver16 = ageOver16;
    return this;
  }

   /**
   * \&quot;true\&quot; if End User is over 16, \&quot;false\&quot; otherwise
   * @return ageOver16
  **/
  public String getAgeOver16() {
    return ageOver16;
  }

  public void setAgeOver16(String ageOver16) {
    this.ageOver16 = ageOver16;
  }

  public IdentData ageOver18(String ageOver18) {
    this.ageOver18 = ageOver18;
    return this;
  }

   /**
   * \&quot;true\&quot; if End User is over 18, \&quot;false\&quot; otherwise
   * @return ageOver18
  **/
  public String getAgeOver18() {
    return ageOver18;
  }

  public void setAgeOver18(String ageOver18) {
    this.ageOver18 = ageOver18;
  }

  public IdentData ageOver21(String ageOver21) {
    this.ageOver21 = ageOver21;
    return this;
  }

   /**
   * \&quot;true\&quot; if End User is over 21, \&quot;false\&quot; otherwise
   * @return ageOver21
  **/
  public String getAgeOver21() {
    return ageOver21;
  }

  public void setAgeOver21(String ageOver21) {
    this.ageOver21 = ageOver21;
  }

  public IdentData signatureImage(String signatureImage) {
    this.signatureImage = signatureImage;
    return this;
  }

   /**
   * URI path to signature image - use media download endpoint
   * @return signatureImage
  **/
  public String getSignatureImage() {
    return signatureImage;
  }

  public void setSignatureImage(String signatureImage) {
    this.signatureImage = signatureImage;
  }

  public IdentData passportData(IdentDataPassportData passportData) {
    this.passportData = passportData;
    return this;
  }

   /**
   * Get passportData
   * @return passportData
  **/
  public IdentDataPassportData getPassportData() {
    return passportData;
  }

  public void setPassportData(IdentDataPassportData passportData) {
    this.passportData = passportData;
  }

  public IdentData passportPortrait(String passportPortrait) {
    this.passportPortrait = passportPortrait;
    return this;
  }

   /**
   * URI path to passport photo - use media download endpoint
   * @return passportPortrait
  **/
  public String getPassportPortrait() {
    return passportPortrait;
  }

  public void setPassportPortrait(String passportPortrait) {
    this.passportPortrait = passportPortrait;
  }

  public IdentData selfieImage(String selfieImage) {
    this.selfieImage = selfieImage;
    return this;
  }

   /**
   * URI path to selfie from liveness check - use media download endpoint
   * @return selfieImage
  **/
  public String getSelfieImage() {
    return selfieImage;
  }

  public void setSelfieImage(String selfieImage) {
    this.selfieImage = selfieImage;
  }

  public IdentData idDocumentImage(String idDocumentImage) {
    this.idDocumentImage = idDocumentImage;
    return this;
  }

   /**
   * URI path to full ID document scan - use media download endpoint
   * @return idDocumentImage
  **/
  public String getIdDocumentImage() {
    return idDocumentImage;
  }

  public void setIdDocumentImage(String idDocumentImage) {
    this.idDocumentImage = idDocumentImage;
  }

  public IdentData idDocumentPdf(String idDocumentPdf) {
    this.idDocumentPdf = idDocumentPdf;
    return this;
  }

   /**
   * URI path to ID document PDF report - use media download endpoint
   * @return idDocumentPdf
  **/
  public String getIdDocumentPdf() {
    return idDocumentPdf;
  }

  public void setIdDocumentPdf(String idDocumentPdf) {
    this.idDocumentPdf = idDocumentPdf;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IdentData identData = (IdentData) o;
    return Objects.equals(this.givenName, identData.givenName) &&
        Objects.equals(this.familyName, identData.familyName) &&
        Objects.equals(this.birthdate, identData.birthdate) &&
        Objects.equals(this.mainAddress, identData.mainAddress) &&
        Objects.equals(this.nationality, identData.nationality) &&
        Objects.equals(this.ageOver14, identData.ageOver14) &&
        Objects.equals(this.ageOver16, identData.ageOver16) &&
        Objects.equals(this.ageOver18, identData.ageOver18) &&
        Objects.equals(this.ageOver21, identData.ageOver21) &&
        Objects.equals(this.signatureImage, identData.signatureImage) &&
        Objects.equals(this.passportData, identData.passportData) &&
        Objects.equals(this.passportPortrait, identData.passportPortrait) &&
        Objects.equals(this.selfieImage, identData.selfieImage) &&
        Objects.equals(this.idDocumentImage, identData.idDocumentImage) &&
        Objects.equals(this.idDocumentPdf, identData.idDocumentPdf);
  }

  @Override
  public int hashCode() {
    return Objects.hash(givenName, familyName, birthdate, mainAddress, nationality, ageOver14, ageOver16, ageOver18, ageOver21, signatureImage, passportData, passportPortrait, selfieImage, idDocumentImage, idDocumentPdf);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IdentData {\n");
    
    sb.append("    givenName: ").append(toIndentedString(givenName)).append("\n");
    sb.append("    familyName: ").append(toIndentedString(familyName)).append("\n");
    sb.append("    birthdate: ").append(toIndentedString(birthdate)).append("\n");
    sb.append("    mainAddress: ").append(toIndentedString(mainAddress)).append("\n");
    sb.append("    nationality: ").append(toIndentedString(nationality)).append("\n");
    sb.append("    ageOver14: ").append(toIndentedString(ageOver14)).append("\n");
    sb.append("    ageOver16: ").append(toIndentedString(ageOver16)).append("\n");
    sb.append("    ageOver18: ").append(toIndentedString(ageOver18)).append("\n");
    sb.append("    ageOver21: ").append(toIndentedString(ageOver21)).append("\n");
    sb.append("    signatureImage: ").append(toIndentedString(signatureImage)).append("\n");
    sb.append("    passportData: ").append(toIndentedString(passportData)).append("\n");
    sb.append("    passportPortrait: ").append(toIndentedString(passportPortrait)).append("\n");
    sb.append("    selfieImage: ").append(toIndentedString(selfieImage)).append("\n");
    sb.append("    idDocumentImage: ").append(toIndentedString(idDocumentImage)).append("\n");
    sb.append("    idDocumentPdf: ").append(toIndentedString(idDocumentPdf)).append("\n");
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
