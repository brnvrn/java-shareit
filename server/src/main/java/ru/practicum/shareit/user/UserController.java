package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto add(@RequestBody UserDto userDto) {
        log.info("Поступил POST-запрос на добавление нового пользователя");
        return userService.addNewUser(userDto);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable Long userId, @RequestBody UserDto userDto) {
        log.info("PATCH-запрос на обновление пользователя с id = {}", userId);
        return userService.updateUser(userId, userDto);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        log.info("Поступил GET-запрос на получение всех пользователей");
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable Long userId) {
        log.info("Поступил GET-запрос на получение пользователя c ID = {}", userId);
        return userService.getUserById(userId);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Long id) {
        log.info("Поступил DELETE-запрос на удаление пользователя с id = {}", id);
        userService.deleteUserById(id);
    }
}
