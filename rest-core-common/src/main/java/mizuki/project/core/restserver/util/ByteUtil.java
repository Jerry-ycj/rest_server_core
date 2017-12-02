package mizuki.project.core.restserver.util;

import java.util.List;

/**
 * Created by ycj on 2016/12/20.
 *
 */
public class ByteUtil {

    public static String byte2hex(byte [] buffer){
        String h = "";
        for (byte aBuffer : buffer) {
            String temp = Integer.toHexString(aBuffer & 0xFF);
            if (temp.length() == 1) {
                temp = "0" + temp;
            }
            h = h + " " + temp;
        }
        return h;
    }

    //高位在前，低位在后
    public static byte[] int2bytes(int num){
        byte[] result = new byte[4];
        result[0] = (byte)((num >>> 24) & 0xff);//说明一
        result[1] = (byte)((num >>> 16)& 0xff );
        result[2] = (byte)((num >>> 8) & 0xff );
        result[3] = (byte)((num) & 0xff );
        return result;
    }

    // byte - bits
    public static int[] byte2bits(byte b){
        int[] ret = new int[8];
        for(int i=0;i<8;i++){
            ret[i] = (b>>>(7-i))&0x1;
        }
        return ret;
    }

    //高位在前，低位在后
    public static int bytes2intHL(byte[] bytes){
        int result = 0;
        if(bytes.length <= 4){
            for(int i=0;i<bytes.length;i++){
                int a = (bytes[i] & 0xff) << (8*(bytes.length-1)-i*8);
                result = result | a;
            }
        }
        return result;
    }
    public static int bytes2intHL(byte b){
        byte[] bytes = new byte[1];
        bytes[0]=b;
        return bytes2intHL(bytes);
    }

    /**
     * 低位在前
     */
    public static int bytes2IntLH(byte[] b) {
        int mask=0xff;
        int temp;
        int n=0;
        for (byte aB : b) {
            n <<= 8;
            temp = aB & mask;
            n |= temp;
        }
        return n;
    }

    public static byte[] subBytes(byte[] src, int begin, int count) {
        byte[] bs = new byte[count];
        System.arraycopy(src, begin, bs, 0, count);
        return bs;
    }

    public static byte[] mergeBytes(byte[][] list){
        int length = 0;
        for(byte[] bytes:list){
            length += bytes.length;
        }
        byte[] dist = new byte[length];
        int temp = 0;
        for (byte[] bytes:list){
            System.arraycopy(bytes,0,dist,temp,bytes.length);
            temp+=bytes.length;
        }
        return dist;
    }
}
