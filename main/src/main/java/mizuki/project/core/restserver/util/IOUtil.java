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
public class IOUtil {

    public static String getSuffixFromMIME(String mime){
        String suffix = "";
        switch (mime){
            case "image/x-ms-bmp":suffix=".bmp";break;
            case "image/jpeg":suffix=".jpg";break;
            case "image/gif":suffix=".gif";break;
            case "image/png":suffix=".png";break;
        }
        return suffix;
    }

    /**
     * path: /xxx
     * filename: xxx
     */
    public static File prepare(String path,String filename){
        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
        return new File(path+"/"+filename);
    }

    /***
     * bytebuffer to file
     */
    public static  void saveByteBuffer2File(ByteBuffer buffer, File dst) throws IOException {
//        BufferedOutputStream bo = new BufferedOutputStream(new FileOutputStream(dst));
        FileChannel channel = null;
        try {
            channel = new FileOutputStream(dst).getChannel();
            channel.write(buffer);
        }finally {
            if(channel!=null){
                channel.close();
            }
        }
    }

    public static ByteBuffer readByteBufferFromFile(File src) throws IOException {
        FileChannel channel = null;
        try {
            channel = new FileInputStream(src).getChannel();
            ByteBuffer buffer = ByteBuffer.allocate((int)src.length());
            channel.read(buffer);
            return buffer;
        }finally {
            if(channel!=null){
                channel.close();
            }
        }
    }

    /**
     *  文件流 写入 文件
     */
    public static void saveStream2File(InputStream in, File dst) throws IOException {
        BufferedInputStream bi = new BufferedInputStream(in);
        BufferedOutputStream bo = new BufferedOutputStream(new FileOutputStream(dst));
        byte[] buffer = new byte[10240];
        while (bi.read(buffer)!=-1){
            bo.write(buffer);
            bo.flush();
        }
        bi.close();
        bo.close();
    }

    /**
     * 文件流 写入 文件(追加)
     */
    public static void saveStream2FileAdd(
            InputStream in, String filepath) throws IOException {
        BufferedInputStream bi = new BufferedInputStream(in);
        RandomAccessFile raf = new RandomAccessFile(filepath, "rw");
        raf.seek(raf.length());
        byte[] buffer = new byte[10240];
        while (bi.read(buffer)!=-1){
            raf.write(buffer);
        }
        bi.close();
        raf.close();
    }

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
