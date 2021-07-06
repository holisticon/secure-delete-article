package de.holisticon.reference.crypto


interface CryptoService {

  fun encrypt(userId: String, data: String): String

  fun decrypt(userId: String, cipherText: String): String
}
