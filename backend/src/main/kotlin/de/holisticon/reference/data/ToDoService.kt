package de.holisticon.reference.data

import org.springframework.stereotype.Service

@Service
class ToDoService(
    private val toDoItemRepository: ToDoItemRepository
) {

  fun getToDoList(userId: Long): List<ToDoItem> {
    return toDoItemRepository.findAllByUserId(userId)
  }

  fun createEntry(item: ToDoItem): ToDoItem {
    return toDoItemRepository.save(item)
  }
}
