package de.holisticon.reference.data

import de.holisticon.reference.crypto.CryptoService
import org.springframework.stereotype.Service

// tag::decrypt-data[]
@Service
class ToDoService(
    private val toDoItemRepository: ToDoItemRepository,
    private val cryptoService: CryptoService
) {

  fun getToDoList(userId: String): List<ToDoItem> {
    val encryptedItems = toDoItemRepository.findAllByUserId(userId)
    return encryptedItems.map {item -> this.decrypt(item)}
  }

  private fun decrypt(toDo: ToDoItem): ToDoItem {
    val decryptedTitle = cryptoService.decrypt(toDo.user.id!!, toDo.title)
    val decryptedDescription = cryptoService.decrypt(toDo.user.id!!, toDo.description)
    return ToDoItem(toDo.id, toDo.user, decryptedTitle, decryptedDescription, toDo.done)
  }
  // end::decrypt-data[]

  fun createEntry(item: ToDoItem): ToDoItem {
    val encryptedItem = encrypt(item)
    val savedItem = toDoItemRepository.save(encryptedItem)
    return decrypt(savedItem)
  }

  private fun encrypt(toDo: ToDoItem): ToDoItem {
    val encryptedTitle = cryptoService.encrypt(toDo.user.id!!, toDo.title)
    val encryptedDescription = cryptoService.encrypt(toDo.user.id!!, toDo.description)
    return ToDoItem(toDo.id, toDo.user, encryptedTitle, encryptedDescription, toDo.done)
  }

}
