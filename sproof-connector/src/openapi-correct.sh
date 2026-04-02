#!/bin/bash

sed -r 's/([\/"])_/\1Sproof/' openapi.json > openapi-corrected.json

