package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    private final BookingService bookingService;
    public static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public BookingDto addNewBooking(@RequestHeader(USER_ID_HEADER) Long userId,
                                    @RequestBody BookingRequestDto bookingRequestDto) {
        log.info("Поступил POST-запрос добавление нового бронирования");
        return bookingService.addNewBooking(userId, bookingRequestDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approvedOrRejectBooking(@RequestHeader(USER_ID_HEADER) Long userId,
                                              @PathVariable Long bookingId,
                                              @RequestParam Boolean approved) {
        log.info("Поступил PATCH-запрос на подтверждение или отклонение запроса на бронирование");
        return bookingService.approvedOrRejectBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    BookingDto getBookingById(@RequestHeader(USER_ID_HEADER) Long userId,
                              @PathVariable Long bookingId) {
        log.info("Поступил GET-запрос на получение бронирования пользовате6ля с id = {}", userId);
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping
    List<BookingDto> getAllBookingByUser(@RequestHeader(USER_ID_HEADER) Long userId,
                                         @RequestParam(defaultValue = "ALL") BookingState state) {
        log.info("Поступил GET-запрос на получение списка всех бронирований текущего пользователя");
        return bookingService.getAllBookingByUser(userId, state);
    }

    @GetMapping("/owner")
    List<BookingDto> getAllBookingByOwner(@RequestHeader(USER_ID_HEADER) Long userId,
                                          @RequestParam(defaultValue = "ALL", required = false) BookingState state) {
        log.info("Поступил GET-запрос на получение списка бронирований для всех вещей владельца");
        return bookingService.getAllBookingByOwner(userId, state);
    }
}