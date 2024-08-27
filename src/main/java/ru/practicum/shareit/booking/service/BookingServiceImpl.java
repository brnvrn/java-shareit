package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.exception.NotFoundException;
import ru.practicum.shareit.user.exception.UnavailableItemException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingDto addNewBooking(Long userId, BookingRequestDto bookingRequestDto) {
        LocalDateTime startBooking = bookingRequestDto.getStart();
        LocalDateTime endBooking = bookingRequestDto.getEnd();

        if (endBooking.isBefore(startBooking) || endBooking.equals(startBooking)
                || startBooking.isBefore(LocalDateTime.now())) {
            throw new UnavailableItemException("Время бронирования некорректно");
        }

        Item item = itemRepository.findById(bookingRequestDto.getItemId()).orElseThrow(() ->
                new NotFoundException("Вещь не найдена"));

        if (!item.getAvailable()) {
            throw new IllegalArgumentException("Вещь недоступена для бронирования");
        }

        if (item.getOwner().getId().equals(userId)) {
            throw new UnavailableItemException("Владелец предмета не может забронировать его");
        }

        User booker = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь не найден"));

        Booking booking = bookingMapper.toBooking(bookingRequestDto);
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);
        bookingRepository.save(booking);
        log.info("Создано новое бронирование с id = {}", booking.getId());
        return bookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto approvedOrRejectBooking(Long userId, Long bookingId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new NotFoundException("Бронирование не найдено"));
        itemRepository.findById(booking.getItem().getId())
                .orElseThrow(() -> new NotFoundException("Такой вещи не существует"));
        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new UnavailableItemException("Вы не являетесь собственником вещи");
        }
        if (approved && booking.getStatus().equals(BookingStatus.APPROVED)) {
            throw new UnavailableItemException("Вещь уже забронирована");
        }
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
            log.info("Бронирование с id = {} подтверждено", booking.getId());
        } else {
            booking.setStatus(BookingStatus.REJECTED);
            log.info("Бронирование с id = {} отклонено", booking.getId());
        }
        bookingRepository.save(booking);
        return bookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto getBookingById(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new NotFoundException("Бронирование не найдено"));
        if (!userId.equals(booking.getBooker().getId()) && !userId.equals(booking.getItem().getOwner().getId())) {
            throw new NotFoundException("Невозможно просмотреть бронирование");
        }
        log.info("Бронирование с id = {} получено", booking.getId());
        return bookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getAllBookingByUser(Long userId, BookingState state) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        return switch (state) {
            case ALL -> bookingRepository.findByBooker_IdOrderByStartDesc(userId).stream()
                    .map(bookingMapper::toBookingDto)
                    .toList();
            case CURRENT ->
                    bookingRepository.findByBooker_IdAndStatusOrderByStartDesc(userId, BookingStatus.APPROVED).stream()
                            .map(bookingMapper::toBookingDto)
                            .toList();
            case PAST -> bookingRepository.findByBooker_IdAndEndIsBeforeOrderByStartDesc(userId,
                            LocalDateTime.now()).stream()
                    .map(bookingMapper::toBookingDto)
                    .toList();
            case FUTURE -> bookingRepository.findByBooker_IdAndStartIsAfterOrderByStartDesc(userId,
                            LocalDateTime.now()).stream()
                    .map(bookingMapper::toBookingDto)
                    .toList();
            case WAITING -> bookingRepository.findByBooker_IdAndStatusOrderByStartDesc(userId,
                            BookingStatus.valueOf(String.valueOf(state))).stream()
                    .map(bookingMapper::toBookingDto)
                    .toList();
            case REJECTED ->
                    bookingRepository.findByBooker_IdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED).stream()
                            .map(bookingMapper::toBookingDto)
                            .toList();
            case null -> throw new NotFoundException("Бронирование не найдено");
        };
    }

    @Override
    public List<BookingDto> getAllBookingByOwner(Long userId, BookingState state) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        return switch (state) {
            case ALL -> bookingRepository.findAllByBookerIdOrderByStartDesc(userId).stream()
                    .map(bookingMapper::toBookingDto)
                    .toList();
            case CURRENT -> bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId,
                            LocalDateTime.now(), LocalDateTime.now()).stream()
                    .map(bookingMapper::toBookingDto)
                    .toList();
            case PAST -> bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(userId,
                            LocalDateTime.now()).stream()
                    .map(bookingMapper::toBookingDto)
                    .toList();
            case FUTURE -> bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(userId,
                            LocalDateTime.now()).stream()
                    .map(bookingMapper::toBookingDto)
                    .toList();
            case WAITING, REJECTED -> bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId,
                            BookingStatus.valueOf(String.valueOf(state))).stream()
                    .map(bookingMapper::toBookingDto)
                    .toList();
            case null -> throw new NotFoundException("Бронирование не найдено");
        };
    }
}
