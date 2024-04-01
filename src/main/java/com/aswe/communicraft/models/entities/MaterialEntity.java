package com.aswe.communicraft.models.entities;

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
@Table(name = "materials")
public class MaterialEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column()
    private String name;

    @Column(columnDefinition = "boolean default false")
    private boolean isUsed;

    @ManyToOne
    @JoinColumn(name = "user_id")
    UserEntity user;

}
