package ru.practicum.exploreit.dto.user;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserDto {
    @NotBlank
    @Length(min = 1, max = 255)
    private String name;
    @NotBlank
    @Email
    @Length(min = 1, max = 512)
    private String email;
}
