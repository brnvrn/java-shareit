package ru.practicum.shareit.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "name")
    @NotBlank
    @Size(max = 255, message = "Имя не должно быть больше 255 символов")
    String name;
    @Column(name = "email")
    @NotBlank
    @Email
    @Size(max = 50, message = "Эмейл не должен быть больше 50 символов")
    String email;
}
