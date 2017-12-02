package mizuki.project.core.restserver.util;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by ycj on 16/6/27.
 * 文件
 */
public class WebIOUtil {

    /***
     * 用于springboot的文件下载
     * param: file or image
     */
    private static ResponseEntity<InputStreamResource> downloadFile(String fullPath,String filetype) throws IOException {
        FileSystemResource file = new FileSystemResource(fullPath);
        if(!file.exists()){
            return null;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
//        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getFilename()));
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        String contentType;
        if("file".equals(filetype)){
            contentType = "application/octet-stream";
        }else if("image".equals(filetype)){
            contentType = "image/jpeg";
        }else{
            return null;
        }
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.contentLength())
                .contentType(MediaType.parseMediaType(contentType))
                .body(new InputStreamResource(file.getInputStream()));
    }

    /***
     * 应用base64标记下载图片，下载接口
     * prePaths 要校验的图片地址前缀
     * */
    public static  ResponseEntity<InputStreamResource> downloadFileWithCode(
            String projectPath,String filetype,String code,String... prePaths ) throws IOException {
        String path = CodeUtil.base64UrlDecode(code);
        // 用于检查 图片的文件名格式
        String regexSuffix = "/[\\w-]+.?[a-zA-Z]*";
        for(String pre:prePaths){
            if(path.matches(pre+regexSuffix)){
                return downloadFile(projectPath+path, filetype);
            }
        }
        return null;

    }
}
