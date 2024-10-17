<<<<<<< HEAD
//package kr.gateway.config;
=======
package kr.gateway.config;//package kr.gateway.config;
>>>>>>> 80cca43033e3ec9ef9c5917c91a2b4fe57bf4ee7
//
//import com.amazonaws.auth.AWSStaticCredentialsProvider;
//import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.client.builder.AwsClientBuilder;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.AmazonS3ClientBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//@Configuration
//public class S3Config {
//    private final String endPoint = "https://kr.object.ncloudstorage.com";
//    private final String regionName = "kr-standard";  //region
//    private final String accessKey = "ncp_iam_BPASKR4hDZEqhZxoJTU0\n"; // 버켓의 접근키
//    private final String secretKey = "ncp_iam_BPKSKRW7EiCKxGGi0L9AODsJCwtJ8wcZ9a"; // NCP의 240703 시크릿키
//    private final String bucketName = "nyamnyam.storage";  //버켓 이름
//    @Bean
//    public AmazonS3 s3Client() {
//        return AmazonS3ClientBuilder.standard()
//                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, regionName))
//                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
//                .build();
//    }
//    @Bean
//    public String bucketName() {
//        return bucketName;
//    }
//}
