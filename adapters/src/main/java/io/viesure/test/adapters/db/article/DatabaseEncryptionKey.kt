package io.viesure.test.adapters.db.article

import androidx.core.content.edit
import io.viesure.test.adapters.android.SecretSharedPreferences
import timber.log.Timber
import javax.crypto.KeyGenerator
import javax.inject.Inject
import javax.inject.Singleton

private const val PREFS_KEY = "db_encryption_key"
private const val KEY_SIZE = 256
private const val KEY_ALGORITHM = "AES"
private val HEX_CHARS = "0123456789ABCDEF".toCharArray()

@Singleton
class DatabaseEncryptionKey @Inject constructor(
    private val secretSharedPreferences: SecretSharedPreferences
) {
    private val tag = this::class.simpleName!!

    fun getOrCreate(): CharArray = get() ?: createAndPersist()

    private fun createAndPersist(): CharArray =
        generateRandomKey()
            .also {
                Timber.tag(tag).d("New encryption key created!")
                persist(it)
            }

    private fun get() = secretSharedPreferences.getString(PREFS_KEY, null)?.toCharArray()

    private fun persist(it: CharArray) {
        secretSharedPreferences.edit { putString(PREFS_KEY, String(it)) }
    }
}

private fun generateRandomKey(): CharArray =
    KeyGenerator.getInstance(KEY_ALGORITHM)
        .also { it.init(KEY_SIZE) }
        .generateKey()
        .encoded.toHexChars()

private fun ByteArray.toHexChars(): CharArray =
    with(StringBuilder(size * 2)) {
        forEach {
            val octet = it.code
            val firstIndex = (octet and 0xF0) ushr 4
            val secondIndex = octet and 0x0F
            append(HEX_CHARS[firstIndex])
            append(HEX_CHARS[secondIndex])
        }
    }.toString().toCharArray()
