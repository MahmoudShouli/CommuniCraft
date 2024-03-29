package com.aswe.communicraft.models.dto;

import com.aswe.communicraft.models.entities.CraftEntity;
import com.aswe.communicraft.models.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Role role;
    @JsonIgnore
    private CraftDto craft;
    private String levelOfSkill;
}
