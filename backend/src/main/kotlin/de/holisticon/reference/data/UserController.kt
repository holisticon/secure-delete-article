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
    private val userService: UserService,
    private val userConverter: UserConverter
): UserApi {

  companion object {
    val log = LoggerFactory.getLogger(UserController::class.java)!!
  }

  override fun createUser(@PathVariable userId: String, @RequestBody userDto: UserDto): ResponseEntity<UserDto> {
    val user = userConverter.toEntity(userDto)
    val savedUser = userService.create(user)
    val responseDto = userConverter.toDto(savedUser)
    log.info("created user with ID: {}", responseDto.userId)
    return ResponseEntity.ok(responseDto)
  }

  override fun getUser(@PathVariable userId: String): ResponseEntity<UserDto> {
    val entity = userService.find(userId.toLong())
    if (!entity.isPresent) {
      return ResponseEntity.notFound().build()
    }
    val response = userConverter.toDto(entity.get())
    return ResponseEntity.ok(response)
  }
}
