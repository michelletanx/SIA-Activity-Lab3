package com.example.ToDoList.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "todo_items")
public class TodoItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Description is required")
    private String description;

    private Boolean isComplete;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")  
    private Person person;

    private LocalDate completionDate;

    @Override
    public String toString() {
        return String.format("TodoItem{id=%d, description='%s', isComplete='%s', person='%s', completionDate='%s'}",
                id, description, isComplete, person != null ? person.getName() : "No person assigned", 
                completionDate != null ? completionDate : "No completion date");
    }
}
