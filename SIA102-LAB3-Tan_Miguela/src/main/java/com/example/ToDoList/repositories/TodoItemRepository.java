package com.example.ToDoList.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ToDoList.models.TodoItem;

@Repository
public interface TodoItemRepository extends JpaRepository<TodoItem, Long> {
    List<TodoItem> findByPerson_Id(Long personId);
}

