package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto addNewUser(UserDto userDto);

    UserDto updateUser(long id, UserDto UserDto);

    List<UserDto> getAllUsers();

    UserDto getUserById(long id);

    void deleteUserById(Long id);
}
