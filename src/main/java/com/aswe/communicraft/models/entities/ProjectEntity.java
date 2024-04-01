package com.aswe.communicraft.models.entities;

import com.aswe.communicraft.models.enums.Skill;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Setter
@Getter
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
    private int availableTeamPositions;

    @Column(nullable = false)
    private int numberOfTasks;

    @Column(nullable = false)
    private boolean isFinished;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Skill projectSkill;

    @OneToMany(mappedBy = "project",cascade = CascadeType.ALL)
    private List<UserEntity> craftsmenList;

    @OneToMany(mappedBy = "project",cascade = CascadeType.ALL)
    private List<ProjectCraft> projectCrafts;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<TaskEntity> tasks;
}