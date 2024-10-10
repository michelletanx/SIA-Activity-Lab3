package com.example.ToDoList.controllers;

import com.example.ToDoList.models.Person;
import com.example.ToDoList.models.TodoItem;
import com.example.ToDoList.services.PersonService;
import com.example.ToDoList.services.TodoItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class TodoFormController {

    @Autowired
    private TodoItemService todoItemService;

    @Autowired
    private PersonService personService;
    @GetMapping("/")
    public String showIndex(Model model) {
        List<TodoItem> todoItems = StreamSupport.stream(todoItemService.getAll().spliterator(), false)
                                                .collect(Collectors.toList());
        List<Person> persons = personService.findAll();

        model.addAttribute("todoItems", todoItems);
        model.addAttribute("persons", persons);

        return "index";
    }

    @GetMapping("/create-todo")
    public String showCreateForm(TodoItem todoItem, Model model) {
        List<Person> persons = personService.findAll();
        model.addAttribute("persons", persons);
        return "new-todo-item";
    }

    @PostMapping("/todo")
    public String createTodoItem(@RequestParam String personName, 
                                  @Valid TodoItem todoItem, 
                                  BindingResult result, 
                                  Model model) {
        if (result.hasErrors()) {
            return "new-todo-item";
        }

        Optional<Person> personOptional = personService.findByName(personName);
        if (personOptional.isPresent()) {
            todoItem.setPerson(personOptional.get());
        } else {
            Person newPerson = new Person();
            newPerson.setName(personName);
            personService.save(newPerson);
            todoItem.setPerson(newPerson);
        }

        todoItem.setIsComplete(false);
        todoItemService.save(todoItem);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deleteTodoItem(@PathVariable("id") Long id, Model model) {
        todoItemService.delete(id);
        return "redirect:/";
    }

    @PutMapping("/api/todos/{id}")
    @ResponseBody
    public TodoItem updateTodoItem(@PathVariable Long id, @RequestBody TodoItem updatedItem) {
        TodoItem todoItem = todoItemService.getById(id)
                .orElseThrow(() -> new IllegalArgumentException("TodoItem id: " + id + " not found"));

        todoItem.setIsComplete(updatedItem.getIsComplete());
        todoItemService.save(todoItem);
        return todoItem;
    }

    @GetMapping("/filter")
    public String filterTodoItems(@RequestParam(required = false) String taskDescription,
                                   @RequestParam(required = false) Long personId,
                                   @RequestParam(required = false) Boolean isComplete,
                                   @RequestParam(required = false) String completionDate,
                                   Model model) {
        LocalDate parsedCompletionDate = null;

        if (completionDate != null && !completionDate.isEmpty()) {
            try {
                parsedCompletionDate = LocalDate.parse(completionDate);
            } catch (DateTimeParseException e) {
                parsedCompletionDate = null; 
            }
        }

        List<TodoItem> filteredItems = todoItemService.filterTodoItems(taskDescription, personId, isComplete, parsedCompletionDate);
        model.addAttribute("todoItems", filteredItems);
        model.addAttribute("persons", personService.findAll());

        return "index";
    }
}
