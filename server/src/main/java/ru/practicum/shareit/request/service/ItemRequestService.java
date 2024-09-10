package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestNewDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRequestMapper itemRequestMapper;

    @Transactional
    public ItemRequestDto addNewItemRequest(Long userId, ItemRequestNewDto itemRequestNewDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id не найден"));
        ItemRequest request = itemRequestMapper.toItemRequest(itemRequestNewDto);
        request.setRequestor(user);
        request.setDescription(itemRequestNewDto.getDescription());
        request.setCreated(LocalDateTime.now());
        log.info("Добавление нового запроса на вещь");
        return itemRequestMapper.toItemRequestDto(itemRequestRepository.save(request));
    }

    public List<ItemRequestResponseDto> getItemRequestByUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        List<ItemRequest> itemRequests = itemRequestRepository.findByRequestorIdOrderByCreatedDesc(userId);
        log.info("Получение списка своих запросов");
        return itemRequestMapper.toListItemRequestResponseDto(itemRequests);
    }

    public ItemRequestResponseDto getItemRequest(Long userId, Long requestId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        ItemRequest request = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос не найден"));
        log.info("Получение данных об одном конкретном запросе");
        return itemRequestMapper.toItemRequestResponseDto(request);
    }

    public List<ItemRequestDto> getAllItemRequests(Long userId, int from, int size) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id не найден"));
        PageRequest pageRequest = PageRequest.of(from, size, Sort.by(Sort.Direction.DESC, "created"));
        Page<ItemRequest> itemRequestPage = itemRequestRepository.findAll(pageRequest);

        log.info("Получение списка своих запросов id: {}", userId);

        return itemRequestPage.getContent().stream()
                .map(itemRequestMapper::toItemRequestDto)
                .toList();
    }
}
