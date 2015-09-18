package co.fastcat.shopid.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MessageDigestUtils {

	protected static final Logger logger = LoggerFactory.getLogger(MessageDigestUtils.class);

	public static String getMD5String(String str) {
		try {
			return getMessageDigestString(MessageDigest.getInstance("MD5"), str);
		} catch (NoSuchAlgorithmException e) {
			logger.error("", e);
			return null;
		}
	}

	public static String getSHA1String(String str) {
		try {
			return getMessageDigestString(MessageDigest.getInstance("SHA-1"), str);
		} catch (NoSuchAlgorithmException e) {
			logger.error("", e);
			return null;
		}
	}

	public static String getSHA256String(String str) {
		try {
			return getMessageDigestString(MessageDigest.getInstance("SHA-256"), str);
		} catch (NoSuchAlgorithmException e) {
			logger.error("", e);
			return null;
		}
	}

	public static String getMessageDigestString(MessageDigest md, String str) {
		return getMessageDigestString(md, str.getBytes());
	}

	public static String getMessageDigestString(MessageDigest md, byte[] bytes) {
		md.update(bytes);
		byte byteData[] = md.digest();
		return toHexString(byteData);
	}

	public static String toHexString(byte[] byteData) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < byteData.length; i++) {
			String hex = Integer.toHexString(byteData[i] & 0xff);
			if (hex.length() == 1) {
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString();
	}

	public static String getMD5Checksum(File file) {
		byte[] byteData = new byte[0];
		try {
			byteData = createMD5Checksum(file);
		} catch (Exception e) {
			return null;
		}
		return toHexString(byteData);
	}

	public static byte[] createMD5Checksum(File file) throws Exception {
		InputStream fis =  new FileInputStream(file);

		byte[] buffer = new byte[1024];
		MessageDigest complete = MessageDigest.getInstance("MD5");
		int numRead;

		do {
			numRead = fis.read(buffer);
			if (numRead > 0) {
				complete.update(buffer, 0, numRead);
			}
		} while (numRead != -1);

		fis.close();
		return complete.digest();
	}
}
