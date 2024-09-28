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
                                              @RequestParam(value = "approved", required = true, defaultValue = "true") Boolean approved) {
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
    public List<BookingDto> getAllBookingByUser(@RequestHeader("X-Sharer-User-id") Long userId,
                                                @RequestParam(required = false, defaultValue = "ALL") BookingState state,
                                                @RequestParam(required = false) Integer from,
                                                @RequestParam(required = false) Integer size) {
        log.info("Поступил GET-запрос на получение списка всех бронирований текущего пользователя");
        return bookingService.getAllBookingByUser(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getUserItemsBookings(@RequestHeader("X-Sharer-User-id") Long userId,
                                                 @RequestParam(required = false, defaultValue = "ALL") BookingState state,
                                                 @RequestParam(required = false) Integer from,
                                                 @RequestParam(required = false) Integer size) {
        log.info("Поступил GET-запрос на получение списка всех бронирований владельца)");
        return bookingService.getAllBookingByOwner(userId, state);
    }
}