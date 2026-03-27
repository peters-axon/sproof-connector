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
import com.sproof.sign.api.v1.client.CollabSigningMode;
import com.sproof.sign.api.v1.client.Recipient;
import com.sproof.sign.api.v1.client.Sender;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
/**
 * Data relevant for the invitation
 */
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2026-03-25T13:40:51.024343900+01:00[Europe/Vienna]")

public class InviteData {
  @JsonProperty("sender")
  private Sender sender = null;

  @JsonProperty("recipients")
  private List<Recipient> recipients = new ArrayList<>();

  @JsonProperty("dueDate")
  private String dueDate = null;

  @JsonProperty("reminderInterval")
  private BigDecimal reminderInterval = null;

  @JsonProperty("startSendingReminders")
  private String startSendingReminders = null;

  @JsonProperty("inviteLanguage")
  private String inviteLanguage = "en";

  @JsonProperty("message")
  private String message = null;

  @JsonProperty("subject")
  private String subject = null;

  @JsonProperty("sendOutFinishedPdf")
  private Boolean sendOutFinishedPdf = null;

  @JsonProperty("focusedSigningMode")
  private Boolean focusedSigningMode = false;

  @JsonProperty("allowForwarding")
  private Boolean allowForwarding = null;

  @JsonProperty("collabSigningMode")
  private CollabSigningMode collabSigningMode = null;

  public InviteData sender(Sender sender) {
    this.sender = sender;
    return this;
  }

   /**
   * Get sender
   * @return sender
  **/
  public Sender getSender() {
    return sender;
  }

  public void setSender(Sender sender) {
    this.sender = sender;
  }

  public InviteData recipients(List<Recipient> recipients) {
    this.recipients = recipients;
    return this;
  }

  public InviteData addRecipientsItem(Recipient recipientsItem) {
    this.recipients.add(recipientsItem);
    return this;
  }

   /**
   * List of recipients **without the sender of the document.**
   * @return recipients
  **/
  public List<Recipient> getRecipients() {
    return recipients;
  }

  public void setRecipients(List<Recipient> recipients) {
    this.recipients = recipients;
  }

  public InviteData dueDate(String dueDate) {
    this.dueDate = dueDate;
    return this;
  }

   /**
   * The date until which the document can be signed. This due date is displayed to the recipient in the invite email and is also visible in the document details section of the dashboard. It serves as a deadline for completing the signing process.
   * @return dueDate
  **/
  public String getDueDate() {
    return dueDate;
  }

  public void setDueDate(String dueDate) {
    this.dueDate = dueDate;
  }

  public InviteData reminderInterval(BigDecimal reminderInterval) {
    this.reminderInterval = reminderInterval;
    return this;
  }

   /**
   * Specifies the number of days between reminder emails.
   * @return reminderInterval
  **/
  public BigDecimal getReminderInterval() {
    return reminderInterval;
  }

  public void setReminderInterval(BigDecimal reminderInterval) {
    this.reminderInterval = reminderInterval;
  }

  public InviteData startSendingReminders(String startSendingReminders) {
    this.startSendingReminders = startSendingReminders;
    return this;
  }

   /**
   * Date from which reminders should be sent out
   * @return startSendingReminders
  **/
  public String getStartSendingReminders() {
    return startSendingReminders;
  }

  public void setStartSendingReminders(String startSendingReminders) {
    this.startSendingReminders = startSendingReminders;
  }

  public InviteData inviteLanguage(String inviteLanguage) {
    this.inviteLanguage = inviteLanguage;
    return this;
  }

   /**
   * Specifies the language of the emails sent to anonymous users who do not have an account. For users with an account, the language they have selected in their profile will be used instead.
   * @return inviteLanguage
  **/
  public String getInviteLanguage() {
    return inviteLanguage;
  }

  public void setInviteLanguage(String inviteLanguage) {
    this.inviteLanguage = inviteLanguage;
  }

  public InviteData message(String message) {
    this.message = message;
    return this;
  }

   /**
   * A custom message sent to the recipients within the email.
   * @return message
  **/
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public InviteData subject(String subject) {
    this.subject = subject;
    return this;
  }

