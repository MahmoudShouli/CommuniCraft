package com.aswe.communicraft.models.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    private String userName;
    private String password;
}
