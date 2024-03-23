package com.aswe.communicraft.models.dto;

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
    private RoleDto role;
    private CraftDto craft;
    private String levelOfSkill;
}
