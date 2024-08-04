package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class BookingDto {
    Long id;
    LocalDate start;
    LocalDate end;
    Item item;
    User booker;
    BookingStatus status;
}
