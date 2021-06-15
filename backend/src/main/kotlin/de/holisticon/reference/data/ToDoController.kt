package de.holisticon.reference.data

import de.holisticon.reference.rest.api.TodoApi
import de.holisticon.reference.rest.model.ToDoItemDto
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ToDoController(
    private val toDoService: ToDoService,
    private val toDoItemConverter: ToDoItemConverter
): TodoApi {

  companion object {
    val log = LoggerFactory.getLogger(ToDoController::class.java)!!
  }

  override fun getToDoList(@PathVariable userId: String): ResponseEntity<List<ToDoItemDto>> {
    val items = toDoService.getToDoList(userId.toLong())
    val dtos = toDoItemConverter.toDtos(items)
    return ResponseEntity.ok(dtos)
  }

  override fun addEntry(@PathVariable userId: String, @RequestBody toDoItem: ToDoItemDto): ResponseEntity<ToDoItemDto> {
    val user = User(userId.toLong(), "", emptyList())
    val entity = toDoItemConverter.toEntity(user, toDoItem)
    val saved = toDoService.createEntry(entity)
    val response = toDoItemConverter.toDto(saved)
    log.info("created todo entry for user ID: {}", userId)
    return ResponseEntity.ok(response)
  }
}
