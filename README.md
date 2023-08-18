# Metadata-setting Lambda function for S3
This repository contains code that streaming export users can use to set up an AWS Lambda function that applies compression metadata to compressed, exported payloads before they reach S3. This will inform AWS of the compression format, allowing the files to be automatically decompressed when downloaded. Do note that this function only works for streaming export to S3 with GZIP compression enabled.

## Usage
To get the ZIP file required to set up this function, follow these steps:
1. Clone this repository.
2. Make any changes you would like to the code.
3. Run these commands from the within this repository's root directory:
    ```
    cd MetadataSettingLambdaFunction
    ./gradlew buildZip
    ```
The resulting ZIP file will be located in `MetadataSettingLambdaFunction/build/distributions`.
