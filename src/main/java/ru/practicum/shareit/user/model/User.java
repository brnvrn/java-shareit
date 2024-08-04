package ru.practicum.shareit.user.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    long id;
    @NotBlank
    String name;
    @NotBlank
    @Email
    String email;
}
