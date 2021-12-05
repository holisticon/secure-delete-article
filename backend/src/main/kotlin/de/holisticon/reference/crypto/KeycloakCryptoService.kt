package de.holisticon.reference.crypto

import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class KeycloakCryptoService(
  private val restTemplate: KeycloakRestTemplate
) : CryptoService {

  companion object {
    // TODO use from config
    private const val KEYCLOAK_URL = "http://localhost:9090/auth/realms/app"
    const val ENCRYPT_ENDPOINT = "$KEYCLOAK_URL/gdpr/encrypt/"
    const val DECRYPT_ENDPOINT = "$KEYCLOAK_URL/gdpr/decrypt/"
  }

  override fun encrypt(userId: String, data: String): String {
    val encryptedData = restTemplate.postForObject(ENCRYPT_ENDPOINT, DecryptedData(userId, data), EncryptedData::class.java)
    return encryptedData.cipherText
  }

  override fun decrypt(userId: String, cipherText: String): String {
    val encryptedData = restTemplate.postForObject(DECRYPT_ENDPOINT, EncryptedData(userId, cipherText), DecryptedData::class.java)
    return encryptedData.data
  }
}

data class EncryptedData(
    val userId: String,
    val cipherText: String
)

data class DecryptedData(
    val userId: String,
    val data: String
)
