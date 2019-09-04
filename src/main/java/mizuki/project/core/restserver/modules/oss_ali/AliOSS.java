package mizuki.project.core.restserver.modules.oss_ali;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sts.model.v20150401.AssumeRoleRequest;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AliOSS {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private OSSClient ossClient;

    @Value("${mod.oss-ali.accessKey}")
    private String accessKey;
    @Value("${mod.oss-ali.accessKeySecret}")
    private String accessKeySecret;
    @Value("${mod.oss-ali.endpoint}")
    private String endpoint;
    @Value("${mod.oss-ali.stsEndpoint}")
    private String stsEndpoint;
    @Value("${mod.oss-ali.bucketName}")
    private String bucketName;
    @Value("${mod.oss-ali.arn}")
    private String arn;
    @Value("${mod.oss-ali.region}")
    private String region;

    private OSSClient ossClient(){
        if(ossClient==null){
            ossClient = new OSSClient(endpoint, accessKey, accessKeySecret);
        }
        return ossClient;
    }

    private void closeOssClient(){
        if(ossClient!=null){
            ossClient.shutdown();
        }
        ossClient=null;
    }

//    @PostConstruct
//    private void init(){
//        if (!ossClient().doesBucketExist(bucketName)) {
//            throw new RuntimeException("ali oss bucket not exist");
//        }
//    }

    /**
     * key - path/xxx.xx
     */
    public void uploadFile(String key, File file) throws IOException {
        OSSClient ossClient = ossClient();
        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            // 私有文件
            objectMetadata.setObjectAcl(CannedAccessControlList.Private);
            ossClient.putObject(new PutObjectRequest(bucketName, key, file,objectMetadata));
        }finally {
            closeOssClient();
        }
    }

    /**
     * 主要授权 终端自主的上传和下载。
     * 使用URL签名授权访问
     * https://help.aliyun.com/document_detail/32016.html?spm=5176.doc32014.6.665.T5Pbcc
     *
     * 注：主要采用sts方式
     */
    public URL signedUrl(String object_key, int time_second){
        return ossClient().generatePresignedUrl(bucketName,object_key,
                new Date(new Date().getTime() + time_second * 1000));
    }

    /**
     * 返回 sts
     * 终端用sts 上传或构造url
     *  path: test/timg.jpg or test/12/*
     * @return
     */
    public Map<String, Object> stsGetPutForUser(String roleSession, List<String> path) throws ClientException {
        // 如何定制你的policy?
        // https://github.com/rockuw/node-sts-app-server/blob/master/policy/bucket_read_write_policy.txt
        StringBuilder resources= new StringBuilder();
        if(path.size()==0) return null;
        for(String r:path){
            resources.append("\"acs:oss:*:*:").append(bucketName).append("/").append(r).append("/*\",");
        }
        resources.deleteCharAt(resources.length()-1);
        String policy = "{" +
                "\"Version\": \"1\", " +
                "\"Statement\": [" +
                " {" +
                "  \"Action\": [" +
                "\"oss:GetObject\",\"oss:PutObject\",\"oss:DeleteObject\",\"oss:PutObjectAcl\", \"oss:GetObjectAcl\"], " +
                "  \"Resource\": [" + resources.toString() + "], " +
                "  \"Effect\": \"Allow\"" +
                " }" +
                "]" +
                "}";
        // 此处必须为 HTTPS
        ProtocolType protocolType = ProtocolType.HTTPS;
        final AssumeRoleResponse response = assumeRole(accessKey, accessKeySecret,
                arn, roleSession, policy, protocolType);
        Map<String,Object> map = new HashMap<>();
        map.put("accessKeyId",response.getCredentials().getAccessKeyId());
        map.put("accessKeySecret",response.getCredentials().getAccessKeySecret());
        map.put("stsToken",response.getCredentials().getSecurityToken());
        map.put("expiration",response.getCredentials().getExpiration());
        map.put("region",region);
        map.put("bucket",bucketName);
        return map;
//            OSSClient ossClient = new OSSClient(endpoint, response.getCredentials().getAccessKeyId(),
//                    response.getCredentials().getAccessKeySecret(), response.getCredentials().getSecurityToken());
//            System.out.println(ossClient.generatePresignedUrl(bucketName,"test/timg.jpg",
//                    new Date(new Date().getTime() + 600 * 1000)));
    }

    private AssumeRoleResponse assumeRole(
            String accessKeyId, String accessKeySecret,
            String roleArn, String roleSessionName, String policy,
            ProtocolType protocolType
    ) throws ClientException {
        // 创建一个 Aliyun Acs Client, 用于发起 OpenAPI 请求
        IClientProfile profile = DefaultProfile.getProfile(region, accessKeyId, accessKeySecret);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        // 创建一个 AssumeRoleRequest 并设置请求参数
        final AssumeRoleRequest request = new AssumeRoleRequest();
        request.setVersion("2015-04-01");
        request.setMethod(MethodType.POST);
        request.setProtocol(protocolType);
        request.setRoleArn(roleArn);
        request.setRoleSessionName(roleSessionName);
        request.setPolicy(policy);
        // duration min/max 15min/1h  default max
//        request.setDurationSeconds(180L);
        // 发起请求，并得到response
        return client.getAcsResponse(request);
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

    public String getArn() {
        return arn;
    }

    public AliOSS setArn(String arn) {
        this.arn = arn;
        return this;
    }
}
