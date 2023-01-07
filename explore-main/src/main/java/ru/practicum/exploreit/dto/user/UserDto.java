package ru.practicum.exploreit.dto.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserDto {
    @NotBlank
    private String name;
    @NotBlank
    @Email
    private String email;
}
