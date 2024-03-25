package com.aswe.communicraft.models.entities;

import com.aswe.communicraft.models.enums.Craft;
import com.aswe.communicraft.models.enums.Role;
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

    @Column()
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column()
    @Enumerated(EnumType.STRING)
    private Craft craft;

    @Column(columnDefinition = "boolean default false")
    private boolean isDeleted;

    @Column()
    private String levelOfSkill;

}
