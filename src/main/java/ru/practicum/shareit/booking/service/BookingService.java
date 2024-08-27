package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

public interface BookingService {
    BookingDto addNewBooking(Long userId, BookingRequestDto bookingRequestDto);

    BookingDto approvedOrRejectBooking(Long userId, Long bookingId, Boolean approved);

    BookingDto getBookingById(Long userId, Long bookingId);

    List<BookingDto> getAllBookingByUser(Long userId, BookingState state);

    List<BookingDto> getAllBookingByOwner(Long userId, BookingState state);
}
