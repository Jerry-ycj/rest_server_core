package mizuki.project.core.restserver.util;

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
}
