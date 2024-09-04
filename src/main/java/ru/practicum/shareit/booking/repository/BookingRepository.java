package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBooker_IdAndStatusOrderByStartDesc(Long userId, BookingStatus bookingStatus);

    List<Booking> findByBooker_IdAndStartIsAfterOrderByStartDesc(Long userId, LocalDateTime now);

    List<Booking> findByBooker_IdAndEndIsBeforeOrderByStartDesc(Long userId, LocalDateTime now);

    List<Booking> findByBooker_IdOrderByStartDesc(Long userId);

    List<Booking> findAllByBookerIdAndItemIdAndStatusAndEndBefore(Long userId, Long itemId, BookingStatus status, LocalDateTime localDateTime);

    List<Booking> findAllByBookerIdOrderByStartDesc(Long userId);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long userId, LocalDateTime startTime,
                                                                             LocalDateTime endTime);

    List<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(Long userId, LocalDateTime time);

    List<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(Long userId, LocalDateTime time);

    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(Long userId, BookingStatus status);
}
