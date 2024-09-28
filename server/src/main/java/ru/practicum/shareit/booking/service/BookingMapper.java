package ru.practicum.shareit.booking.service;

import org.mapstruct.Mapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    Booking toBooking(BookingRequestDto bookingRequestDto);

    BookingDto toBookingDto(Booking booking);
}
