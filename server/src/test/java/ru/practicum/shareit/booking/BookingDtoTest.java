package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class BookingDtoTest {
    @Autowired
    private JacksonTester<BookingDto> json;

    @Test
    void testSerialize() throws Exception {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("Alex")
                .email("alex@mail.com")
                .build();
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .requestId(1L)
                .available(true)
                .description("Описание предмета")
                .name("Предмет")
                .build();
        BookingDto bookingDto = BookingDto.builder()
                .id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .booker(userDto)
                .item(itemDto)
                .status(BookingStatus.APPROVED)
                .build();

        JsonContent<BookingDto> result = json.write(bookingDto);

        assertThat(result).hasJsonPath("$.id")
                .hasJsonPath("$.start")
                .hasJsonPath("$.end")
                .hasJsonPath("$.status")
                .hasJsonPath("$.booker.id")
                .hasJsonPath("$.booker.name")
                .hasJsonPath("$.booker.email")
                .hasJsonPath("$.item.id")
                .hasJsonPath("$.item.available")
                .hasJsonPath("$.item.description")
                .hasJsonPath("$.item.name");

        assertThat(result).extractingJsonPathNumberValue("$.id")
                .satisfies(item_id -> assertThat(item_id.longValue()).isEqualTo(bookingDto.getId()));
        assertThat(result).extractingJsonPathStringValue("$.start")
                .satisfies(created -> assertThat(created).isNotNull());
        assertThat(result).extractingJsonPathStringValue("$.end")
                .satisfies(created -> assertThat(created).isNotNull());
        assertThat(result).extractingJsonPathValue("$.status")
                .satisfies(status -> assertThat(status).isEqualTo(bookingDto.getStatus().name()));

        assertThat(result).extractingJsonPathNumberValue("$.booker.id")
                .satisfies(item_id -> assertThat(item_id.longValue()).isEqualTo(bookingDto.getBooker().getId()));
        assertThat(result).extractingJsonPathStringValue("$.booker.name")
                .satisfies(item_description -> assertThat(item_description).isEqualTo(bookingDto.getBooker().getName()));
        assertThat(result).extractingJsonPathStringValue("$.booker.email")
                .satisfies(item_description -> assertThat(item_description).isEqualTo(bookingDto.getBooker().getEmail()));

        assertThat(result).extractingJsonPathNumberValue("$.item.id")
                .satisfies(id -> assertThat(id.longValue()).isEqualTo(bookingDto.getItem().getId()));

        assertThat(result).extractingJsonPathStringValue("$.item.name")
                .satisfies(item_name -> assertThat(item_name).isEqualTo(bookingDto.getItem().getName()));
        assertThat(result).extractingJsonPathStringValue("$.item.description")
                .satisfies(item_description -> assertThat(item_description).isEqualTo(bookingDto.getItem().getDescription()));
        assertThat(result).extractingJsonPathBooleanValue("$.item.available")
                .satisfies(item_available -> assertThat(item_available).isEqualTo(bookingDto.getItem().getAvailable()));
    }
}