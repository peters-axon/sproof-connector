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
import com.sproof.sign.api.v1.client.Document;

import java.util.ArrayList;
import java.util.List;
/**
 * DocumentResponse
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2026-03-25T13:40:51.024343900+01:00[Europe/Vienna]")

public class DocumentResponse {
  @JsonProperty("data")
  private List<Document> data = null;

  @JsonProperty("pagination")
  private AllOfDocumentResponsePagination pagination = null;

  @JsonProperty("allDocuments")
  private Integer allDocuments = null;

  @JsonProperty("envelopeDocuments")
  private Integer envelopeDocuments = null;

  @JsonProperty("createdDocuments")
  private Integer createdDocuments = null;

  @JsonProperty("createdIncompleteDocuments")
  private Integer createdIncompleteDocuments = null;

  @JsonProperty("incompleteDocuments")
  private Integer incompleteDocuments = null;

  @JsonProperty("draftDocuments")
  private Integer draftDocuments = null;

  @JsonProperty("invitedDocuments")
  private Integer invitedDocuments = null;

  @JsonProperty("declinedDocuments")
  private Integer declinedDocuments = null;

  @JsonProperty("pendingInvitations")
  private Integer pendingInvitations = null;

  @JsonProperty("overdueDocuments")
  private Integer overdueDocuments = null;

  @JsonProperty("openInvitedDocuments")
  private Integer openInvitedDocuments = null;

  @JsonProperty("openInvitedToApproveDocuments")
  private Integer openInvitedToApproveDocuments = null;

  @JsonProperty("openInvitedToViewDocuments")
  private Integer openInvitedToViewDocuments = null;

  @JsonProperty("openInvitedToSignDocuments")
  private Integer openInvitedToSignDocuments = null;

  @JsonProperty("openPositionedDocuments")
  private Integer openPositionedDocuments = null;

  @JsonProperty("stackedDocuments")
  private Integer stackedDocuments = null;

  @JsonProperty("completedDocuments")
  private Integer completedDocuments = null;

  public DocumentResponse data(List<Document> data) {
    this.data = data;
    return this;
  }

  public DocumentResponse addDataItem(Document dataItem) {
    if (this.data == null) {
      this.data = new ArrayList<>();
    }
    this.data.add(dataItem);
    return this;
  }

   /**
   * List of documents
   * @return data
  **/
  public List<Document> getData() {
    return data;
  }

  public void setData(List<Document> data) {
    this.data = data;
  }

  public DocumentResponse pagination(AllOfDocumentResponsePagination pagination) {
    this.pagination = pagination;
    return this;
  }

   /**
   * Get pagination
   * @return pagination
  **/
  public AllOfDocumentResponsePagination getPagination() {
    return pagination;
  }

  public void setPagination(AllOfDocumentResponsePagination pagination) {
    this.pagination = pagination;
  }

  public DocumentResponse allDocuments(Integer allDocuments) {
    this.allDocuments = allDocuments;
    return this;
  }

   /**
   * Number of all documents
   * @return allDocuments
  **/
  public Integer getAllDocuments() {
    return allDocuments;
  }

  public void setAllDocuments(Integer allDocuments) {
    this.allDocuments = allDocuments;
  }

  public DocumentResponse envelopeDocuments(Integer envelopeDocuments) {
    this.envelopeDocuments = envelopeDocuments;
    return this;
  }

   /**
   * Number of documents in folders
   * @return envelopeDocuments
  **/
  public Integer getEnvelopeDocuments() {
    return envelopeDocuments;
  }

  public void setEnvelopeDocuments(Integer envelopeDocuments) {
    this.envelopeDocuments = envelopeDocuments;
  }

  public DocumentResponse createdDocuments(Integer createdDocuments) {
    this.createdDocuments = createdDocuments;
    return this;
  }

   /**
   * Number of created documents
   * @return createdDocuments
  **/
  public Integer getCreatedDocuments() {
    return createdDocuments;
  }

  public void setCreatedDocuments(Integer createdDocuments) {
    this.createdDocuments = createdDocuments;
  }

  public DocumentResponse createdIncompleteDocuments(Integer createdIncompleteDocuments) {
    this.createdIncompleteDocuments = createdIncompleteDocuments;
    return this;
  }

   /**
   * Number of created incomplete documents
   * @return createdIncompleteDocuments
  **/
  public Integer getCreatedIncompleteDocuments() {
    return createdIncompleteDocuments;
  }

  public void setCreatedIncompleteDocuments(Integer createdIncompleteDocuments) {
    this.createdIncompleteDocuments = createdIncompleteDocuments;
  }

  public DocumentResponse incompleteDocuments(Integer incompleteDocuments) {
    this.incompleteDocuments = incompleteDocuments;
    return this;
  }

   /**
   * Number of total incomplete documents
   * @return incompleteDocuments
  **/
  public Integer getIncompleteDocuments() {
    return incompleteDocuments;
  }

  public void setIncompleteDocuments(Integer incompleteDocuments) {
    this.incompleteDocuments = incompleteDocuments;
  }

  public DocumentResponse draftDocuments(Integer draftDocuments) {
    this.draftDocuments = draftDocuments;
    return this;
  }

   /**
   * Number of drafts
   * @return draftDocuments
  **/
  public Integer getDraftDocuments() {
    return draftDocuments;
  }

  public void setDraftDocuments(Integer draftDocuments) {
    this.draftDocuments = draftDocuments;
  }

  public DocumentResponse invitedDocuments(Integer invitedDocuments) {
    this.invitedDocuments = invitedDocuments;
    return this;
  }

   /**
   * Number of invited documents
   * @return invitedDocuments
  **/
  public Integer getInvitedDocuments() {
    return invitedDocuments;
  }

  public void setInvitedDocuments(Integer invitedDocuments) {
    this.invitedDocuments = invitedDocuments;
  }

  public DocumentResponse declinedDocuments(Integer declinedDocuments) {
    this.declinedDocuments = declinedDocuments;
    return this;
  }

   /**
   * Number of declined documents
   * @return declinedDocuments
  **/
  public Integer getDeclinedDocuments() {
    return declinedDocuments;
  }

  public void setDeclinedDocuments(Integer declinedDocuments) {
    this.declinedDocuments = declinedDocuments;
  }

  public DocumentResponse pendingInvitations(Integer pendingInvitations) {
    this.pendingInvitations = pendingInvitations;
    return this;
  }

   /**
   * Number of pending invitations (not in signing round yet)
   * @return pendingInvitations
  **/
  public Integer getPendingInvitations() {
    return pendingInvitations;
  }

  public void setPendingInvitations(Integer pendingInvitations) {
    this.pendingInvitations = pendingInvitations;
  }

  public DocumentResponse overdueDocuments(Integer overdueDocuments) {
    this.overdueDocuments = overdueDocuments;
    return this;
  }

   /**
   * Number of overdue documents
   * @return overdueDocuments
  **/
  public Integer getOverdueDocuments() {
    return overdueDocuments;
  }

  public void setOverdueDocuments(Integer overdueDocuments) {
    this.overdueDocuments = overdueDocuments;
  }

  public DocumentResponse openInvitedDocuments(Integer openInvitedDocuments) {
    this.openInvitedDocuments = openInvitedDocuments;
    return this;
  }

   /**
   * Number of open invited documents
   * @return openInvitedDocuments
  **/
  public Integer getOpenInvitedDocuments() {
    return openInvitedDocuments;
  }

  public void setOpenInvitedDocuments(Integer openInvitedDocuments) {
    this.openInvitedDocuments = openInvitedDocuments;
  }

  public DocumentResponse openInvitedToApproveDocuments(Integer openInvitedToApproveDocuments) {
    this.openInvitedToApproveDocuments = openInvitedToApproveDocuments;
    return this;
  }

   /**
   * Number of open documents to be approved by you
   * @return openInvitedToApproveDocuments
  **/
  public Integer getOpenInvitedToApproveDocuments() {
    return openInvitedToApproveDocuments;
  }

  public void setOpenInvitedToApproveDocuments(Integer openInvitedToApproveDocuments) {
    this.openInvitedToApproveDocuments = openInvitedToApproveDocuments;
  }

  public DocumentResponse openInvitedToViewDocuments(Integer openInvitedToViewDocuments) {
    this.openInvitedToViewDocuments = openInvitedToViewDocuments;
    return this;
  }

   /**
   * Number of open documents to be viewed by you
   * @return openInvitedToViewDocuments
  **/
  public Integer getOpenInvitedToViewDocuments() {
    return openInvitedToViewDocuments;
  }

  public void setOpenInvitedToViewDocuments(Integer openInvitedToViewDocuments) {
    this.openInvitedToViewDocuments = openInvitedToViewDocuments;
  }

  public DocumentResponse openInvitedToSignDocuments(Integer openInvitedToSignDocuments) {
    this.openInvitedToSignDocuments = openInvitedToSignDocuments;
    return this;
  }

   /**
   * Number of open documents to be signed by you
   * @return openInvitedToSignDocuments
  **/
  public Integer getOpenInvitedToSignDocuments() {
    return openInvitedToSignDocuments;
  }

  public void setOpenInvitedToSignDocuments(Integer openInvitedToSignDocuments) {
    this.openInvitedToSignDocuments = openInvitedToSignDocuments;
  }

  public DocumentResponse openPositionedDocuments(Integer openPositionedDocuments) {
    this.openPositionedDocuments = openPositionedDocuments;
    return this;
  }

   /**
   * Number of open positioned documents (ready for stack)
   * @return openPositionedDocuments
  **/
  public Integer getOpenPositionedDocuments() {
    return openPositionedDocuments;
  }

  public void setOpenPositionedDocuments(Integer openPositionedDocuments) {
    this.openPositionedDocuments = openPositionedDocuments;
  }

  public DocumentResponse stackedDocuments(Integer stackedDocuments) {
    this.stackedDocuments = stackedDocuments;
    return this;
  }

   /**
   * Number of stacked documents
   * @return stackedDocuments
  **/
  public Integer getStackedDocuments() {
    return stackedDocuments;
  }

  public void setStackedDocuments(Integer stackedDocuments) {
    this.stackedDocuments = stackedDocuments;
  }

  public DocumentResponse completedDocuments(Integer completedDocuments) {
    this.completedDocuments = completedDocuments;
    return this;
  }

   /**
   * Number of completed documents
   * @return completedDocuments
  **/
  public Integer getCompletedDocuments() {
    return completedDocuments;
  }

  public void setCompletedDocuments(Integer completedDocuments) {
    this.completedDocuments = completedDocuments;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DocumentResponse _documentResponse = (DocumentResponse) o;
    return Objects.equals(this.data, _documentResponse.data) &&
        Objects.equals(this.pagination, _documentResponse.pagination) &&
        Objects.equals(this.allDocuments, _documentResponse.allDocuments) &&
        Objects.equals(this.envelopeDocuments, _documentResponse.envelopeDocuments) &&
        Objects.equals(this.createdDocuments, _documentResponse.createdDocuments) &&
        Objects.equals(this.createdIncompleteDocuments, _documentResponse.createdIncompleteDocuments) &&
        Objects.equals(this.incompleteDocuments, _documentResponse.incompleteDocuments) &&
        Objects.equals(this.draftDocuments, _documentResponse.draftDocuments) &&
        Objects.equals(this.invitedDocuments, _documentResponse.invitedDocuments) &&
        Objects.equals(this.declinedDocuments, _documentResponse.declinedDocuments) &&
        Objects.equals(this.pendingInvitations, _documentResponse.pendingInvitations) &&
        Objects.equals(this.overdueDocuments, _documentResponse.overdueDocuments) &&
        Objects.equals(this.openInvitedDocuments, _documentResponse.openInvitedDocuments) &&
        Objects.equals(this.openInvitedToApproveDocuments, _documentResponse.openInvitedToApproveDocuments) &&
        Objects.equals(this.openInvitedToViewDocuments, _documentResponse.openInvitedToViewDocuments) &&
        Objects.equals(this.openInvitedToSignDocuments, _documentResponse.openInvitedToSignDocuments) &&
        Objects.equals(this.openPositionedDocuments, _documentResponse.openPositionedDocuments) &&
        Objects.equals(this.stackedDocuments, _documentResponse.stackedDocuments) &&
        Objects.equals(this.completedDocuments, _documentResponse.completedDocuments);
  }

  @Override
  public int hashCode() {
    return Objects.hash(data, pagination, allDocuments, envelopeDocuments, createdDocuments, createdIncompleteDocuments, incompleteDocuments, draftDocuments, invitedDocuments, declinedDocuments, pendingInvitations, overdueDocuments, openInvitedDocuments, openInvitedToApproveDocuments, openInvitedToViewDocuments, openInvitedToSignDocuments, openPositionedDocuments, stackedDocuments, completedDocuments);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DocumentResponse {\n");
    
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("    pagination: ").append(toIndentedString(pagination)).append("\n");
    sb.append("    allDocuments: ").append(toIndentedString(allDocuments)).append("\n");
    sb.append("    envelopeDocuments: ").append(toIndentedString(envelopeDocuments)).append("\n");
    sb.append("    createdDocuments: ").append(toIndentedString(createdDocuments)).append("\n");
    sb.append("    createdIncompleteDocuments: ").append(toIndentedString(createdIncompleteDocuments)).append("\n");
    sb.append("    incompleteDocuments: ").append(toIndentedString(incompleteDocuments)).append("\n");
    sb.append("    draftDocuments: ").append(toIndentedString(draftDocuments)).append("\n");
    sb.append("    invitedDocuments: ").append(toIndentedString(invitedDocuments)).append("\n");
    sb.append("    declinedDocuments: ").append(toIndentedString(declinedDocuments)).append("\n");
    sb.append("    pendingInvitations: ").append(toIndentedString(pendingInvitations)).append("\n");
    sb.append("    overdueDocuments: ").append(toIndentedString(overdueDocuments)).append("\n");
    sb.append("    openInvitedDocuments: ").append(toIndentedString(openInvitedDocuments)).append("\n");
    sb.append("    openInvitedToApproveDocuments: ").append(toIndentedString(openInvitedToApproveDocuments)).append("\n");
    sb.append("    openInvitedToViewDocuments: ").append(toIndentedString(openInvitedToViewDocuments)).append("\n");
    sb.append("    openInvitedToSignDocuments: ").append(toIndentedString(openInvitedToSignDocuments)).append("\n");
    sb.append("    openPositionedDocuments: ").append(toIndentedString(openPositionedDocuments)).append("\n");
    sb.append("    stackedDocuments: ").append(toIndentedString(stackedDocuments)).append("\n");
    sb.append("    completedDocuments: ").append(toIndentedString(completedDocuments)).append("\n");
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
