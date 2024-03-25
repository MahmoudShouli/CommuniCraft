package com.aswe.communicraft.models.entities;


import com.aswe.communicraft.models.enums.Craft;
import com.aswe.communicraft.models.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "projects")
public class ProjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false,unique = true)
    private String name;

    @Column(nullable = false)
    private int teamSize;

    @Column(nullable = false)
    private int numberOfTasks;

    @Column(nullable = false)
    private boolean isFinished;



}
