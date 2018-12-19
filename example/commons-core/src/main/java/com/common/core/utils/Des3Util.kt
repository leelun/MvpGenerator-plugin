package com.common.core.utils

import android.util.Base64

import java.security.NoSuchAlgorithmException

import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESedeKeySpec
import javax.crypto.spec.IvParameterSpec

/**
 * @author liulun
 * @version V1.0
 * @Description: des3 加密解密
 * @date 2016/12/5 10:40
 */
object Des3Util {
    private val MCRYPT_TRIPLEDES = "DESede"
    private val TRANSFORMATION = "DESede/CBC/PKCS5Padding"
    private val encoding = "utf-8"
    @Throws(Exception::class)
    fun decrypt(key: String, keyiv: String, encryptStr: String): String {
        val spec = DESedeKeySpec(key.toByteArray(charset(encoding)))
        val keyFactory = SecretKeyFactory.getInstance(MCRYPT_TRIPLEDES)
        val sec = keyFactory.generateSecret(spec)
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val IvParameters = IvParameterSpec(keyiv.toByteArray(charset(encoding)))
        cipher.init(Cipher.DECRYPT_MODE, sec, IvParameters)
        return String(cipher.doFinal(Base64.decode(encryptStr, Base64.DEFAULT)), charset(encoding))
    }

    @Throws(Exception::class)
    fun encrypt(key: String, keyiv: String, plainText: String): String {
        val spec = DESedeKeySpec(key.toByteArray(charset(encoding)))
        val keyFactory = SecretKeyFactory.getInstance("DESede")
        val sec = keyFactory.generateSecret(spec)
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val IvParameters = IvParameterSpec(keyiv.toByteArray(charset(encoding)))
        cipher.init(Cipher.ENCRYPT_MODE, sec, IvParameters)
        return Base64.encodeToString(cipher.doFinal(plainText.toByteArray(charset(encoding))), Base64.DEFAULT)
    }

    @Throws(NoSuchAlgorithmException::class)
    fun generateSecretKey(): ByteArray {
        val keygen = KeyGenerator.getInstance(MCRYPT_TRIPLEDES)
        return keygen.generateKey().encoded
    }
}
