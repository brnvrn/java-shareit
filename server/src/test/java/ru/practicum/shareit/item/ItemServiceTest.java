package ru.practicum.shareit.item;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
class ItemServiceTest {

    @Autowired
    private ItemService itemService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private BookingRepository bookingRepository;

    private User savedUser;
    private Item savedItem;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setName("alex");
        user.setEmail("alex@mail.com");
        savedUser = userRepository.save(user);

        Item item = new Item();
        item.setName("Предмет");
        item.setDescription("Описание предмета");
        item.setAvailable(true);
        item.setOwner(savedUser);
        savedItem = itemRepository.save(item);
    }

    @Test
    void testAddNewItem() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Вещь");
        itemDto.setDescription("Описание");
        itemDto.setAvailable(true);

        ItemDto createdItem = itemService.addNewItem(savedUser.getId(), itemDto);

        assertNotNull(createdItem);
        assertNotNull(createdItem.getId());
        assertEquals(itemDto.getName(), createdItem.getName());
        assertEquals(itemDto.getDescription(), createdItem.getDescription());
        assertEquals(itemDto.getAvailable(), createdItem.getAvailable());
        assertEquals(savedUser.getId(), createdItem.getOwner().getId());
    }

    @Test
    void testUpdate() {
        ItemDto updateDto = new ItemDto();
        updateDto.setName("Вещь");
        updateDto.setDescription("Описание");
        updateDto.setAvailable(true);

        ItemDto updatedItem = itemService.update(savedUser.getId(), savedItem.getId(), updateDto);

        assertNotNull(updatedItem);
        assertEquals(savedItem.getId(), updatedItem.getId());
        assertEquals(updateDto.getName(), updatedItem.getName());
        assertEquals(updateDto.getDescription(), updatedItem.getDescription());
        assertEquals(updateDto.getAvailable(), updatedItem.getAvailable());
        assertEquals(savedUser.getId(), updatedItem.getOwner().getId());
    }

    @Test
    void testGetItemById() {
        Comment comment1 = new Comment();
        comment1.setText("Отлично");
        comment1.setAuthor(savedUser);
        comment1.setItem(savedItem);
        comment1.setCreated(LocalDateTime.now());
        commentRepository.save(comment1);

        Comment comment2 = new Comment();
        comment2.setText("Ок");
        comment2.setAuthor(savedUser);
        comment2.setItem(savedItem);
        comment2.setCreated(LocalDateTime.now());
        commentRepository.save(comment2);

        ItemCommentDto itemCommentDto = itemService.getItemById(savedUser.getId(), savedItem.getId());

        assertNotNull(itemCommentDto);
        assertEquals(savedItem.getId(), itemCommentDto.getId());
        assertEquals(savedItem.getName(), itemCommentDto.getName());
        assertEquals(savedItem.getDescription(), itemCommentDto.getDescription());
        assertEquals(savedItem.getAvailable(), itemCommentDto.getAvailable());
        assertEquals(2, itemCommentDto.getComments().size());
    }

    @Test
    void testAddComment() {
        Booking booking = new Booking();
        booking.setItem(savedItem);
        booking.setBooker(savedUser);
        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().minusDays(1));
        booking.setStatus(BookingStatus.APPROVED);
        bookingRepository.save(booking);

        CommentDto commentDto = new CommentDto();
        commentDto.setText("Okey");

        CommentDto createdComment = itemService.addComment(savedUser.getId(), savedItem.getId(), commentDto);

        assertNotNull(createdComment);
        assertNotNull(createdComment.getId());
        assertEquals(commentDto.getText(), createdComment.getText());
        assertEquals(savedItem.getId(), createdComment.getItemId());
        assertEquals(savedUser.getName(), createdComment.getAuthorName());
    }
}
