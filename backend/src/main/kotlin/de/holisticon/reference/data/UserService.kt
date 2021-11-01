package de.holisticon.reference.data

import de.holisticon.reference.crypto.CryptoService
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository,
    private val cryptoService: CryptoService
) {

  fun create(user: User): User {
    val encryptedUser = encrypt(user)
    val savedUser = userRepository.save(encryptedUser)
    return decrypt(savedUser)
  }

  fun find(userId: String): Optional<User> {
    val encryptedUser = userRepository.findById(userId)
    return encryptedUser.map(this::decrypt)
  }

  private fun encrypt(user: User): User {
    val encryptedName = cryptoService.encrypt(user.id.toString(), user.name)
    return User(user.id, encryptedName, user.toDoItems)
  }

  private fun decrypt(user: User): User {
    val decryptedName = cryptoService.decrypt(user.id.toString(), user.name)
    return User(user.id, decryptedName, user.toDoItems)
  }
}
