package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class User {
    Long id;
    @NotBlank
    String name;
    @NotBlank
    String email;
}
