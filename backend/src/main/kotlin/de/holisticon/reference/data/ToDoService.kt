package de.holisticon.reference.data

import de.holisticon.reference.crypto.CryptoService
import org.springframework.stereotype.Service

@Service
class ToDoService(
    private val toDoItemRepository: ToDoItemRepository,
    private val cryptoService: CryptoService
) {

  fun getToDoList(userId: Long): List<ToDoItem> {
    val encryptedItems = toDoItemRepository.findAllByUserId(userId)
    return encryptedItems.map {item -> this.decrypt(item)}
  }

  fun createEntry(item: ToDoItem): ToDoItem {
    val encryptedItem = encrypt(item)
    val savedItem = toDoItemRepository.save(encryptedItem)
    return decrypt(savedItem)
  }

  private fun encrypt(toDo: ToDoItem): ToDoItem {
    val encryptedTitle = cryptoService.encrypt(toDo.user.id.toString(), toDo.title)
    val encryptedDescription = cryptoService.encrypt(toDo.user.id.toString(), toDo.description)
    return ToDoItem(toDo.id, toDo.user, encryptedTitle, encryptedDescription, toDo.done)
  }

  private fun decrypt(toDo: ToDoItem): ToDoItem {
    val decryptedTitle = cryptoService.decrypt(toDo.user.id.toString(), toDo.title)
    val decryptedDescription = cryptoService.decrypt(toDo.user.id.toString(), toDo.description)
    return ToDoItem(toDo.id, toDo.user, decryptedTitle, decryptedDescription, toDo.done)
  }
}
