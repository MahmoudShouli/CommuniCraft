package com.aswe.communicraft.models.dto;


import com.aswe.communicraft.models.entities.CraftEntity;
import com.aswe.communicraft.models.entities.ProjectCraft;
import com.aswe.communicraft.models.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {
    private String name;

    private int teamSize;


    private List<UserEntity> craftsmenList;

    private List<String> craftsNeeded;


}