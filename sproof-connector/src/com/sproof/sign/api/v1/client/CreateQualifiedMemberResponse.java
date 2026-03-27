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
import com.sproof.sign.api.v1.client.PlanMember;

import java.time.OffsetDateTime;
/**
 * CreateQualifiedMemberResponse
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2026-03-25T13:40:51.024343900+01:00[Europe/Vienna]")

public class CreateQualifiedMemberResponse {
  @JsonProperty("email")
  private String email = null;

  @JsonProperty("firstName")
  private String firstName = null;

  @JsonProperty("lastName")
  private String lastName = null;

  @JsonProperty("identificationStarted")
  private Boolean identificationStarted = null;

  @JsonProperty("isIdentified")
  private Boolean isIdentified = null;

  @JsonProperty("identifiedAt")
  private OffsetDateTime identifiedAt = null;

  @JsonProperty("isAdvancedUser")
  private Boolean isAdvancedUser = null;

  @JsonProperty("isQualifiedUser")
  private Boolean isQualifiedUser = null;

  @JsonProperty("isBlocked")
  private Boolean isBlocked = null;

  @JsonProperty("isPending")
  private Boolean isPending = null;

  /**
   * Role of the user in the plan
   */
  public enum RoleEnum {
    OWNER("owner"),
    USER("user"),
    POWERUSER("poweruser");

    private String value;

    RoleEnum(String value) {
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
    public static RoleEnum fromValue(String input) {
      for (RoleEnum b : RoleEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }

  }  @JsonProperty("role")
  private RoleEnum role = null;

  @JsonProperty("planTransferPending")
  private Boolean planTransferPending = null;

  @JsonProperty("isDeletable")
  private Boolean isDeletable = null;

  @JsonProperty("remainingDaysToDelete")
  private Integer remainingDaysToDelete = null;

  @JsonProperty("lastLoginAt")
  private OffsetDateTime lastLoginAt = null;

  @JsonProperty("verificationLink")
  private String verificationLink = null;

  public CreateQualifiedMemberResponse email(String email) {
    this.email = email;
    return this;
  }

   /**
   * Email of plan member
   * @return email
  **/
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public CreateQualifiedMemberResponse firstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

   /**
   * First name of member
   * @return firstName
  **/
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public CreateQualifiedMemberResponse lastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

   /**
   * Last name of member
   * @return lastName
  **/
  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public CreateQualifiedMemberResponse identificationStarted(Boolean identificationStarted) {
    this.identificationStarted = identificationStarted;
    return this;
  }

   /**
   * Boolean which indicates if the identification process was started
   * @return identificationStarted
  **/
  public Boolean isIdentificationStarted() {
    return identificationStarted;
  }

  public void setIdentificationStarted(Boolean identificationStarted) {
    this.identificationStarted = identificationStarted;
  }

  public CreateQualifiedMemberResponse isIdentified(Boolean isIdentified) {
    this.isIdentified = isIdentified;
    return this;
  }

   /**
   * Boolean which indicates if the plan member is identified for qualified signing
   * @return isIdentified
  **/
  public Boolean isIsIdentified() {
    return isIdentified;
  }

  public void setIsIdentified(Boolean isIdentified) {
    this.isIdentified = isIdentified;
  }

  public CreateQualifiedMemberResponse identifiedAt(OffsetDateTime identifiedAt) {
    this.identifiedAt = identifiedAt;
    return this;
  }

   /**
   * Date of the last time the user went through the identification process
   * @return identifiedAt
  **/
  public OffsetDateTime getIdentifiedAt() {
    return identifiedAt;
  }

  public void setIdentifiedAt(OffsetDateTime identifiedAt) {
    this.identifiedAt = identifiedAt;
  }

  public CreateQualifiedMemberResponse isAdvancedUser(Boolean isAdvancedUser) {
    this.isAdvancedUser = isAdvancedUser;
    return this;
  }

   /**
   * Boolean which indicates if the plan member is an advanced user
   * @return isAdvancedUser
  **/
  public Boolean isIsAdvancedUser() {
    return isAdvancedUser;
  }

  public void setIsAdvancedUser(Boolean isAdvancedUser) {
    this.isAdvancedUser = isAdvancedUser;
  }

  public CreateQualifiedMemberResponse isQualifiedUser(Boolean isQualifiedUser) {
    this.isQualifiedUser = isQualifiedUser;
    return this;
  }

   /**
   * Boolean which indicates if the plan member is a qualifed user
   * @return isQualifiedUser
  **/
  public Boolean isIsQualifiedUser() {
    return isQualifiedUser;
  }

  public void setIsQualifiedUser(Boolean isQualifiedUser) {
    this.isQualifiedUser = isQualifiedUser;
  }

  public CreateQualifiedMemberResponse isBlocked(Boolean isBlocked) {
    this.isBlocked = isBlocked;
    return this;
  }

   /**
   * Boolean which indicates if the plan member is blocked but not deleted
   * @return isBlocked
  **/
  public Boolean isIsBlocked() {
    return isBlocked;
  }

  public void setIsBlocked(Boolean isBlocked) {
    this.isBlocked = isBlocked;
  }

  public CreateQualifiedMemberResponse isPending(Boolean isPending) {
    this.isPending = isPending;
    return this;
  }

   /**
   * Boolean which indicates that a plan member wants to join a plan which need to be updated.
   * @return isPending
  **/
  public Boolean isIsPending() {
    return isPending;
  }

  public void setIsPending(Boolean isPending) {
    this.isPending = isPending;
  }

  public CreateQualifiedMemberResponse role(RoleEnum role) {
    this.role = role;
    return this;
  }

   /**
   * Role of the user in the plan
   * @return role
  **/
  public RoleEnum getRole() {
    return role;
  }

  public void setRole(RoleEnum role) {
    this.role = role;
  }

  public CreateQualifiedMemberResponse planTransferPending(Boolean planTransferPending) {
    this.planTransferPending = planTransferPending;
    return this;
  }

   /**
   * Plan transfer is in progress
   * @return planTransferPending
  **/
  public Boolean isPlanTransferPending() {
    return planTransferPending;
  }

  public void setPlanTransferPending(Boolean planTransferPending) {
    this.planTransferPending = planTransferPending;
  }

  public CreateQualifiedMemberResponse isDeletable(Boolean isDeletable) {
    this.isDeletable = isDeletable;
    return this;
  }

   /**
   * Boolean which indicates that a plan member can be deleted. You need to wait at lease 30 days after the identification of a plan member before you can delete the member
   * @return isDeletable
  **/
  public Boolean isIsDeletable() {
    return isDeletable;
  }

  public void setIsDeletable(Boolean isDeletable) {
    this.isDeletable = isDeletable;
  }

  public CreateQualifiedMemberResponse remainingDaysToDelete(Integer remainingDaysToDelete) {
    this.remainingDaysToDelete = remainingDaysToDelete;
    return this;
  }

   /**
   * Remaining days to wait until the plan member can be finally deleted
   * @return remainingDaysToDelete
  **/
  public Integer getRemainingDaysToDelete() {
    return remainingDaysToDelete;
  }

  public void setRemainingDaysToDelete(Integer remainingDaysToDelete) {
    this.remainingDaysToDelete = remainingDaysToDelete;
  }

  public CreateQualifiedMemberResponse lastLoginAt(OffsetDateTime lastLoginAt) {
    this.lastLoginAt = lastLoginAt;
    return this;
  }

   /**
   * Date of the last time the member logged into their account
   * @return lastLoginAt
  **/
  public OffsetDateTime getLastLoginAt() {
    return lastLoginAt;
  }

  public void setLastLoginAt(OffsetDateTime lastLoginAt) {
    this.lastLoginAt = lastLoginAt;
  }

  public CreateQualifiedMemberResponse verificationLink(String verificationLink) {
    this.verificationLink = verificationLink;
    return this;
  }

   /**
   * A link to set the password and complete the registration process
   * @return verificationLink
  **/
  public String getVerificationLink() {
    return verificationLink;
  }

  public void setVerificationLink(String verificationLink) {
    this.verificationLink = verificationLink;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateQualifiedMemberResponse createQualifiedMemberResponse = (CreateQualifiedMemberResponse) o;
    return Objects.equals(this.email, createQualifiedMemberResponse.email) &&
        Objects.equals(this.firstName, createQualifiedMemberResponse.firstName) &&
        Objects.equals(this.lastName, createQualifiedMemberResponse.lastName) &&
        Objects.equals(this.identificationStarted, createQualifiedMemberResponse.identificationStarted) &&
        Objects.equals(this.isIdentified, createQualifiedMemberResponse.isIdentified) &&
        Objects.equals(this.identifiedAt, createQualifiedMemberResponse.identifiedAt) &&
        Objects.equals(this.isAdvancedUser, createQualifiedMemberResponse.isAdvancedUser) &&
        Objects.equals(this.isQualifiedUser, createQualifiedMemberResponse.isQualifiedUser) &&
        Objects.equals(this.isBlocked, createQualifiedMemberResponse.isBlocked) &&
        Objects.equals(this.isPending, createQualifiedMemberResponse.isPending) &&
        Objects.equals(this.role, createQualifiedMemberResponse.role) &&
        Objects.equals(this.planTransferPending, createQualifiedMemberResponse.planTransferPending) &&
        Objects.equals(this.isDeletable, createQualifiedMemberResponse.isDeletable) &&
        Objects.equals(this.remainingDaysToDelete, createQualifiedMemberResponse.remainingDaysToDelete) &&
        Objects.equals(this.lastLoginAt, createQualifiedMemberResponse.lastLoginAt) &&
        Objects.equals(this.verificationLink, createQualifiedMemberResponse.verificationLink);
  }

  @Override
  public int hashCode() {
    return Objects.hash(email, firstName, lastName, identificationStarted, isIdentified, identifiedAt, isAdvancedUser, isQualifiedUser, isBlocked, isPending, role, planTransferPending, isDeletable, remainingDaysToDelete, lastLoginAt, verificationLink);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateQualifiedMemberResponse {\n");
    
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
    sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
    sb.append("    identificationStarted: ").append(toIndentedString(identificationStarted)).append("\n");
    sb.append("    isIdentified: ").append(toIndentedString(isIdentified)).append("\n");
    sb.append("    identifiedAt: ").append(toIndentedString(identifiedAt)).append("\n");
    sb.append("    isAdvancedUser: ").append(toIndentedString(isAdvancedUser)).append("\n");
    sb.append("    isQualifiedUser: ").append(toIndentedString(isQualifiedUser)).append("\n");
    sb.append("    isBlocked: ").append(toIndentedString(isBlocked)).append("\n");
    sb.append("    isPending: ").append(toIndentedString(isPending)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    planTransferPending: ").append(toIndentedString(planTransferPending)).append("\n");
    sb.append("    isDeletable: ").append(toIndentedString(isDeletable)).append("\n");
    sb.append("    remainingDaysToDelete: ").append(toIndentedString(remainingDaysToDelete)).append("\n");
    sb.append("    lastLoginAt: ").append(toIndentedString(lastLoginAt)).append("\n");
    sb.append("    verificationLink: ").append(toIndentedString(verificationLink)).append("\n");
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
