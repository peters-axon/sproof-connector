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
import com.sproof.sign.api.v1.client.Box;
import com.sproof.sign.api.v1.client.Signer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
/**
 * SignatureRequestBody
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2026-03-25T13:40:51.024343900+01:00[Europe/Vienna]")

public class SignatureRequestBody {
  @JsonProperty("token")
  private String token = null;

  @JsonProperty("data")
  private String data = null;

  /**
   * MIME type of the Base64-encoded file. Required for DOCX files.
   */
  public enum MimetypeEnum {
    PDF("application/pdf"),
    VND_OPENXMLFORMATS_OFFICEDOCUMENT_WORDPROCESSINGML_DOCUMENT("application/vnd.openxmlformats-officedocument.wordprocessingml.document");

    private String value;

    MimetypeEnum(String value) {
      this.value = value;
    }
    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    @JsonCreator
    public static MimetypeEnum fromValue(String input) {
      for (MimetypeEnum b : MimetypeEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }

  }  @JsonProperty("mimetype")
  private MimetypeEnum mimetype = MimetypeEnum.PDF;

  @JsonProperty("email")
  private String email = null;

  @JsonProperty("firstName")
  private String firstName = null;

  @JsonProperty("lastName")
  private String lastName = null;

  @JsonProperty("fileName")
  private String fileName = null;

  @JsonProperty("callbackUrl")
  private String callbackUrl = null;

  @JsonProperty("returnUrl")
  private String returnUrl = null;

  @JsonProperty("returnBtnText")
  private String returnBtnText = null;

  @JsonProperty("language")
  private String language = "en";

  @JsonProperty("signatureTypes")
  private List<String> signatureTypes = null;

  @JsonProperty("subject")
  private String subject = null;

  @JsonProperty("message")
  private String message = null;

  @JsonProperty("dueDate")
  private String dueDate = null;

  @JsonProperty("focusedSigningMode")
  private Boolean focusedSigningMode = false;

  @JsonProperty("usePlaceholders")
  private Boolean usePlaceholders = false;

  @JsonProperty("boxes")
  private List<Box> boxes = null;

  @JsonProperty("isMailMerge")
  private Boolean isMailMerge = false;

  @JsonProperty("signers")
  private List<Signer> signers = null;

  @JsonProperty("label")
  private String label = null;

  @JsonProperty("refId")
  private String refId = null;

  @JsonProperty("useFastlane")
  private Boolean useFastlane = false;

  @JsonProperty("fastlaneProfileId")
  private UUID fastlaneProfileId = null;

  public SignatureRequestBody token(String token) {
    this.token = token;
    return this;
  }

   /**
   * Token for authentication (e.g. your API key)
   * @return token
  **/
  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public SignatureRequestBody data(String data) {
    this.data = data;
    return this;
  }

   /**
   * Base64 encoded file (PDF or DOCX). If the file is DOCX, mimetype must be set accordingly.
   * @return data
  **/
  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public SignatureRequestBody mimetype(MimetypeEnum mimetype) {
    this.mimetype = mimetype;
    return this;
  }

   /**
   * MIME type of the Base64-encoded file. Required for DOCX files.
   * @return mimetype
  **/
  public MimetypeEnum getMimetype() {
    return mimetype;
  }

  public void setMimetype(MimetypeEnum mimetype) {
    this.mimetype = mimetype;
  }

  public SignatureRequestBody email(String email) {
    this.email = email;
    return this;
  }

   /**
   * Email of the sender. It must belong to a member of your plan.
   * @return email
  **/
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public SignatureRequestBody firstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

   /**
   * First name of the sender
   * @return firstName
  **/
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public SignatureRequestBody lastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

   /**
   * Last name of the sender
   * @return lastName
  **/
  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public SignatureRequestBody fileName(String fileName) {
    this.fileName = fileName;
    return this;
  }

   /**
   * Name of the file
   * @return fileName
  **/
  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public SignatureRequestBody callbackUrl(String callbackUrl) {
    this.callbackUrl = callbackUrl;
    return this;
  }

   /**
   * A post request including the document will be sent to this Url when a new signature is created
   * @return callbackUrl
  **/
  public String getCallbackUrl() {
    return callbackUrl;
  }

  public void setCallbackUrl(String callbackUrl) {
    this.callbackUrl = callbackUrl;
  }

  public SignatureRequestBody returnUrl(String returnUrl) {
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

  public SignatureRequestBody returnBtnText(String returnBtnText) {
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

  public SignatureRequestBody language(String language) {
    this.language = language;
    return this;
  }

   /**
   * Specifies the language of the emails sent to anonymous users who do not have an account. For users with an account, the language they have selected in their profile will be used instead.
   * @return language
  **/
  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public SignatureRequestBody signatureTypes(List<String> signatureTypes) {
    this.signatureTypes = signatureTypes;
    return this;
  }

  public SignatureRequestBody addSignatureTypesItem(String signatureTypesItem) {
    if (this.signatureTypes == null) {
      this.signatureTypes = new ArrayList<>();
    }
    this.signatureTypes.add(signatureTypesItem);
    return this;
  }

   /**
   * This parameter accepts an array containing a single string that specifies the allowed signature type.  - Set to [\&quot;qualified\&quot;] to allow only qualified signatures. - Set to [\&quot;advanced\&quot;] to allow only advanced signatures. - If the array is left empty, all signature types are allowed. 
   * @return signatureTypes
  **/
  public List<String> getSignatureTypes() {
    return signatureTypes;
  }

  public void setSignatureTypes(List<String> signatureTypes) {
    this.signatureTypes = signatureTypes;
  }

  public SignatureRequestBody subject(String subject) {
    this.subject = subject;
    return this;
  }

   /**
   * The subject line of the email sent to the signers.
   * @return subject
  **/
  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public SignatureRequestBody message(String message) {
    this.message = message;
    return this;
  }

   /**
   * A custom message sent to the signers within the email
   * @return message
  **/
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public SignatureRequestBody dueDate(String dueDate) {
    this.dueDate = dueDate;
    return this;
  }

   /**
   * For historical reasons, this date marks the start of reminder email notifications. To precisely manage the actual due date—when the document must be signed—as well as the sendReminders and reminderInterval settings, we recommend using the new [createSignatureRequest](#operation/createSignatureRequest) feature
   * @return dueDate
  **/
  public String getDueDate() {
    return dueDate;
  }

  public void setDueDate(String dueDate) {
    this.dueDate = dueDate;
  }

  public SignatureRequestBody focusedSigningMode(Boolean focusedSigningMode) {
    this.focusedSigningMode = focusedSigningMode;
    return this;
  }

   /**
   * When this option is enabled, the recipient of the signature request will be limited to  signing or declining the document, and will not be able to navigate away from the page
   * @return focusedSigningMode
  **/
  public Boolean isFocusedSigningMode() {
    return focusedSigningMode;
  }

  public void setFocusedSigningMode(Boolean focusedSigningMode) {
    this.focusedSigningMode = focusedSigningMode;
  }

  public SignatureRequestBody usePlaceholders(Boolean usePlaceholders) {
    this.usePlaceholders = usePlaceholders;
    return this;
  }

   /**
   * If set we will use the placeholders stored in the document for inviting signers.
   * @return usePlaceholders
  **/
  public Boolean isUsePlaceholders() {
    return usePlaceholders;
  }

  public void setUsePlaceholders(Boolean usePlaceholders) {
    this.usePlaceholders = usePlaceholders;
  }

  public SignatureRequestBody boxes(List<Box> boxes) {
    this.boxes = boxes;
    return this;
  }

  public SignatureRequestBody addBoxesItem(Box boxesItem) {
    if (this.boxes == null) {
      this.boxes = new ArrayList<>();
    }
    this.boxes.add(boxesItem);
    return this;
  }

   /**
   * The boxes of the document.
   * @return boxes
  **/
  public List<Box> getBoxes() {
    return boxes;
  }

  public void setBoxes(List<Box> boxes) {
    this.boxes = boxes;
  }

  public SignatureRequestBody isMailMerge(Boolean isMailMerge) {
    this.isMailMerge = isMailMerge;
    return this;
  }

   /**
   * When set to &#x60;true&#x60;, the [mail merge document](https://www.sproof.com/en/help-resources/sproof-academy/send-out-mail-merges) included in the request will be automatically split and sent out to all detected recipients within the document.  The return value will be an array of document objects representing all successfully created and sent documents.
   * @return isMailMerge
  **/
  public Boolean isIsMailMerge() {
    return isMailMerge;
  }

  public void setIsMailMerge(Boolean isMailMerge) {
    this.isMailMerge = isMailMerge;
  }

  public SignatureRequestBody signers(List<Signer> signers) {
    this.signers = signers;
    return this;
  }

  public SignatureRequestBody addSignersItem(Signer signersItem) {
    if (this.signers == null) {
      this.signers = new ArrayList<>();
    }
    this.signers.add(signersItem);
    return this;
  }

   /**
   * List of signers **without the sender of the document.**
   * @return signers
  **/
  public List<Signer> getSigners() {
    return signers;
  }

  public void setSigners(List<Signer> signers) {
    this.signers = signers;
  }

  public SignatureRequestBody label(String label) {
    this.label = label;
    return this;
  }

   /**
   * A custom label that can be sent to organize documents.
   * @return label
  **/
  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public SignatureRequestBody refId(String refId) {
    this.refId = refId;
    return this;
  }

   /**
   * The refId is an optional field used to cross-reference the uploaded document. If provided, it allows you to assign a custom reference identifier to the document, which will be returned as part of the document object in future API responses. This can be useful for tracking or organizing documents by an external system or workflow. 
   * @return refId
  **/
  public String getRefId() {
    return refId;
  }

  public void setRefId(String refId) {
    this.refId = refId;
  }

  public SignatureRequestBody useFastlane(Boolean useFastlane) {
    this.useFastlane = useFastlane;
    return this;
  }

   /**
   * If set to true all signers receive a fastlane link to the document in the invitation email.
   * @return useFastlane
  **/
  public Boolean isUseFastlane() {
    return useFastlane;
  }

  public void setUseFastlane(Boolean useFastlane) {
    this.useFastlane = useFastlane;
  }

  public SignatureRequestBody fastlaneProfileId(UUID fastlaneProfileId) {
    this.fastlaneProfileId = fastlaneProfileId;
    return this;
  }

   /**
   * Specify the Fastlane profile which should be used with this document. useFastlane needs to be set to true.
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
    SignatureRequestBody _signatureRequestBody = (SignatureRequestBody) o;
    return Objects.equals(this.token, _signatureRequestBody.token) &&
        Objects.equals(this.data, _signatureRequestBody.data) &&
        Objects.equals(this.mimetype, _signatureRequestBody.mimetype) &&
        Objects.equals(this.email, _signatureRequestBody.email) &&
        Objects.equals(this.firstName, _signatureRequestBody.firstName) &&
        Objects.equals(this.lastName, _signatureRequestBody.lastName) &&
        Objects.equals(this.fileName, _signatureRequestBody.fileName) &&
        Objects.equals(this.callbackUrl, _signatureRequestBody.callbackUrl) &&
        Objects.equals(this.returnUrl, _signatureRequestBody.returnUrl) &&
        Objects.equals(this.returnBtnText, _signatureRequestBody.returnBtnText) &&
        Objects.equals(this.language, _signatureRequestBody.language) &&
        Objects.equals(this.signatureTypes, _signatureRequestBody.signatureTypes) &&
        Objects.equals(this.subject, _signatureRequestBody.subject) &&
        Objects.equals(this.message, _signatureRequestBody.message) &&
        Objects.equals(this.dueDate, _signatureRequestBody.dueDate) &&
        Objects.equals(this.focusedSigningMode, _signatureRequestBody.focusedSigningMode) &&
        Objects.equals(this.usePlaceholders, _signatureRequestBody.usePlaceholders) &&
        Objects.equals(this.boxes, _signatureRequestBody.boxes) &&
        Objects.equals(this.isMailMerge, _signatureRequestBody.isMailMerge) &&
        Objects.equals(this.signers, _signatureRequestBody.signers) &&
        Objects.equals(this.label, _signatureRequestBody.label) &&
        Objects.equals(this.refId, _signatureRequestBody.refId) &&
        Objects.equals(this.useFastlane, _signatureRequestBody.useFastlane) &&
        Objects.equals(this.fastlaneProfileId, _signatureRequestBody.fastlaneProfileId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(token, data, mimetype, email, firstName, lastName, fileName, callbackUrl, returnUrl, returnBtnText, language, signatureTypes, subject, message, dueDate, focusedSigningMode, usePlaceholders, boxes, isMailMerge, signers, label, refId, useFastlane, fastlaneProfileId);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SignatureRequestBody {\n");
    
    sb.append("    token: ").append(toIndentedString(token)).append("\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("    mimetype: ").append(toIndentedString(mimetype)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
    sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
    sb.append("    fileName: ").append(toIndentedString(fileName)).append("\n");
    sb.append("    callbackUrl: ").append(toIndentedString(callbackUrl)).append("\n");
    sb.append("    returnUrl: ").append(toIndentedString(returnUrl)).append("\n");
    sb.append("    returnBtnText: ").append(toIndentedString(returnBtnText)).append("\n");
    sb.append("    language: ").append(toIndentedString(language)).append("\n");
    sb.append("    signatureTypes: ").append(toIndentedString(signatureTypes)).append("\n");
    sb.append("    subject: ").append(toIndentedString(subject)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    dueDate: ").append(toIndentedString(dueDate)).append("\n");
    sb.append("    focusedSigningMode: ").append(toIndentedString(focusedSigningMode)).append("\n");
    sb.append("    usePlaceholders: ").append(toIndentedString(usePlaceholders)).append("\n");
    sb.append("    boxes: ").append(toIndentedString(boxes)).append("\n");
    sb.append("    isMailMerge: ").append(toIndentedString(isMailMerge)).append("\n");
    sb.append("    signers: ").append(toIndentedString(signers)).append("\n");
    sb.append("    label: ").append(toIndentedString(label)).append("\n");
    sb.append("    refId: ").append(toIndentedString(refId)).append("\n");
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
