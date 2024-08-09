package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {
    User addNewUser(User user);

    User updateUser(User user);

    List<User> getAllUsers();

    User getUserById(long id);

    void deleteUserById(Long id);
}
