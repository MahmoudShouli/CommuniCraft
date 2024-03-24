package com.aswe.communicraft.models.dto;

import com.aswe.communicraft.models.enums.Craft;
import com.aswe.communicraft.models.enums.Role;
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
    private Craft craft;
    private String levelOfSkill;
}
