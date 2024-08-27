package ru.practicum.shareit.item.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.CommentRepository;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.exception.NotFoundException;
import ru.practicum.shareit.user.exception.UnavailableItemException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final ItemMapper itemMapper;

    @Override
    public ItemDto addNewItem(long userId, ItemDto itemDto) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь не найден"));
        Item item = itemMapper.toItem(itemDto);
        item.setOwner(user);
        log.info("Добавление нового предмета пользователя с id = {}", userId);
        return itemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {
        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь не найден"));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Вещь не найдена"));
        if (!item.getOwner().getId().equals(userId)) {
            throw new IllegalArgumentException("Пользователь не является владельцем вещи");
        }
        if (itemDto.getName() != null && !itemDto.getName().isEmpty()) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null && !itemDto.getDescription().isEmpty()) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        log.info("Обновление предмета с id = {} пользователя с id = {}", itemId, userId);

        return itemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemCommentDto getItemById(Long userId, Long itemId) {
        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь не найден"));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Вещь не найдена"));
        ItemCommentDto itemInfoDto = itemMapper.toItemInfoDto(item);
        itemInfoDto.setComments(commentMapper.toListDto(commentRepository.findAllByItemId(itemId)));
        log.info("Получение предмета с id = {} пользователя с id = {}", itemId, userId);
        return itemInfoDto;
    }

    @Override
    public List<ItemDto> findAll(Long userId) {
        log.info("Получение всех предметов пользователя с id = {}", userId);
        return itemMapper.toLItemListDto(itemRepository.findByOwnerId(userId));
    }

    @Override
    public List<ItemDto> searchItems(Long userId, String text) {
        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь не найден"));

        if (text.isEmpty()) {
            return List.of();
        }

        log.info("Поиск предметов пользователя с id = {}", userId);
        return itemMapper.toLItemListDto(itemRepository.search(text));
    }

    @Override
    public CommentDto addComment(Long userId, Long itemId, CommentDto commentDto) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Вещь не найдена"));
        User author = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь не найден"));
        Comment comment = commentMapper.toComment(commentDto);
        if (comment == null || comment.getText() == null) {
            throw new UnavailableItemException("Комментарий не должен быть пустым");
        }
        if (bookingRepository.findAllByBookerIdAndItemIdAndStatusAndEndBefore(userId, itemId, BookingStatus.APPROVED,
                LocalDateTime.now()).isEmpty()) {
            throw new UnavailableItemException("Пользователь с не брал вещь в бронирование");
        }
        comment.setAuthor(author);
        comment.setItem(item);
        comment.setCreated(LocalDateTime.now());
        CommentDto savedComment = commentMapper.toCommentDto(commentRepository.save(comment));
        savedComment.setAuthorName(author.getName());
        savedComment.setItemId(item.getId());

        log.info("Комментарий от пользователя с id = {} оставлен для вещи с id = {}", userId, itemId);
        return savedComment;
    }
}
