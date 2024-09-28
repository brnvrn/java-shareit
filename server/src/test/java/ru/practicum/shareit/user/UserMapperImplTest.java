package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserMapper;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserMapperImplTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void testToUserDto_Success() {
        User user = new User();
        user.setId(1L);
        user.setName("Alex");
        user.setEmail("alex@mail.com");

        UserDto userDto = userMapper.toUserDto(user);

        assertNotNull(userDto);
        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getName(), userDto.getName());
        assertEquals(user.getEmail(), userDto.getEmail());
    }

    @Test
    void testToUserDto_NullInput() {
        UserDto userDto = userMapper.toUserDto(null);

        assertNull(userDto);
    }

    @Test
    void testToUser_Success() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("Alex");
        userDto.setEmail("alex@mail.com");

        User user = userMapper.toUser(userDto);

        assertNotNull(user);
        assertEquals(userDto.getId(), user.getId());
        assertEquals(userDto.getName(), user.getName());
        assertEquals(userDto.getEmail(), user.getEmail());
    }

    @Test
    void testToUser_NullInput() {
        User user = userMapper.toUser(null);

        assertNull(user);
    }
}
