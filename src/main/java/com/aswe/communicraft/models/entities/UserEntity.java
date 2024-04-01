package com.aswe.communicraft.models.entities;

import com.aswe.communicraft.models.enums.Role;
import com.aswe.communicraft.models.enums.Skill;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false,unique = true)
    private String userName;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column()
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "craft_id")
    private CraftEntity craft;

    @ManyToOne
    @JoinColumn(name = "project_id")
    @JsonIgnore
    private ProjectEntity project;

    @Column(columnDefinition = "boolean default false")
    private boolean isDeleted;

    @Column()
    @Enumerated(EnumType.STRING)
    private Skill levelOfSkill;

    @Column(columnDefinition = "boolean default false")
    private boolean isLeader;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "task_id")
    private TaskEntity task;

}