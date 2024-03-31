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
@Table(name = "projects_crafts")
public class ProjectCraft {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    private ProjectEntity project;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    private CraftEntity craft;



}
