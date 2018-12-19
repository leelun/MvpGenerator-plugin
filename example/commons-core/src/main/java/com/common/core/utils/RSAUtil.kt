package com.common.core.utils

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.security.KeyFactory
import java.security.NoSuchAlgorithmException
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec

import javax.crypto.Cipher

import android.annotation.SuppressLint
import android.util.Base64
import java.nio.charset.Charset

/**
 * rsa加密工具
 */
object RSAUtil {
    private val RSA_PUBLICE = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDLpXlWIdsifQJDyG5i8zbSjjw+t98MgL6Ktp5IkG85quGYk+GQLkASH3+DOPACcFytfCrj927r3DtMjgUwOsE5uGKsnv7dPxtjq/jd3j8XvsjdBdK56rdsLnj8soVI8xfcaZLiz39QnTGxbqvh9SbVnzhgVZgjBwdG8XroGPDt1wIDAQAB"
    private val RSA_PRIVATE = ""
    private val ALGORITHM = "RSA"

    /**
     * 得到公钥
     *
     * @param algorithm
     * @param bysKey
     * @return
     */
    @Throws(NoSuchAlgorithmException::class, Exception::class)
    private fun getPublicKeyFromX509(algorithm: String,
                                     bysKey: String): PublicKey {
        val decodedKey = Base64.decode(bysKey, Base64.DEFAULT)
        val x509 = X509EncodedKeySpec(decodedKey)

        val keyFactory = KeyFactory.getInstance(algorithm)
        return keyFactory.generatePublic(x509)
    }

    /**
     * 得到私钥
     *
     * @param algorithm
     * @param bysKey
     * @return
     */
    @Throws(NoSuchAlgorithmException::class, Exception::class)
    private fun getPrivateKeyFromX509(algorithm: String,
                                      bysKey: String): PrivateKey {
        val decodedKey = Base64.decode(bysKey, Base64.DEFAULT)
        val pkcs8KeySpec = PKCS8EncodedKeySpec(decodedKey)
        val keyFactory = KeyFactory.getInstance(algorithm)
        return keyFactory.generatePrivate(pkcs8KeySpec)
    }

    /**
     * 使用公钥加密
     *
     * @param content
     * @return
     */
    @SuppressLint("TrulyRandom")
    fun encryptByPublic(content: String): String? {
        try {
            val pubkey = getPublicKeyFromX509(ALGORITHM, RSA_PUBLICE)

            val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
            cipher.init(Cipher.ENCRYPT_MODE, pubkey)

            val data = content.toByteArray(charset("UTF-8"))
            val blockSize = cipher.blockSize
            val outputSize = cipher.getOutputSize(data.size)// 获得加密块加密后块大小
            val leavedSize = data.size % blockSize
            val blocksSize = if (leavedSize != 0)
                data.size / blockSize + 1
            else
                data.size / blockSize
            val raw = ByteArray(outputSize * blocksSize)
            var i = 0
            while (data.size - i * blockSize > 0) {
                if (data.size - i * blockSize > blockSize)
                    cipher.doFinal(data, i * blockSize, blockSize, raw, i * outputSize)
                else
                    cipher.doFinal(data, i * blockSize, data.size - i * blockSize, raw, i * outputSize)
                // 这里面doUpdate方法不可用，查看源代码后发现每次doUpdate后并没有什么实际动作除了把byte[]放到ByteArrayOutputStream中
                // ，而最后doFinal的时候才将所有的byte[]进行加密，可是到了此时加密块大小很可能已经超出了OutputSize所以只好用dofinal方法。
                i++
            }
            return String(Base64.encode(raw, Base64.DEFAULT))

        } catch (e: Exception) {
            return null
        }

    }

    /**
     * 使用公钥解密
     *
     * @param content
     * 密文
     * @return 解密后的字符串
     */
    fun decryptByPublic(content: String): String? {
        try {
            val pubkey = getPublicKeyFromX509(ALGORITHM, RSA_PUBLICE)
            val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
            cipher.init(Cipher.DECRYPT_MODE, pubkey)
            val ins = ByteArrayInputStream(Base64.decode(content,
                    Base64.DEFAULT))
            val writer = ByteArrayOutputStream()
            val buf = ByteArray(128)

            while (true) {
                var bufl: Int = ins.read(buf)
                if (bufl == -1) break;
                var block: ByteArray? = null
                if (buf.size == bufl) {
                    block = buf
                } else {
                    block = ByteArray(bufl)
                    for (i in 0 until bufl) {
                        block[i] = buf[i]
                    }
                }
                writer.write(cipher.doFinal(block))
            }
            return String(writer.toByteArray(), Charset.forName("utf-8"))
        } catch (e: Exception) {
            return null
        }

    }

    /**
     * 使用私钥加密
     *
     * @param content
     * @return
     */
    @SuppressLint("TrulyRandom")
    fun encryptByPrivate(content: String): String? {
        try {
            val prikey = getPrivateKeyFromX509(ALGORITHM, RSA_PRIVATE)

            val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
            cipher.init(Cipher.ENCRYPT_MODE, prikey)

            val data = content.toByteArray(charset("UTF-8"))
            val blockSize = cipher.blockSize
            val outputSize = cipher.getOutputSize(data.size)// 获得加密块加密后块大小
            val leavedSize = data.size % blockSize
            val blocksSize = if (leavedSize != 0)
                data.size / blockSize + 1
            else
                data.size / blockSize
            val raw = ByteArray(outputSize * blocksSize)
            var i = 0
            while (data.size - i * blockSize > 0) {
                if (data.size - i * blockSize > blockSize)
                    cipher.doFinal(data, i * blockSize, blockSize, raw, i * outputSize)
                else
                    cipher.doFinal(data, i * blockSize, data.size - i * blockSize, raw, i * outputSize)
                // 这里面doUpdate方法不可用，查看源代码后发现每次doUpdate后并没有什么实际动作除了把byte[]放到ByteArrayOutputStream中
                // ，而最后doFinal的时候才将所有的byte[]进行加密，可是到了此时加密块大小很可能已经超出了OutputSize所以只好用dofinal方法。
                i++
            }
            return String(Base64.encode(raw, Base64.DEFAULT))

        } catch (e: Exception) {
            return null
        }

    }

    /**
     * 使用私钥解密
     *
     * @param content
     * 密文
     * @return 解密后的字符串
     */
    fun decryptByPrivate(content: String): String? {
        try {
            val prikey = getPrivateKeyFromX509(ALGORITHM, RSA_PRIVATE)
            val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
            cipher.init(Cipher.DECRYPT_MODE, prikey)
            val ins = ByteArrayInputStream(Base64.decode(content,
                    Base64.DEFAULT))
            val writer = ByteArrayOutputStream()
            val buf = ByteArray(128)

            while (true) {
                var bufl: Int = ins.read(buf)
                if (bufl == -1) break;
                var block: ByteArray? = null
                if (buf.size == bufl) {
                    block = buf
                } else {
                    block = ByteArray(bufl)
                    for (i in 0 until bufl) {
                        block[i] = buf[i]
                    }
                }
                writer.write(cipher.doFinal(block))
            }
            return String(writer.toByteArray(), Charset.forName("utf-8"))
        } catch (e: Exception) {
            return null
        }

    }
}