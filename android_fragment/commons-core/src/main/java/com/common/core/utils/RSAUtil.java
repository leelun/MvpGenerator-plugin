package com.common.core.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import android.annotation.SuppressLint;
import android.util.Base64;

/**
 * rsa加密工具
 */
public class RSAUtil {
	private static final String RSA_PUBLICE = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDLpXlWIdsifQJDyG5i8zbSjjw+t98MgL6Ktp5IkG85quGYk+GQLkASH3+DOPACcFytfCrj927r3DtMjgUwOsE5uGKsnv7dPxtjq/jd3j8XvsjdBdK56rdsLnj8soVI8xfcaZLiz39QnTGxbqvh9SbVnzhgVZgjBwdG8XroGPDt1wIDAQAB";
	private static final String RSA_PRIVATE = "";
	private static final String ALGORITHM = "RSA";

	/**
	 * 得到公钥
	 * 
	 * @param algorithm
	 * @param bysKey
	 * @return
	 */
	private static PublicKey getPublicKeyFromX509(String algorithm,
			String bysKey) throws NoSuchAlgorithmException, Exception {
		byte[] decodedKey = Base64.decode(bysKey, Base64.DEFAULT);
		X509EncodedKeySpec x509 = new X509EncodedKeySpec(decodedKey);

		KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
		return keyFactory.generatePublic(x509);
	}

	/**
	 * 得到私钥
	 * 
	 * @param algorithm
	 * @param bysKey
	 * @return
	 */
	private static PrivateKey getPrivateKeyFromX509(String algorithm,
			String bysKey) throws NoSuchAlgorithmException, Exception {
		byte[] decodedKey = Base64.decode(bysKey, Base64.DEFAULT);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(decodedKey);
		KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
		return keyFactory.generatePrivate(pkcs8KeySpec);
	}

	/**
	 * 使用公钥加密
	 * 
	 * @param content
	 * @return
	 */
	@SuppressLint("TrulyRandom")
	public static String encryptByPublic(String content) {
		try {
			PublicKey pubkey = getPublicKeyFromX509(ALGORITHM, RSA_PUBLICE);

			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, pubkey);

			byte data[] = content.getBytes("UTF-8");
			int blockSize = cipher.getBlockSize();
			int outputSize = cipher.getOutputSize(data.length);// 获得加密块加密后块大小
			int leavedSize = data.length % blockSize;
			int blocksSize = leavedSize != 0 ? data.length / blockSize + 1
					: data.length / blockSize;
			byte[] raw = new byte[outputSize * blocksSize];
			int i = 0;
			while (data.length - i * blockSize > 0) {
				if (data.length - i * blockSize > blockSize)
					cipher.doFinal(data, i * blockSize, blockSize, raw, i
							* outputSize);
				else
					cipher.doFinal(data, i * blockSize, data.length - i
							* blockSize, raw, i * outputSize);
				// 这里面doUpdate方法不可用，查看源代码后发现每次doUpdate后并没有什么实际动作除了把byte[]放到ByteArrayOutputStream中
				// ，而最后doFinal的时候才将所有的byte[]进行加密，可是到了此时加密块大小很可能已经超出了OutputSize所以只好用dofinal方法。
				i++;
			}
			String s = new String(Base64.encode(raw, Base64.DEFAULT));
			return s;

		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 使用公钥解密
	 * 
	 * @param content
	 *            密文
	 * @return 解密后的字符串
	 */
	public static String decryptByPublic(String content) {
		try {
			PublicKey pubkey = getPublicKeyFromX509(ALGORITHM, RSA_PUBLICE);
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, pubkey);
			InputStream ins = new ByteArrayInputStream(Base64.decode(content,
					Base64.DEFAULT));
			ByteArrayOutputStream writer = new ByteArrayOutputStream();
			byte[] buf = new byte[128];
			int bufl;
			while ((bufl = ins.read(buf)) != -1) {
				byte[] block = null;
				if (buf.length == bufl) {
					block = buf;
				} else {
					block = new byte[bufl];
					for (int i = 0; i < bufl; i++) {
						block[i] = buf[i];
					}
				}
				writer.write(cipher.doFinal(block));
			}
			return new String(writer.toByteArray(), "utf-8");
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 使用私钥加密
	 * 
	 * @param content
	 * @return
	 */
	@SuppressLint("TrulyRandom")
	public static String encryptByPrivate(String content) {
		try {
			PrivateKey prikey = getPrivateKeyFromX509(ALGORITHM, RSA_PRIVATE);

			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, prikey);

			byte data[] = content.getBytes("UTF-8");
			int blockSize = cipher.getBlockSize();
			int outputSize = cipher.getOutputSize(data.length);// 获得加密块加密后块大小
			int leavedSize = data.length % blockSize;
			int blocksSize = leavedSize != 0 ? data.length / blockSize + 1
					: data.length / blockSize;
			byte[] raw = new byte[outputSize * blocksSize];
			int i = 0;
			while (data.length - i * blockSize > 0) {
				if (data.length - i * blockSize > blockSize)
					cipher.doFinal(data, i * blockSize, blockSize, raw, i
							* outputSize);
				else
					cipher.doFinal(data, i * blockSize, data.length - i
							* blockSize, raw, i * outputSize);
				// 这里面doUpdate方法不可用，查看源代码后发现每次doUpdate后并没有什么实际动作除了把byte[]放到ByteArrayOutputStream中
				// ，而最后doFinal的时候才将所有的byte[]进行加密，可是到了此时加密块大小很可能已经超出了OutputSize所以只好用dofinal方法。
				i++;
			}
			String s = new String(Base64.encode(raw, Base64.DEFAULT));
			return s;

		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 使用私钥解密
	 * 
	 * @param content
	 *            密文
	 * @return 解密后的字符串
	 */
	public static String decryptByPrivate(String content) {
		try {
			PrivateKey prikey = getPrivateKeyFromX509(ALGORITHM, RSA_PRIVATE);
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, prikey);
			InputStream ins = new ByteArrayInputStream(Base64.decode(content,
					Base64.DEFAULT));
			ByteArrayOutputStream writer = new ByteArrayOutputStream();
			byte[] buf = new byte[128];
			int bufl;
			while ((bufl = ins.read(buf)) != -1) {
				byte[] block = null;
				if (buf.length == bufl) {
					block = buf;
				} else {
					block = new byte[bufl];
					for (int i = 0; i < bufl; i++) {
						block[i] = buf[i];
					}
				}
				writer.write(cipher.doFinal(block));
			}
			return new String(writer.toByteArray(), "utf-8");
		} catch (Exception e) {
			return null;
		}
	}
}