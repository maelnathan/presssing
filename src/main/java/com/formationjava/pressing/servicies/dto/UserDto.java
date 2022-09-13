package com.formationjava.pressing.servicies.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.formationjava.pressing.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDto {
    private Long id;

    @NotBlank
    @Size(min = 1, max = 50)
    private String login;

    @Size(min = 4, max = 256)
    private String password;

    @Email
    @Size(min = 5, max = 254)
    private String email;

    private boolean activated = false;

    private Integer version;

    private Set<Role> roles;
}
