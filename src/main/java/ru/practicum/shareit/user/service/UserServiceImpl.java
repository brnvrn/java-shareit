package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    @Override
    public UserDto addNewUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        log.info("Добавление пользователя с id = {}", userDto.getId());
        return UserMapper.toUserDto(userStorage.addNewUser(user));
    }

    @Override
    public UserDto updateUser(long userId, UserDto userDto) {
        User userToUpdate = UserMapper.toUser(userDto);
        userToUpdate.setId(userId);
        User updatedUser = userStorage.updateUser(userToUpdate);
        log.info("Пользователь  с id = {} успешно обновлен", userId);
        return UserMapper.toUserDto(updatedUser);
    }

    @Override
    public List<UserDto> getAllUsers() {
        log.info("Получение всех пользователей");
        return userStorage.getAllUsers().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(long id) {
        User user = userStorage.getUserById(id);
        log.info("Получение пользователя с id = {}", id);
        return UserMapper.toUserDto(user);
    }

    @Override
    public void deleteUserById(Long id) {
        log.info("Удаление пользователя с id = {}", id);
        userStorage.deleteUserById(id);
    }
}
