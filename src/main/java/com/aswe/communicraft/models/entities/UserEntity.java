package com.aswe.communicraft.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id")
    private RoleEntity roleEntity;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "craft_id")
    private CraftEntity craftEntity;

    @Column(columnDefinition = "boolean default false")
    private boolean isDeleted;

    @Column()
    private String levelOfSkill;


}
