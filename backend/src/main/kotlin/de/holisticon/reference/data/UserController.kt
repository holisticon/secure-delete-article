package de.holisticon.reference.data

import de.holisticon.reference.rest.api.UserApi
import de.holisticon.reference.rest.model.UserDto
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userRepository: UserRepository,
    private val userConverter: UserConverter
): UserApi {

  companion object {
    val log = LoggerFactory.getLogger(UserController::class.java)!!
  }

  override fun createUser(@PathVariable userId: String, @RequestBody user: UserDto): ResponseEntity<UserDto> {
    val entity = userConverter.toEntity(user)
    val result = userRepository.save(entity)
    val response = userConverter.toDto(result)
    log.info("created user with ID: {}", response.userId)
    return ResponseEntity.ok(response)
  }

  override fun getUser(@PathVariable userId: String): ResponseEntity<UserDto> {
    val entity = userRepository.findById(userId.toLong())
    if (!entity.isPresent) {
      return ResponseEntity.notFound().build()
    }
    val response = userConverter.toDto(entity.get())
    return ResponseEntity.ok(response)
  }
}
