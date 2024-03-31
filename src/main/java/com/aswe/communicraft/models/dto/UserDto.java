package com.aswe.communicraft.models.dto;

import com.aswe.communicraft.models.entities.CraftEntity;
import com.aswe.communicraft.models.enums.Role;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String userName;

    private String email;

    private String password;

    private CraftDto craft;

    private String levelOfSkill;

}
