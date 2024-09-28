package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestNewDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemRequestMapperImplTest {

    @Autowired
    private ItemRequestMapper itemRequestMapper;

    @Test
    void testToItemRequestDto_Success() {
        User user = new User();
        user.setId(1L);
        user.setName("Alex");
        user.setEmail("alex@mail.com");

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("Описание предмета");
        itemRequest.setRequestor(user);
        itemRequest.setCreated(LocalDateTime.now());

        ItemRequestDto result = itemRequestMapper.toItemRequestDto(itemRequest);

        assertNotNull(result);
        assertEquals(itemRequest.getId(), result.getId());
        assertEquals(itemRequest.getDescription(), result.getDescription());
        assertEquals(itemRequest.getRequestor(), result.getRequestor());
        assertEquals(itemRequest.getCreated(), result.getCreated());
    }

    @Test
    void testToItemRequestDto_NullInput() {
        ItemRequestDto result = itemRequestMapper.toItemRequestDto(null);

        assertNull(result);
    }

    @Test
    void testToItemRequest_Success() {
        ItemRequestNewDto itemRequestNewDto = new ItemRequestNewDto();
        itemRequestNewDto.setDescription("Новое описание вещи");

        ItemRequest result = itemRequestMapper.toItemRequest(itemRequestNewDto);

        assertNotNull(result);
        assertEquals(itemRequestNewDto.getDescription(), result.getDescription());
    }

    @Test
    void testToItemRequest_NullInput() {
        ItemRequest result = itemRequestMapper.toItemRequest(null);

        assertNull(result);
    }

    @Test
    void testToItemRequestResponseDto_Success() {
        User user = new User();
        user.setId(1L);
        user.setName("Alex");
        user.setEmail("alex@mail.com");

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("Описание предмета");
        itemRequest.setRequestor(user);
        itemRequest.setCreated(LocalDateTime.now());

        ItemRequestResponseDto result = itemRequestMapper.toItemRequestResponseDto(itemRequest);

        assertNotNull(result);
        assertEquals(itemRequest.getId(), result.getId());
        assertEquals(itemRequest.getDescription(), result.getDescription());
        assertEquals(itemRequest.getRequestor().getId(), result.getRequestor().getId());
        assertEquals(itemRequest.getCreated(), result.getCreated());
    }

    @Test
    void testToItemRequestResponseDto_NullInput() {
        ItemRequestResponseDto result = itemRequestMapper.toItemRequestResponseDto(null);

        assertNull(result);
    }

    @Test
    void testToListItemRequestResponseDto_Success() {
        User user = new User();
        user.setId(1L);
        user.setName("Alex");
        user.setEmail("alex@mail.com");

        ItemRequest itemRequest1 = new ItemRequest();
        itemRequest1.setId(1L);
        itemRequest1.setDescription("Описание предмета");
        itemRequest1.setRequestor(user);
        itemRequest1.setCreated(LocalDateTime.now());

        ItemRequest itemRequest2 = new ItemRequest();
        itemRequest2.setId(2L);
        itemRequest2.setDescription("Описание вещи");
        itemRequest2.setRequestor(user);
        itemRequest2.setCreated(LocalDateTime.now());

        List<ItemRequest> itemRequests = Arrays.asList(itemRequest1, itemRequest2);

        List<ItemRequestResponseDto> result = itemRequestMapper.toListItemRequestResponseDto(itemRequests);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(itemRequest1.getId(), result.get(0).getId());
        assertEquals(itemRequest2.getId(), result.get(1).getId());
    }

    @Test
    void testToListItemRequestResponseDto_NullInput() {
        List<ItemRequestResponseDto> result = itemRequestMapper.toListItemRequestResponseDto(null);

        assertNull(result);
    }

    @Test
    void testToListItemRequestResponseDto_EmptyList() {
        List<ItemRequestResponseDto> result = itemRequestMapper.toListItemRequestResponseDto(Arrays.asList());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}