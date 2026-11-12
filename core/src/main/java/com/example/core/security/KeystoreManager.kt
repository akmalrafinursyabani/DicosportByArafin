package com.example.core.security

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.core.content.edit
import java.security.KeyStore
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

object KeystoreManager {
    private const val KEY_ALIAS = "DicosportRoomKey"
    private const val ANDROID_KEYSTORE = "AndroidKeyStore"
    private const val PREF_NAME = "dicosport_security_pref"
    private const val KEY_ENCRYPTED_PASSPHRASE = "encrypted_passphrase"
    private const val KEY_IV = "passphrase_iv"
    private const val TRANSFORMATION = "AES/GCM/NoPadding"

    fun getPassphrase(context: Context): ByteArray {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val encryptedPassphraseBase64 = prefs.getString(KEY_ENCRYPTED_PASSPHRASE, null)
        val ivBase64 = prefs.getString(KEY_IV, null)

        val secretKey = getOrCreateSecretKey()

        if (encryptedPassphraseBase64 != null && ivBase64 != null) {
            try {
                val encryptedPassphrase = Base64.decode(encryptedPassphraseBase64, Base64.DEFAULT)
                val iv = Base64.decode(ivBase64, Base64.DEFAULT)
                val cipher = Cipher.getInstance(TRANSFORMATION)
                val spec = GCMParameterSpec(128, iv)
                cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)
                return cipher.doFinal(encryptedPassphrase)
            } catch (_: Exception) {
                // If decryption fails, fallback to generating a new passphrase below
            }
        }

        val newPassphrase = ByteArray(32)
        SecureRandom().nextBytes(newPassphrase)

        try {
            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            val iv = cipher.iv
            val encryptedPassphrase = cipher.doFinal(newPassphrase)

            prefs.edit {
                putString(KEY_ENCRYPTED_PASSPHRASE, Base64.encodeToString(encryptedPassphrase, Base64.DEFAULT))
                putString(KEY_IV, Base64.encodeToString(iv, Base64.DEFAULT))
            }
        } catch (_: Exception) {
            return "DefaultDicosportSecurePassphrase2026".toByteArray()
        }

        return newPassphrase
    }

    private fun getOrCreateSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }

        if (!keyStore.containsAlias(KEY_ALIAS)) {
            val keyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES,
                ANDROID_KEYSTORE
            )
            val keyGenParameterSpec = KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(256)
                .build()

            keyGenerator.init(keyGenParameterSpec)
            return keyGenerator.generateKey()
        }

        val entry = keyStore.getEntry(KEY_ALIAS, null) as KeyStore.SecretKeyEntry
        return entry.secretKey
    }
}
