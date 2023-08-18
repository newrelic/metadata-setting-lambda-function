package metadatasetter;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.MetadataDirective;

import java.util.Map;

/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<S3Event, String> {

    public String handleRequest(final S3Event input, final Context context) {
        S3Client s3Client = S3Client.builder().build();

        S3EventNotification.S3EventNotificationRecord record = input.getRecords().get(0);
        String bucketName = record.getS3().getBucket().getName();
        String objectKey = record.getS3().getObject().getUrlDecodedKey();

        HeadObjectResponse headObjectResponse = s3Client.headObject(HeadObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build());

        String encoding = headObjectResponse.contentEncoding();
        String contentType = headObjectResponse.contentType();

        if (encoding == null || encoding.isBlank()) {
            s3Client.copyObject(CopyObjectRequest.builder()
                    .destinationBucket(bucketName)
                    .destinationKey(objectKey)
                    .sourceBucket(bucketName)
                    .sourceKey(objectKey)
                    .contentEncoding("gzip")
                    .contentType(contentType)
                    .metadata(Map.of(
                            "processed-by-lambda", "true"
                    ))
                    .metadataDirective(MetadataDirective.REPLACE)
                    .build());
            return "Ok";
        }

        return "No Change";
    }
}
