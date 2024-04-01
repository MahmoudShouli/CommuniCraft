package com.aswe.communicraft.models.dto;


import com.aswe.communicraft.models.enums.Role;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {
    private String userName;
    private String email;
    private String password;
    private Role role;
    private CraftDto craft;
    private String levelOfSkill;
}