   /**
   * The subject line of the email sent to the recipients.
   * @return subject
  **/
  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public InviteData sendOutFinishedPdf(Boolean sendOutFinishedPdf) {
    this.sendOutFinishedPdf = sendOutFinishedPdf;
    return this;
  }

   /**
   * If set to true, the pdf is sent as an attachment to all recipients on the document (If it is smaller than 5MB).
   * @return sendOutFinishedPdf
  **/
  public Boolean isSendOutFinishedPdf() {
    return sendOutFinishedPdf;
  }

  public void setSendOutFinishedPdf(Boolean sendOutFinishedPdf) {
    this.sendOutFinishedPdf = sendOutFinishedPdf;
  }

  public InviteData focusedSigningMode(Boolean focusedSigningMode) {
    this.focusedSigningMode = focusedSigningMode;
    return this;
  }

   /**
   * When this option is enabled, the recipient of the signature request will be limited to signing or declining the document, and will not be able to navigate away from the page.
   * @return focusedSigningMode
  **/
  public Boolean isFocusedSigningMode() {
    return focusedSigningMode;
  }

  public void setFocusedSigningMode(Boolean focusedSigningMode) {
    this.focusedSigningMode = focusedSigningMode;
  }

  public InviteData allowForwarding(Boolean allowForwarding) {
    this.allowForwarding = allowForwarding;
    return this;
  }

   /**
   * Allow forwarding of the invite.
   * @return allowForwarding
  **/
  public Boolean isAllowForwarding() {
    return allowForwarding;
  }

  public void setAllowForwarding(Boolean allowForwarding) {
    this.allowForwarding = allowForwarding;
  }

  public InviteData collabSigningMode(CollabSigningMode collabSigningMode) {
    this.collabSigningMode = collabSigningMode;
    return this;
  }

   /**
   * Get collabSigningMode
   * @return collabSigningMode
  **/
  public CollabSigningMode getCollabSigningMode() {
    return collabSigningMode;
  }

  public void setCollabSigningMode(CollabSigningMode collabSigningMode) {
    this.collabSigningMode = collabSigningMode;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InviteData inviteData = (InviteData) o;
    return Objects.equals(this.sender, inviteData.sender) &&
        Objects.equals(this.recipients, inviteData.recipients) &&
        Objects.equals(this.dueDate, inviteData.dueDate) &&
        Objects.equals(this.reminderInterval, inviteData.reminderInterval) &&
        Objects.equals(this.startSendingReminders, inviteData.startSendingReminders) &&
        Objects.equals(this.inviteLanguage, inviteData.inviteLanguage) &&
        Objects.equals(this.message, inviteData.message) &&
        Objects.equals(this.subject, inviteData.subject) &&
        Objects.equals(this.sendOutFinishedPdf, inviteData.sendOutFinishedPdf) &&
        Objects.equals(this.focusedSigningMode, inviteData.focusedSigningMode) &&
        Objects.equals(this.allowForwarding, inviteData.allowForwarding) &&
        Objects.equals(this.collabSigningMode, inviteData.collabSigningMode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sender, recipients, dueDate, reminderInterval, startSendingReminders, inviteLanguage, message, subject, sendOutFinishedPdf, focusedSigningMode, allowForwarding, collabSigningMode);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InviteData {\n");
    
    sb.append("    sender: ").append(toIndentedString(sender)).append("\n");
    sb.append("    recipients: ").append(toIndentedString(recipients)).append("\n");
    sb.append("    dueDate: ").append(toIndentedString(dueDate)).append("\n");
    sb.append("    reminderInterval: ").append(toIndentedString(reminderInterval)).append("\n");
    sb.append("    startSendingReminders: ").append(toIndentedString(startSendingReminders)).append("\n");
    sb.append("    inviteLanguage: ").append(toIndentedString(inviteLanguage)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    subject: ").append(toIndentedString(subject)).append("\n");
    sb.append("    sendOutFinishedPdf: ").append(toIndentedString(sendOutFinishedPdf)).append("\n");
    sb.append("    focusedSigningMode: ").append(toIndentedString(focusedSigningMode)).append("\n");
    sb.append("    allowForwarding: ").append(toIndentedString(allowForwarding)).append("\n");
    sb.append("    collabSigningMode: ").append(toIndentedString(collabSigningMode)).append("\n");
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
