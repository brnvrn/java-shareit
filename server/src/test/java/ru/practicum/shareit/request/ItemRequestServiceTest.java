package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestNewDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestMapper;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ItemRequestServiceTest {

    @Autowired
    private ItemRequestService itemRequestService;

    @MockBean
    private ItemRequestRepository itemRequestRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ItemRequestMapper itemRequestMapper;

    @Test
    void testAddNewItemRequest_Success() {
        ItemRequestNewDto itemRequestNewDto = new ItemRequestNewDto();
        itemRequestNewDto.setDescription("Test request");

        User user = new User();
        user.setId(1L);

        ItemRequest request = new ItemRequest();
        request.setId(1L);
        request.setRequestor(user);
        request.setDescription(itemRequestNewDto.getDescription());
        request.setCreated(LocalDateTime.now());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRequestMapper.toItemRequest(itemRequestNewDto)).thenReturn(request);
        when(itemRequestRepository.save(request)).thenReturn(request);
        when(itemRequestMapper.toItemRequestDto(request)).thenReturn(new ItemRequestDto());

        ItemRequestDto result = itemRequestService.addNewItemRequest(1L, itemRequestNewDto);

        assertNotNull(result);
        verify(itemRequestRepository, times(1)).save(request);
    }

    @Test
    void testGetItemRequestByUser_Success() {
        List<ItemRequest> itemRequests = new ArrayList<>();
        ItemRequest request1 = new ItemRequest();
        request1.setId(1L);
        request1.setRequestor(new User());
        request1.getRequestor().setId(1L);
        ItemRequest request2 = new ItemRequest();
        request2.setId(2L);
        request2.setRequestor(new User());
        request2.getRequestor().setId(1L);
        itemRequests.add(request1);
        itemRequests.add(request2);

        ItemRequestResponseDto responseDto1 = new ItemRequestResponseDto();
        responseDto1.setId(1L);
        ItemRequestResponseDto responseDto2 = new ItemRequestResponseDto();
        responseDto2.setId(2L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(itemRequestRepository.findByRequestorIdOrderByCreatedDesc(1L)).thenReturn(itemRequests);
        when(itemRequestMapper.toListItemRequestResponseDto(itemRequests)).thenReturn(List.of(responseDto1, responseDto2));

        List<ItemRequestResponseDto> result = itemRequestService.getItemRequestByUser(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
    }

    @Test
    void testGetItemRequest_Success() {
        ItemRequest request = new ItemRequest();
        request.setId(1L);
        request.setRequestor(new User());
        request.getRequestor().setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(itemRequestRepository.findById(1L)).thenReturn(Optional.of(request));
        when(itemRequestMapper.toItemRequestResponseDto(request)).thenReturn(new ItemRequestResponseDto());

        ItemRequestResponseDto result = itemRequestService.getItemRequest(1L, 1L);

        assertNotNull(result);
        verify(itemRequestRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllItemRequests_Success() {
        List<ItemRequest> itemRequests = new ArrayList<>();
        ItemRequest request1 = new ItemRequest();
        request1.setId(1L);
        request1.setRequestor(new User());
        request1.getRequestor().setId(1L);
        ItemRequest request2 = new ItemRequest();
        request2.setId(2L);
        request2.setRequestor(new User());
        request2.getRequestor().setId(1L);
        itemRequests.add(request1);
        itemRequests.add(request2);

        Page<ItemRequest> itemRequestPage = new PageImpl<>(itemRequests);

        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(itemRequestRepository.findAll(any(PageRequest.class))).thenReturn(itemRequestPage);
        when(itemRequestMapper.toItemRequestDto(any())).thenReturn(new ItemRequestDto());

        List<ItemRequestDto> result = itemRequestService.getAllItemRequests(1L, 0, 10);

        assertNotNull(result);
        assertEquals(2, result.size());
    }
}