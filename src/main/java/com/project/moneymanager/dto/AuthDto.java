package com.project.moneymanager.dto;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@Getter
//@Setter
public class AuthDto {

    private String email;
    private String password;
    private String token;

}
