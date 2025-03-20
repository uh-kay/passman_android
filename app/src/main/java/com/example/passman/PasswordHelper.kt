package com.example.passman

import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

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
    }
}