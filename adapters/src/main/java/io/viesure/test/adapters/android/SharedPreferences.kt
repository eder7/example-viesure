package io.viesure.test.adapters.android

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import javax.inject.Inject
import javax.inject.Singleton

private const val PREFS_FILE_NAME_SECRET = "secret"

@Singleton
class SecretSharedPreferences @Inject constructor(applicationContext: Context) :
    SharedPreferences by createSecretSharedPreferences(applicationContext)

private fun createSecretSharedPreferences(context: Context): SharedPreferences {
    val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    return EncryptedSharedPreferences.create(
        PREFS_FILE_NAME_SECRET,
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
}
