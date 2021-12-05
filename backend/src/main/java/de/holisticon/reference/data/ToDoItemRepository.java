package de.holisticon.reference.data;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ToDoItemRepository extends PagingAndSortingRepository<ToDoItem, Long> {

    List<ToDoItem> findAllByUserId(String userId);
}
