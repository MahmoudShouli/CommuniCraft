package com.aswe.communicraft.models.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tasks")
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false,unique = true)
    private String name;


    @Column(columnDefinition = "boolean default false")
    private boolean isFinished;

    @ManyToOne
    @JoinColumn(name = "project_id")
    @JsonIgnore
    private ProjectEntity project;

}
