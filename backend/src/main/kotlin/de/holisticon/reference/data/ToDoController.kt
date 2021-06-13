package de.holisticon.reference.data

import de.holisticon.reference.rest.api.TodoApi
import org.springframework.web.bind.annotation.RestController

@RestController
class ToDoController(
    private val toDoItemRepository: ToDoItemRepository,
    private val toDoItemConverter: ToDoItemConverter
): TodoApi {
}
