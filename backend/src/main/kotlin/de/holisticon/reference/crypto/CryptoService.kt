package de.holisticon.reference.crypto

import org.springframework.stereotype.Service

@Service
class CryptoService {

  fun encrypt(userId: String, data: String): String {
    // @TODO: Implement actual encryption
    return data
  }

  fun decrypt(userId: String, cipherText: String): String {
    // @TODO: Implement actual decryption
    return cipherText
  }
}
