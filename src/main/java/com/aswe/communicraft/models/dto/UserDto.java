package com.aswe.communicraft.models.dto;

import com.aswe.communicraft.models.enums.Crafts;
import com.aswe.communicraft.models.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String userName;

    private String email;

    private String password;

    private Crafts craft;

    private Roles role;

    private String levelOfSkill;

}
