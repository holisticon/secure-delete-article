package de.holisticon.reference.data

import de.holisticon.reference.rest.model.UserDto
import org.mapstruct.InheritInverseConfiguration
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import javax.persistence.*

@Entity
@Table(name = "user")
data class User(
    @Id
    var id: String? = null,
    var name: String,
    @OneToMany(fetch = FetchType.LAZY)
    var toDoItems: List<ToDoItem>) {
  constructor() : this(null,"", emptyList())
}

@Mapper(componentModel = "spring")
interface UserConverter {

  @Mappings(
      Mapping(source = "id", target = "userId"),
      Mapping(source = "name", target = "name")
  )
  fun toDto(entity: User): UserDto
  fun toDtos(users: Iterable<User>): List<UserDto>

  @InheritInverseConfiguration
  fun toEntity(dto: UserDto): User

  fun integerListToStringList(integers: List<Int>): List<String>

}
