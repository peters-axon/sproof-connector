package com.axonivy.connector.sproof;

public interface SproofMessages {
	public static final String SIG_RQ_RSP_1 = """
			{
			"documents": [
			{
			"id": "39266f1b0f27be86f389de0e1b0f27be86e56e2dcfd66c745ff0a27d0f7b92c405280a",
			"name": "",
			"language": "en",
			"startSendingReminders": null,
			"sendReminder": false,
			"reminderSent": null,
			"reminderInterval": null,
			"createdAt": "2026-03-27T16:43:52.213Z",
			"callbackUrl": null,
			"returnUrl": null,
			"returnBtnText": null,
			"inPersonSigning": false,
			"signingRound": 1,
			"isTemplate": false,
			"templateId": null,
			"linkId": null,
			"linkExpiresAt": null,
			"derivedFromLink": false,
			"state": "pending",
			"complianceLevel": "None",
			"allowForwarding": false,
			"linkExpires": false,
			"setDueDate": false,
			"dueDate": null,
			"focusedSigningMode": false,
			"linkMaxSignaturesEnabled": false,
			"linkMaxSignatures": null,
			"fastlaneProfileId": null,
			"refId": null,
			"sendOutFinishedPdf": false,
			"signatureTypes": [
			"advanced"
			],
			"members": [
			{
			"id": "277e66203718a7f2ec4ea4f2994999014f1b0f27be86b679e05ff72085d6ca562a1c5e",
			"documentId": "39266f1b0f27be86f389de0e03ba819157e51b0f27be86745ff0a27d0f7b92c405280a",
			"email": "signer2@sprooftest.axonivy.com",
			"firstName": "Signer",
			"lastName": "Two",
			"isAdmin": false,
			"createdAt": "2026-03-27T16:43:52.330Z",
			"sentDeleteNotificationAt": null,
			"manuallyLastEmailSent": null,
			"signaturePosition": [
			{
			"page": 0,
			"x": 0.151,
			"y": 0.329,
			"width": 0.31,
			"height": 0.111,
			"pageWidth": 595,
			"pageHeight": 842
			}
			],
			"doNotSendEmails": true,
			"declinedAt": null,
			"signingOrder": 2,
			"onDemand": "",
			"recipientId": "de917f71-d8ce-447e-a3f8-96b171dfa4d9",
			"signatureTypes": [],
			"signatureTypesActive": false,
			"privateMessage": null,
			"customId": null,
			"useFastlane": false,
			"role": "signer",
			"approvedAt": null,
			"verificationConfig": {
			"method": "SMS",
			"editable": true
			},
			"phoneNumber": null,
			"signatures": [],
			"signedAt": null
			},
			{
			"id": "fc4f9a5fbe0de62614c54189309f91b0f27be864fcbc9e220a8cd0d0ea044d52d1a16b",
			"documentId": "39266f1b0f27be86f389de0e03ba1b0f27be862dcfd66c745ff0a27d0f7b92c405280a",
			"email": "signer1@sprooftest.axonivy.com",
			"firstName": "Signer",
			"lastName": "One",
			"isAdmin": false,
			"createdAt": "2026-03-27T16:43:52.323Z",
			"sentDeleteNotificationAt": null,
			"manuallyLastEmailSent": null,
			"signaturePosition": [
			{
			"page": 0,
			"x": 0.151,
			"y": 0.23400003,
			"width": 0.31,
			"height": 0.111,
			"pageWidth": 595,
			"pageHeight": 842
			}
			],
			"doNotSendEmails": true,
			"declinedAt": null,
			"signingOrder": 1,
			"onDemand": "",
			"recipientId": "ec329c34-3dae-479b-8ee1-1b0f27be862a",
			"signatureTypes": [],
			"signatureTypesActive": false,
			"privateMessage": null,
			"customId": null,
			"useFastlane": false,
			"role": "signer",
			"approvedAt": null,
			"verificationConfig": {
			"method": "SMS",
			"editable": true
			},
			"phoneNumber": null,
			"signatures": [],
			"signedAt": null
			}
			],
			"member": {
			"id": "4a65f6b98018d7f7250c5cdab0e1ed6f51b0f27be86e587388fd7cc0d4cb95c94ad09d",
			"documentId": "39266f1b0f27be86f389de0e01b0f27be8656e2dcfd66c745ff0a27d0f7b92c405280a",
			"email": "some.sender@sprooftest.axonivy.com",
			"firstName": "Sproof",
			"lastName": "Sender",
			"isAdmin": true,
			"createdAt": "2026-03-27T16:43:52.215Z",
			"sentDeleteNotificationAt": null,
			"manuallyLastEmailSent": null,
			"signaturePosition": [],
			"doNotSendEmails": false,
			"declinedAt": null,
			"signingOrder": 1,
			"onDemand": "",
			"recipientId": "8cbfec2d-ef0a-4c80-a047-1b0f27be866d",
			"signatureTypes": [],
			"signatureTypesActive": false,
			"privateMessage": null,
			"customId": null,
			"useFastlane": false,
			"role": "none",
			"approvedAt": null,
			"verificationConfig": null,
			"phoneNumber": null,
			"signatures": [],
			"signedAt": null
			},
			"boxes": [],
			"overdue": false
			}
			]
			}			
			""";
	public static final String GET_DOC_RSP_1 = """
			{
			"name": "sign-fixed",
			"id": "6a60850f8255b436e61e4b40c454ba8811b0f27be86b03ca04d0a343ffba19b664b8a4",
			"language": "en",
			"updatedAt": "2026-03-30T16:42:15.717Z",
			"createdAt": "2026-03-30T16:42:15.497Z",
			"signaturesTypes": [
			"advanced"
			],
			"callbackUrl": null,
			"returnUrl": null,
			"returnBtnText": null,
			"inPersonSigning": false,
			"signingRound": 1,
			"state": "pending",
			"member": {
			"id": "552275375850489cd461301912934d93a1b0f27be8634b6b86d5ba92fafa773cb78102",
			"email": "some.sender@sprooftest.axonivy.com",
			"firstName": "Sproof",
			"lastName": "Sender",
			"lastActivityAt": "2026-03-30T16:42:15.500Z",
			"createdAt": "2026-03-30T16:42:15.500Z",
			"isAdmin": true,
			"isSigner": false,
			"isApprover": false,
			"isViewer": false,
			"signed": false,
			"approvedAt": null,
			"viewedAt": null,
			"signaturePosition": [],
			"signedAt": null,
			"signingOrder": 1,
			"declinedAt": null,
			"signatures": []
			},
			"boxes": [],
			"members": [
			{
			"id": "81458440115287d5aa392f40831a0db4e1b0f27be86d3e4f590cb1c89cbdb941e69b51",
			"isSigner": true,
			"isApprover": false,
			"isViewer": false,
			"email": "signer2@sprooftest.axonivy.com",
			"firstName": "Signer",
			"lastName": "Two",
			"isAdmin": false,
			"signedAt": null,
			"approvedAt": null,
			"viewedAt": null,
			"declinedAt": null,
			"signingOrder": 1,
			"signaturePosition": {
			"x": 0.3,
			"y": 0.8,
			"page": 0,
			"width": 0.3,
			"height": 0.1
			},
			"signed": false,
			"signatures": []
			},
			{
			"id": "9eff3f05b5e3a54d7d4cb7657626f6e061b0f27be86abd2946d97833ab6258c0a0f452",
			"isSigner": true,
			"isApprover": false,
			"isViewer": false,
			"email": "signer1@sprooftest.axonivy.com",
			"firstName": "Signer",
			"lastName": "One",
			"isAdmin": false,
			"signedAt": null,
			"approvedAt": null,
			"viewedAt": null,
			"declinedAt": null,
			"signingOrder": 1,
			"signaturePosition": {
			"x": 0.3,
			"y": 0.6,
			"page": 0,
			"width": 0.3,
			"height": 0.1
			},
			"signed": false,
			"signatures": []
			}
			],
			"allSignersSigned": false,
			"allMembersSigned": false
			}
			""";

