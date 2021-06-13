package de.holisticon.reference.data

import de.holisticon.reference.rest.api.UserApi
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userRepository: UserRepository,
    private val userConverter: UserConverter
): UserApi {

}
