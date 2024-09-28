package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@ToString
@Table(name = "items")
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NotBlank
    @Size(max = 255, message = "Имя не должно быть больше 255 символов")
    String name;
    @NotBlank(message = "Описание не должно быть пустым")
    @Size(max = 255, message = "Имя не должно быть больше 255 символов")
    String description;
    @NotNull
    Boolean available;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    User owner;
    @OneToMany
    @JoinColumn(name = "item_id")
    private List<Comment> comments;
    @ManyToOne
    @JoinColumn(name = "request_id")
    private ItemRequest request;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        return id != null && id.equals(((Item) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
