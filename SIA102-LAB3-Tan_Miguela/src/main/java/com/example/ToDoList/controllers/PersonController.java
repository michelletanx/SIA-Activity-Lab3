package com.example.ToDoList.controllers;

import com.example.ToDoList.models.Person;
import com.example.ToDoList.models.TodoItem;
import com.example.ToDoList.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
public class PersonController {

    @Autowired
    private PersonService personService;
    
    @GetMapping("/add-person")
    public String showAddPersonForm(@RequestParam(value = "description", required = false) String description, 
                                     Model model) {
        model.addAttribute("person", new Person());
        model.addAttribute("description", description);
        return "add-person"; 
    }

    @PostMapping("/person")
    public String addPerson(@Valid Person person, BindingResult result,
                            @RequestParam("description") String description, Model model) {
        if (result.hasErrors()) {
            return "add-person";
        }
        personService.save(person);

        model.addAttribute("persons", personService.findAll());
        model.addAttribute("todoItem", new TodoItem()); // Keep if needed on the next page
        model.addAttribute("description", description);

        return "new-todo-item";
    }
}
