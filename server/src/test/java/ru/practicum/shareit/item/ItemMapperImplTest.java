package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.ItemCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemMapperImplTest {

    @Autowired
    private ItemMapper itemMapper;

    @Test
    void testToItemDto_Success() {
        User owner = new User();
        owner.setId(1L);
        owner.setName("Alex");

        Item item = new Item();
        item.setId(1L);
        item.setName("Предмет");
        item.setDescription("Описание");
        item.setAvailable(true);
        item.setOwner(owner);

        ItemDto itemDto = itemMapper.toItemDto(item);

        assertNotNull(itemDto);
        assertEquals(item.getId(), itemDto.getId());
        assertEquals(item.getName(), itemDto.getName());
        assertEquals(item.getDescription(), itemDto.getDescription());
        assertEquals(item.getAvailable(), itemDto.getAvailable());
        assertEquals(item.getOwner(), itemDto.getOwner());
    }

    @Test
    void testToItemDto_NullInput() {
        ItemDto itemDto = itemMapper.toItemDto(null);

        assertNull(itemDto);
    }

    @Test
    void testToItem_Success() {
        User owner = new User();
        owner.setId(1L);
        owner.setName("Alex");

        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Предмет");
        itemDto.setDescription("Описание");
        itemDto.setAvailable(true);
        itemDto.setOwner(owner);

        Item item = itemMapper.toItem(itemDto);

        assertNotNull(item);
        assertEquals(itemDto.getId(), item.getId());
        assertEquals(itemDto.getName(), item.getName());
        assertEquals(itemDto.getDescription(), item.getDescription());
        assertEquals(itemDto.getAvailable(), item.getAvailable());
        assertEquals(itemDto.getOwner(), item.getOwner());
    }

    @Test
    void testToItem_NullInput() {
        Item item = itemMapper.toItem(null);

        assertNull(item);
    }

    @Test
    void testToLItemListDto_Success() {
        User owner = new User();
        owner.setId(1L);
        owner.setName("Alex");

        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Предмет");
        item1.setDescription("Описание");
        item1.setAvailable(true);
        item1.setOwner(owner);

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("Вещь");
        item2.setDescription("Описание вещи");
        item2.setAvailable(false);
        item2.setOwner(owner);

        List<Item> items = Arrays.asList(item1, item2);

        List<ItemDto> itemDtoList = itemMapper.toLItemListDto(items);

        assertNotNull(itemDtoList);
        assertEquals(2, itemDtoList.size());
        assertEquals(item1.getId(), itemDtoList.get(0).getId());
        assertEquals(item2.getId(), itemDtoList.get(1).getId());
    }

    @Test
    void testToLItemListDto_NullInput() {
        List<ItemDto> itemDtoList = itemMapper.toLItemListDto(null);

        assertNull(itemDtoList);
    }

    @Test
    void testToLItemListDto_EmptyList() {
        List<ItemDto> itemDtoList = itemMapper.toLItemListDto(Arrays.asList());

        assertNotNull(itemDtoList);
        assertTrue(itemDtoList.isEmpty());
    }

    @Test
    void testToItemInfoDto_Success() {
        User owner = new User();
        owner.setId(1L);
        owner.setName("Alex");

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("Отлично");
        comment.setCreated(LocalDateTime.now());
        comment.setAuthor(owner);

        Item item = new Item();
        item.setId(1L);
        item.setName("Предмет");
        item.setDescription("Описание");
        item.setAvailable(true);
        item.setOwner(owner);
        item.setComments(Arrays.asList(comment));

        ItemCommentDto itemCommentDto = itemMapper.toItemInfoDto(item);

        assertNotNull(itemCommentDto);
        assertEquals(item.getId(), itemCommentDto.getId());
        assertEquals(item.getName(), itemCommentDto.getName());
        assertEquals(item.getDescription(), itemCommentDto.getDescription());
        assertEquals(item.getAvailable(), itemCommentDto.getAvailable());
        assertEquals(1, itemCommentDto.getComments().size());
        assertEquals(comment.getText(), itemCommentDto.getComments().get(0).getText());
    }

    @Test
    void testToItemInfoDto_NullInput() {
        ItemCommentDto itemCommentDto = itemMapper.toItemInfoDto(null);

        assertNull(itemCommentDto);
    }
}
