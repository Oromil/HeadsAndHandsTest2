package com.oromil.hhtest.utils

import com.oromil.hhtest.data.entities.UserAccount
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

private const val ALGORITHM = "AES"
private const val MIN_KEY_SIZE = 16

private fun encrypt(data: ByteArray, key: ByteArray): ByteArray {

    val keyLength = if (key.size < MIN_KEY_SIZE) MIN_KEY_SIZE else key.size
    val encryptKey = ByteArray(keyLength)
    System.arraycopy(key, 0, encryptKey, 0, key.size)
    val cipher = Cipher.getInstance(ALGORITHM)
    val secretKeySpec = SecretKeySpec(encryptKey, ALGORITHM)
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec)
    return cipher.doFinal(data)
}


fun encryptUserAccount(userAccount: UserAccount): UserAccount {
    val encryptedEmail = encrypt(userAccount.email.toByteArray(), userAccount.password.toByteArray())
    val encryptedPassword = encrypt(userAccount.password.toByteArray(), userAccount.email.toByteArray())
    userAccount.email = String(encryptedEmail)
    userAccount.password = String(encryptedPassword)
    return userAccount
}