package com.example.ToDoList.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.ToDoList.models.Person;
import com.example.ToDoList.models.TodoItem;
import com.example.ToDoList.repositories.PersonRepository;
import com.example.ToDoList.repositories.TodoItemRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TodoItemService {

    @Autowired
    private TodoItemRepository todoItemRepository;

    @Autowired
    private PersonRepository personRepository;

    public Optional<TodoItem> getById(Long id) {
        return todoItemRepository.findById(id);
    }

    public Iterable<TodoItem> getAll() {
        return todoItemRepository.findAll();
    }

    public TodoItem save(TodoItem todoItem) {
        return todoItemRepository.save(todoItem);
    }

    public void delete(Long id) {
        Optional<TodoItem> todoItemOptional = todoItemRepository.findById(id);
        if (todoItemOptional.isPresent()) {
            todoItemRepository.delete(todoItemOptional.get());
        }
    }

    public TodoItem createTodoForPerson(Long personId, TodoItem todoItem) {
        Optional<Person> person = personRepository.findById(personId);
        if (person.isPresent()) {
            todoItem.setPerson(person.get());
            return todoItemRepository.save(todoItem);
        } else {
            throw new IllegalArgumentException("Person not found");
        }
    }

    public List<TodoItem> findTasksByPerson(Long personId) {
        return todoItemRepository.findByPerson_Id(personId);
    }

    public List<TodoItem> filterTodoItems(String taskDescription, Long personId, Boolean isComplete, LocalDate completionDate) {
        List<TodoItem> todoItems = (List<TodoItem>) todoItemRepository.findAll();
        return todoItems.stream()
                .filter(item -> (taskDescription == null || item.getDescription().toLowerCase().contains(taskDescription.toLowerCase()))
                        && (personId == null || (item.getPerson() != null && item.getPerson().getId().equals(personId)))
                        && (isComplete == null || item.getIsComplete().equals(isComplete))
                        && (completionDate == null || (item.getCompletionDate() != null && item.getCompletionDate().isEqual(completionDate)))
                ).toList();
    }
}
