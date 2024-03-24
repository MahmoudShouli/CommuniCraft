package com.aswe.communicraft.models.dto;

import com.aswe.communicraft.models.enums.Crafts;
import com.aswe.communicraft.models.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {
    private String userName;
    private String email;
    private String password;
    private Roles role;
    private Crafts craft;
    private String levelOfSkill;
}
