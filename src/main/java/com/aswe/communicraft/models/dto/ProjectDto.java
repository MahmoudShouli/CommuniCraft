package com.aswe.communicraft.models.dto;


import com.aswe.communicraft.models.entities.UserEntity;
import com.aswe.communicraft.models.enums.Skill;
import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {
    private String name;

    private int availableTeamPositions;

    private List<UserEntity> craftsmenList;

    private List<String> craftsNeeded;

    private Skill projectSkill;

}