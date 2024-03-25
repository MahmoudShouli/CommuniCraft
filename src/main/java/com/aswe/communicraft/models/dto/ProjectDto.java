package com.aswe.communicraft.models.dto;


import com.aswe.communicraft.models.entities.UserEntity;
import com.aswe.communicraft.models.enums.Craft;
import com.aswe.communicraft.models.enums.Role;
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

    private int numberOfTasks;

    //private List<UserEntity> craftsmenList;
}
