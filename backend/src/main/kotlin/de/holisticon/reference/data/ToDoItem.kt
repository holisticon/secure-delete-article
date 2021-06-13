package de.holisticon.reference.data

import de.holisticon.reference.rest.model.ToDoItemDto
import org.mapstruct.InheritInverseConfiguration
import org.mapstruct.Mapper
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "todo")
data class ToDoItem(
    @Id
    var id: Long? = null,
    val title: String,
    val description: String,
    val done: Boolean) {
  constructor() : this(null, "", "", false)
}

@Mapper(componentModel = "spring")
interface ToDoItemConverter {

  fun toDto(entity: ToDoItem): ToDoItemDto
  fun toDtos(items: Iterable<ToDoItem>): List<ToDoItemDto>

  @InheritInverseConfiguration
  fun toEntity(dto: ToDoItemDto): ToDoItem

  fun integerListToStringList(integers: List<Int>): List<String>

}
