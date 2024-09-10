package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;


@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;
    public static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> bookItem(@RequestHeader(USER_ID_HEADER) @Positive long userId,
                                           @RequestBody @Valid BookItemRequestDto requestDto) {
        log.info("Поступил POST-запрос на добавление нового бронирования");
        return bookingClient.bookItem(userId, requestDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<?> approveOrRejectBooking(@PathVariable @Positive long bookingId,
                                                    @RequestHeader(USER_ID_HEADER) @Positive Long userId,
                                                    @RequestParam(value = "approved", required = true,
                                                            defaultValue = "true") Boolean approved) {
        log.info("Поступил PATCH-запрос на подтверждение или отклонение запроса на бронирование");
        return bookingClient.approveOrRejectBooking(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@RequestHeader(USER_ID_HEADER) @Positive long userId,
                                                 @PathVariable @Positive Long bookingId) {
        log.info("Поступил GET-запрос на получение бронирования пользователя с id = {}", userId);
        return bookingClient.getBookingById(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllBookingByUser(@RequestHeader(USER_ID_HEADER) @Positive long userId,
                                                      @RequestParam(required = false, defaultValue = "ALL")
                                                      String state) {
        log.info("Поступил GET-запрос на получение списка всех бронирований текущего пользователя");
        BookingState stateParam = BookingState.from(state)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + state));
        return bookingClient.getAllBookingByUser(userId, stateParam);
    }


    @GetMapping("/owner")
    public ResponseEntity<?> getUserItemsBookings(@RequestHeader(USER_ID_HEADER) @Positive Long userId,
                                                  @RequestParam(required = false, defaultValue = "ALL") BookingState state,
                                                  @RequestParam(required = false) Integer from,
                                                  @RequestParam(required = false) Integer size) {
        log.info("Поступил GET-запрос на получение списка бронирований для всех вещей владельца");
        return bookingClient.getBookings(userId, state, from, size);
    }
}
