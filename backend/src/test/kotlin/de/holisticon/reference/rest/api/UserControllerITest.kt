package de.holisticon.reference.rest.api

import de.holisticon.reference.data.User
import de.holisticon.reference.data.UserConverter
import de.holisticon.reference.data.UserService
import de.holisticon.reference.rest.model.UserDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import java.util.Optional.of

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("itest")
open class UserControllerITest {

  @Autowired
  private lateinit var restTemplate: TestRestTemplate

  @Autowired
  private lateinit var converter: UserConverter

  @MockBean
  private lateinit var userService: UserService

  @Test
  fun `controller respond to request`() {
    // given
    val user = User(id = "1", name = "hans.wurst", toDoItems = emptyList())
    val userDto = converter.toDto(user)
    `when`(userService.find(user.id!!)).thenReturn(of(user))

    // when
    val response = restTemplate.exchange("/user/1", HttpMethod.GET, null, object : ParameterizedTypeReference<UserDto>() {})

    // then
    assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    assertThat(response.body).isEqualTo(userDto)
  }
}
