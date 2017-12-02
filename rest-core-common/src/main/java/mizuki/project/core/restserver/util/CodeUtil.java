package mizuki.project.core.restserver.util;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
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


	/**
	 * DES
	 */

	public static byte[] DESEncrypt(byte[] content, String password)
			throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
		Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");// 创建密码器
		cipher.init(Cipher.ENCRYPT_MODE, getDESSecretKey(password),new SecureRandom());
		return cipher.doFinal(content);
	}

	public static byte[] DESDecrypt(byte[] content, String password)
			throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
		Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");// 创建密码器
		cipher.init(Cipher.DECRYPT_MODE, getDESSecretKey(password),new SecureRandom());
		return cipher.doFinal(content);
	}

	private static SecretKey getDESSecretKey(final String password) throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException {
		SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DES");
		KeySpec keySpec = new DESKeySpec(password.getBytes());
		SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
		return secretKey;
	}

}
