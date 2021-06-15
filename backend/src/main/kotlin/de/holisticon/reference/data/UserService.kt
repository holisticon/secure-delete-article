package de.holisticon.reference.data

import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository
) {

  fun create(user: User): User {
    return userRepository.save(user)
  }

  fun find(userId: Long): Optional<User> {
    return userRepository.findById(userId)
  }
}
