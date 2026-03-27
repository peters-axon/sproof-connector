package com.axonivy.connector.sproof;

public interface SproofMessages {
	public static final String response1 = """
{
    "documents": [
        {
            "id": "39266f1b0f27be86f389de0e03ba819157e56e2dcfd66c745ff0a27d0f7b92c405280a",
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
                    "id": "277e66203718a7f2ec4ea4f2994999014f22bf8d7513b679e05ff72085d6ca562a1c5e",
                    "documentId": "39266f1b0f27be86f389de0e03ba819157e56e2dcfd66c745ff0a27d0f7b92c405280a",
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
                    "id": "fc4f9a5fbe0de62614c54189309f9924e3196bc4fcbc9e220a8cd0d0ea044d52d1a16b",
                    "documentId": "39266f1b0f27be86f389de0e03ba819157e56e2dcfd66c745ff0a27d0f7b92c405280a",
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
                    "recipientId": "ec329c34-3dae-479b-8ee1-e64d3e23542a",
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
                "id": "4a65f6b98018d7f7250c5cdab0e1ed6f53657757328e587388fd7cc0d4cb95c94ad09d",
                "documentId": "39266f1b0f27be86f389de0e03ba819157e56e2dcfd66c745ff0a27d0f7b92c405280a",
                "email": "teamgryphon+panda-sproofsender@docuwaregroup.onmicrosoft.com",
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
                "recipientId": "8cbfec2d-ef0a-4c80-a047-a368d332616d",
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

}
