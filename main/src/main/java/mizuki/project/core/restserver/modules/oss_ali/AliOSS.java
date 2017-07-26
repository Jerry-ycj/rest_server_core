package mizuki.project.core.restserver.modules.oss_ali;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CreateBucketRequest;
import com.aliyun.oss.model.PutObjectRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.io.*;

@ConfigurationProperties("mod.oss_ali")
public class AliOSS {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private OSSClient ossClient;
    private String accessKey;
    private String accessKeySecret;
    private String endpoint;
    private String bucketName;

    @PostConstruct
    private void init(){
        ossClient = new OSSClient(endpoint, accessKey, accessKeySecret);

        if (!ossClient.doesBucketExist(bucketName)) {
                /*
                 * Create a new OSS bucket
                 */
            logger.info("Creating bucket " + bucketName);
            ossClient.createBucket(bucketName);
            CreateBucketRequest createBucketRequest= new CreateBucketRequest(bucketName);
            createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
            ossClient.createBucket(createBucketRequest);
        }
    }

    /**
     * key - path/xxx.xx
     */
    public void uploadFile(String key, File file) throws IOException {
        try {
            ossClient.putObject(new PutObjectRequest(bucketName, key, file));
        }finally {
            ossClient.shutdown();
        }
    }

    public String getAccessKey() {
        return accessKey;
    }

    public AliOSS setAccessKey(String accessKey) {
        this.accessKey = accessKey;
        return this;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public AliOSS setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
        return this;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public AliOSS setEndpoint(String endpoint) {
        this.endpoint = endpoint;
        return this;
    }

    public String getBucketName() {
        return bucketName;
    }

    public AliOSS setBucketName(String bucketName) {
        this.bucketName = bucketName;
        return this;
    }
}
