package com.example.ToDoList.services;

import com.example.ToDoList.models.Person;
import com.example.ToDoList.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    public void save(Person person) {
        personRepository.save(person);
    }

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public Optional<Person> findByName(String name) {
        return personRepository.findByName(name);
    }
}
