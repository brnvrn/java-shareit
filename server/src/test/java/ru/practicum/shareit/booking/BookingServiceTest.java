package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingMapper;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.exception.NotFoundException;
import ru.practicum.shareit.user.exception.UnavailableItemException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class BookingServiceTest {

    @Autowired
    private BookingService bookingService;

    @MockBean
    private BookingRepository bookingRepository;

    @MockBean
    private BookingMapper bookingMapper;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ItemRepository itemRepository;

    @Test
    void testAddNewBooking_Success() {
        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setItemId(1L);
        bookingRequestDto.setStart(LocalDateTime.now().plusDays(1));
        bookingRequestDto.setEnd(LocalDateTime.now().plusDays(2));

        User booker = new User();
        booker.setId(2L);

        User owner = new User();
        owner.setId(1L);

        Item item = new Item();
        item.setId(1L);
        item.setOwner(owner);
        item.setAvailable(true);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(bookingMapper.toBooking(bookingRequestDto)).thenReturn(booking);
        when(bookingRepository.save(booking)).thenReturn(booking);
        when(bookingMapper.toBookingDto(booking)).thenReturn(new BookingDto());

        BookingDto result = bookingService.addNewBooking(2L, bookingRequestDto);

        assertNotNull(result);
        verify(bookingRepository, times(1)).save(booking);
    }

    @Test
    void testApprovedOrRejectBooking_Success() {
        User owner = new User();
        owner.setId(1L);

        User booker = new User();
        booker.setId(2L);

        Item item = new Item();
        item.setId(1L);
        item.setOwner(owner);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(bookingMapper.toBookingDto(booking)).thenReturn(new BookingDto());

        BookingDto result = bookingService.approvedOrRejectBooking(1L, 1L, true);

        assertNotNull(result);
        assertEquals(BookingStatus.APPROVED, booking.getStatus());
        verify(bookingRepository, times(1)).save(booking);
    }

    @Test
    void testGetBookingById_Success() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setBooker(new User());
        booking.getBooker().setId(1L);
        booking.setItem(new Item());
        booking.getItem().setOwner(new User());
        booking.getItem().getOwner().setId(2L);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingMapper.toBookingDto(booking)).thenReturn(new BookingDto());

        BookingDto result = bookingService.getBookingById(1L, 1L);

        assertNotNull(result);
        verify(bookingRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllBookingByUser_Success() {
        List<Booking> bookings = new ArrayList<>();
        Booking booking1 = new Booking();
        booking1.setId(1L);
        booking1.setBooker(new User());
        booking1.getBooker().setId(1L);
        booking1.setStatus(BookingStatus.APPROVED);
        Booking booking2 = new Booking();
        booking2.setId(2L);
        booking2.setBooker(new User());
        booking2.getBooker().setId(1L);
        booking2.setStatus(BookingStatus.REJECTED);
        bookings.add(booking1);
        bookings.add(booking2);

        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(bookingRepository.findByBooker_IdOrderByStartDesc(1L)).thenReturn(bookings);
        when(bookingMapper.toBookingDto(any())).thenReturn(new BookingDto());

        List<BookingDto> result = bookingService.getAllBookingByUser(1L, BookingState.ALL);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testGetAllBookingByOwner_Success() {
        List<Booking> bookings = new ArrayList<>();
        Booking booking1 = new Booking();
        booking1.setId(1L);
        booking1.setItem(new Item());
        booking1.getItem().setOwner(new User());
        booking1.getItem().getOwner().setId(1L);
        booking1.setStatus(BookingStatus.APPROVED);
        Booking booking2 = new Booking();
        booking2.setId(2L);
        booking2.setItem(new Item());
        booking2.getItem().setOwner(new User());
        booking2.getItem().getOwner().setId(1L);
        booking2.setStatus(BookingStatus.REJECTED);
        bookings.add(booking1);
        bookings.add(booking2);

        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(bookingRepository.findAllByBookerIdOrderByStartDesc(1L)).thenReturn(bookings);
        when(bookingMapper.toBookingDto(any())).thenReturn(new BookingDto());

        List<BookingDto> result = bookingService.getAllBookingByOwner(1L, BookingState.ALL);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testAddNewBooking_NullStartAndEndTime() {
        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setItemId(1L);

        assertThrows(IllegalArgumentException.class, () -> bookingService.addNewBooking(2L, bookingRequestDto));
    }

    @Test
    void testAddNewBooking_EndTimeBeforeStartTime() {
        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setItemId(1L);
        bookingRequestDto.setStart(LocalDateTime.now().plusDays(2));
        bookingRequestDto.setEnd(LocalDateTime.now().plusDays(1));

        assertThrows(UnavailableItemException.class, () -> bookingService.addNewBooking(2L, bookingRequestDto));
    }

    @Test
    void testAddNewBooking_StartTimeInPast() {
        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setItemId(1L);
        bookingRequestDto.setStart(LocalDateTime.now().minusDays(1));
        bookingRequestDto.setEnd(LocalDateTime.now().plusDays(1));

        assertThrows(UnavailableItemException.class, () -> bookingService.addNewBooking(2L, bookingRequestDto));
    }

    @Test
    void testAddNewBooking_OwnerCannotBookOwnItem() {
        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setItemId(1L);
        bookingRequestDto.setStart(LocalDateTime.now().plusDays(1));
        bookingRequestDto.setEnd(LocalDateTime.now().plusDays(2));

        User owner = new User();
        owner.setId(2L);

        Item item = new Item();
        item.setId(1L);
        item.setOwner(owner);
        item.setAvailable(true);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(userRepository.findById(2L)).thenReturn(Optional.of(owner));

        assertThrows(UnavailableItemException.class, () -> bookingService.addNewBooking(2L, bookingRequestDto));
    }

    @Test
    void testApprovedOrRejectBooking_BookingNotFound() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingService.approvedOrRejectBooking(1L, 1L, true));
    }

    @Test
    void testApprovedOrRejectBooking_ItemNotFound() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setItem(new Item());

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingService.approvedOrRejectBooking(1L, 1L, true));
    }

    @Test
    void testGetBookingById_BookingNotFound() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingService.getBookingById(1L, 1L));
    }

    @Test
    void testGetBookingById_UserNotAllowedToViewBooking() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setBooker(new User());
        booking.getBooker().setId(1L);
        booking.setItem(new Item());
        booking.getItem().setOwner(new User());
        booking.getItem().getOwner().setId(2L);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        assertThrows(NotFoundException.class, () -> bookingService.getBookingById(3L, 1L));
    }
}
