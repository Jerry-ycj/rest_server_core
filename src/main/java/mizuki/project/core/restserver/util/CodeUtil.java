package mizuki.project.core.restserver.util;

import java.security.MessageDigest;
import java.util.Base64;

/**
 * 编码
 */
public class CodeUtil {

	public static String md5(String str) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			str = buf.toString();
		} catch (Exception e) {
			e.printStackTrace();

		}
		return str;
	}

    /**
     * 任意字符 base64编码
     */
	public static String base64UrlEncode(String src){
        return src==null?null : new String(Base64.getUrlEncoder().encode(src.getBytes()));
    }

    public static String base64UrlDecode(String src){
        return src==null?null : new String(Base64.getUrlDecoder().decode(src.getBytes()));
    }


}
