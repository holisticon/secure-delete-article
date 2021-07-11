package de.holisticon.reference.crypto

// tag::crypto-service[]
interface CryptoService {

  fun encrypt(userId: String, data: String): String

  fun decrypt(userId: String, cipherText: String): String
}
// end::crypto-service[]
