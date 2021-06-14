package de.holisticon.reference.data

import de.holisticon.reference.rest.model.ToDoItemDto
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import javax.persistence.*

@Entity
@Table(name = "todo")
data class ToDoItem(
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    var user: User,
    var title: String,
    var description: String,
    var done: Boolean) {
  constructor() : this(null, User(),"", "", false)
}

@Mapper(componentModel = "spring")
interface ToDoItemConverter {

  fun toDto(entity: ToDoItem): ToDoItemDto
  fun toDtos(items: Iterable<ToDoItem>): List<ToDoItemDto>

  @Mappings(
      Mapping(source = "user", target = "user"),
      Mapping(source = "dto.id", target = "id"),
      Mapping(source = "dto.title", target = "title"),
      Mapping(source = "dto.description", target = "description"),
      /*
       * TODO: swagger codegen generates a weird method without "get" or "is" prefix for booleans. MapStruct doesn't like.
       */
      Mapping(expression = "java(dto.Done() == null ? false : dto.Done())", target = "done"),
  )
  fun toEntity(user: User, dto: ToDoItemDto): ToDoItem

  fun integerListToStringList(integers: List<Int>): List<String>

}
