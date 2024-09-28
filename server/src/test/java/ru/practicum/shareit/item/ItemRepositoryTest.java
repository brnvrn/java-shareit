package ru.practicum.shareit.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;


    private final User user = User.builder()
            .id(1L)
            .name("Alex")
            .email("alex@mail.com")
            .build();

    private final Item item = Item.builder()
            .id(1L)
            .name("Предмет")
            .description("Описание предмета")
            .available(true)
            .owner(user)
            .build();

    @BeforeEach
    void setUp() {
        userRepository.save(user);
        itemRepository.save(item);
    }

    @AfterEach
    void tearDown() {
        itemRepository.delete(item);
        userRepository.delete(user);
    }

    @Test
    void testFindAllByOwnerId() {
        List<Item> allByOwnerId = itemRepository.findByOwnerId(user.getId());
        assertEquals(allByOwnerId.size(), 1);
        assertEquals(allByOwnerId.getFirst(), item);
    }

    @Test
    void testSearch() {
        List<Item> items = itemRepository.search("Описание предмета");
        assertEquals(items.size(), 1);
        assertEquals(items.getFirst(), item);
    }
}
