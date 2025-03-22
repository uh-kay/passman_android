package com.example.passman

import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

data class EncryptedData(
    val encryptedBytes: ByteArray,
    val iv: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EncryptedData

        if (!encryptedBytes.contentEquals(other.encryptedBytes)) return false
        if (!iv.contentEquals(other.iv)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = encryptedBytes.contentHashCode()
        result = 31 * result + iv.contentHashCode()
        return result
    }
}

class PasswordHelper {
    companion object {
        private const val ITERATIONS = 10000
        private const val KEY_LENGTH = 256 // Key length in bits
        private const val SALT_LENGTH = 16 // Salt length in bytes

        fun generateSalt(): ByteArray {
            val random = SecureRandom()
            val salt = ByteArray(SALT_LENGTH)
            random.nextBytes(salt)
            return salt
        }

        fun hashPassword(password: String, salt: ByteArray): ByteArray {
            val spec = PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH)
            val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
            return factory.generateSecret(spec).encoded
        }

//        fun deriveEncryptionKey(masterPassword: String, salt: ByteArray): SecretKey {
//            val spec = PBEKeySpec(masterPassword.toCharArray(), salt, ITERATIONS, KEY_LENGTH)
//            val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
//            val keyBytes = factory.generateSecret(spec).encoded
//            return SecretKeySpec(keyBytes, "AES")
//        }
//
//        fun encryptPassword(password: String, encryptionKey: SecretKey): EncryptedData {
//            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
//            val iv = ByteArray(12)
//            SecureRandom().nextBytes(iv)
//
//            cipher.init(Cipher.ENCRYPT_MODE, encryptionKey, GCMParameterSpec(128, iv))
//            val encryptedBytes = cipher.doFinal(password.toByteArray())
//
//            return EncryptedData(encryptedBytes, iv)
//        }
//
//        @OptIn(ExperimentalEncodingApi::class)
//        fun createdEncryptedDataFromBase64(encryptedData: String, ivData: String): EncryptedData {
//            val encryptedBytes = Base64.decode(encryptedData)
//            val iv = Base64.decode(ivData)
//            return EncryptedData(encryptedBytes, iv)
//        }
//
//
//        fun decryptPassword(encryptedData: EncryptedData, encryptionKey: SecretKey): String {
//            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
//            cipher.init(Cipher.DECRYPT_MODE, encryptionKey, GCMParameterSpec(128, encryptedData.iv))
//            val decryptedBytes = cipher.doFinal(encryptedData.encryptedBytes)
//
//            return String(decryptedBytes)
//        }
    }
}