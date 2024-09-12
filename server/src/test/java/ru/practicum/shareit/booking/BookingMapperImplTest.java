package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookingMapperImplTest {

    @Autowired
    private BookingMapper bookingMapper;

    @Test
    void testToBooking_Success() {
        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setStart(LocalDateTime.now().plusDays(1));
        bookingRequestDto.setEnd(LocalDateTime.now().plusDays(2));

        Booking booking = bookingMapper.toBooking(bookingRequestDto);

        assertNotNull(booking);
        assertEquals(bookingRequestDto.getStart(), booking.getStart());
        assertEquals(bookingRequestDto.getEnd(), booking.getEnd());
    }

    @Test
    void testToBooking_NullInput() {
        Booking booking = bookingMapper.toBooking(null);

        assertNull(booking);
    }

    @Test
    void testToBookingDto_Success() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Предмет");
        item.setDescription("Описание");
        item.setAvailable(true);
        item.setOwner(new User());

        User booker = new User();
        booker.setId(1L);
        booker.setName("Alex");
        booker.setEmail("alex@mail.com");

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.APPROVED);

        BookingDto bookingDto = bookingMapper.toBookingDto(booking);

        assertNotNull(bookingDto);
        assertEquals(booking.getId(), bookingDto.getId());
        assertEquals(booking.getStart(), bookingDto.getStart());
        assertEquals(booking.getEnd(), bookingDto.getEnd());
        assertEquals(booking.getItem().getId(), bookingDto.getItem().getId());
        assertEquals(booking.getItem().getName(), bookingDto.getItem().getName());
        assertEquals(booking.getItem().getDescription(), bookingDto.getItem().getDescription());
        assertEquals(booking.getItem().getAvailable(), bookingDto.getItem().getAvailable());
        assertEquals(booking.getBooker().getId(), bookingDto.getBooker().getId());
        assertEquals(booking.getBooker().getName(), bookingDto.getBooker().getName());
        assertEquals(booking.getBooker().getEmail(), bookingDto.getBooker().getEmail());
        assertEquals(booking.getStatus(), bookingDto.getStatus());
    }

    @Test
    void testToBookingDto_NullInput() {
        BookingDto bookingDto = bookingMapper.toBookingDto(null);

        assertNull(bookingDto);
    }
}