	public static final String GET_DOC_RSP_2 = """
			{
			"name": "sign-placeholders",
			"id": "fd3dc1062d8b41ac38d1eeeb5b5e70ad71b0f27be86432554081a1cd19982165e91f04",
			"language": "en",
			"updatedAt": "2026-04-02T15:19:26.137Z",
			"createdAt": "2026-04-02T15:16:32.936Z",
			"signaturesTypes": [
			"advanced"
			],
			"callbackUrl": null,
			"returnUrl": null,
			"returnBtnText": null,
			"inPersonSigning": false,
			"signingRound": 2,
			"state": "pending",
			"member": {
			"id": "0415d107da1e1b0f27be86b0128d9938f94095e4fda85201276706c4cfb7c5c3db4c1f",
			"email": "some.sender@sprooftest.axonivy.com",
			"firstName": "Sproof",
			"lastName": "Sender",
			"lastActivityAt": "2026-04-02T15:16:32.938Z",
			"createdAt": "2026-04-02T15:16:32.938Z",
			"isAdmin": true,
			"isSigner": false,
			"isApprover": false,
			"isViewer": false,
			"signed": false,
			"approvedAt": null,
			"viewedAt": null,
			"signaturePosition": [],
			"signedAt": null,
			"signingOrder": 1,
			"declinedAt": null,
			"signatures": []
			},
			"boxes": [],
			"members": [
			{
			"id": "cfa2d3415a131b0f27be860e826fa3e1ef5ec2192df3a949791a4c2776c03aeabbfdf4",
			"isSigner": true,
			"isApprover": false,
			"isViewer": false,
			"email": "some.body@sprooftest.axonivy.com",
			"firstName": "Signer",
			"lastName": "Two",
			"isAdmin": false,
			"signedAt": null,
			"approvedAt": null,
			"viewedAt": null,
			"declinedAt": null,
			"signingOrder": 2,
			"signaturePosition": {
			"x": 0.151,
			"y": 0.47100005,
			"page": 0,
			"width": 0.303,
			"height": 0.109,
			"pageHeight": 842,
			"pageWidth": 595
			},
			"signed": false,
			"signatures": []
			},
			{
			"id": "da9de7064a971b0f27be86b79bbf49d01a359459ff1d68724d2db2995da8bcfeeca23f",
			"isSigner": true,
			"isApprover": false,
			"isViewer": false,
			"email": "some.otherbody@sprooftest.axonivy.com",
			"firstName": "Signer",
			"lastName": "One",
			"isAdmin": false,
			"signedAt": "2026-04-02T15:19:25.942Z",
			"approvedAt": null,
			"viewedAt": null,
			"declinedAt": null,
			"signingOrder": 1,
			"signaturePosition": null,
			"signed": true,
			"signatures": [
			{
			"signatureType": "aes_sproof",
			"signedAt": "2026-04-02T15:19:25.942Z"
			}
			]
			}
			],
			"allSignersSigned": false,
			"allMembersSigned": false
			}
			""";
}
