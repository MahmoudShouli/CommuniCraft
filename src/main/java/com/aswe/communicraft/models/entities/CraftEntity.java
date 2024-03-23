package com.aswe.communicraft.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "crafts")

public class CraftEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int craftId;

    @Column(unique = true)
    private String craftName;


}
