# Sproof Connector

Sproof is a modern digital signature solution coming from Austria. This connector provides
integration between Sproof and AxonIvy.

The Sproof API is published as a REST solution and integrated into Ivy as a REST client. The connector
provides the API as-is to avoid imposing any unwanted restrictions on the API. A demo
shows two simple variations of a typical signature workflow. As described later, additional calls may require minor adjustments to the connector.


## Demo

The demo is designed to fit on a single page. First, you have the option to designate two people as
signers. You must provide each person’s first name, last name, and email address.
If any of this information is missing, the person will be ignored in this demo for the sake of simplicity. 
*Please note that Sproof will send signature requests to the provided email addresses!*


The demo shows a static and a dynamic method for creating signature documents.

Once the document has been created, it can be sent to Sproof for signature with the click of a button. Sending the document initiates the signature process in Sproof, and signers are prompted to sign.
The API provides a so-called `Member Id` for identification, which is displayed in the interface (or can also be entered manually).


Clicking *Fetch* retrieves the current state of the document from Sproof and displays it on the page. (Another option, which is not shown in this demo, is to use a `callbackUrl`, which Sproof will call upon certain events and which could, for example, be connected to a REST service provided by AxonIvy.)

For additional features and information, please refer to the [Sproof API Documentation](https://docs.sproof.com/)!

### Signatures on a static document

A static document is created, and signers are assigned an absolute, fixed position
within the document for their signature. This approach is suitable when the document size is fixed or, more generally, when the absolute position of a signature within the document is known in advance.

### Signatures on a dynamically generated document

A dynamic document is created that contains placeholders for signers at specific locations.
This approach is ideal when documents can vary in size (such as
invoices with a variable number of items or contracts with optional sections). In this case,
the exact position of a signature within the document is unknown in advance.

## Setup

To start the demo, you will need a Sproof API key, as well as the sender's name and email address, which must be defined in global variables (e.g., in the Cockpit). You can request this information directly from Sproof.

```
@variables.yaml@
```

### Development

Sproof does not use the API key in a header field but instead passes it as a `GET` parameter or a JSON attribute, depending on the API call. This connector therefore does not adopt this abstraction but instead provides a service, `com.axonivy.connector.sproof.service.SproofService`, to easily retrieve the relevant data (for most calls, the API key is sufficient).

#### Legacy API

Sproof offers some endpoints in multiple versions (“legacy”). In the specification, the entities used are sometimes distinguished by a leading underscore `_`. This underscore was replaced by the word `Sproof` when the client was generated. Therefore, some entities exist in two variants (e.g., `Document` and `SproofDocument`), and care must be taken to use the correct variant when making REST calls.

Similarly, Sproof does not provide type information for polymorphic objects, which means that differentiation cannot be performed automatically in typed languages (such as Java). For some of the known types, special deserializers were therefore created in a `SproofFeature` to make this decision.

The current implementation of the connector includes only the objects required for the demo. If you need additional ones, you can unpack the connector and add the missing definitions. In this case, we would be pleased to hear about these changes so we can potentially incorporate them into the connector.

Alternatively, in these cases, you always have the option to read results as `JsonNode` (turn off the OpenAPI switch!) and specifically access the fields required for your use case.

#### Client Creation

As described earlier, Sproof’s OpenAPI specification currently contains object names that differ from one another only by a leading underscore. To enable the Client Generator to read these, these underscores have been replaced with the word `Sproof` (as of April 2026). If Sproof maintains this approach, a new version of the specification can be downloaded and “corrected” using the Unix shell script `openapi-correct.sh` (or similar logic). The “corrected” specification `openapi-corrected.json` can then be used directly to generate the REST client.