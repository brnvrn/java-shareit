package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.exception.NotUniqueEmailException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Component
public class UserDbStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> uniqueEmails = new HashSet<>();
    private long generatorId = 0;

    @Override
    public User addNewUser(User user) {
        if (uniqueEmails.contains(user.getEmail())) {
            throw new NotUniqueEmailException("Пользователь с таким эмейлом уже существует");
        }
        long id = ++generatorId;
        user.setId(id);
        users.put(id, user);
        uniqueEmails.add(user.getEmail());
        return user;
    }

    @Override
    public User updateUser(User user) {
        long id = user.getId();
        User userFromDb = getUserById(id);

        if (user.getEmail() == null) {
            user.setEmail(userFromDb.getEmail());
        }
        if (user.getName() == null) {
            user.setName(userFromDb.getName());
        }
        users.remove(id);
        if (isDuplicateEmail(user.getEmail())) {
            users.put(userFromDb.getId(), userFromDb);
            throw new NotUniqueEmailException("Такой эмайл уже существует");
        }

        users.put(id, user);
        return users.get(id);
    }


    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(long id) {
        if (!users.containsKey(id)) {
            throw new IllegalArgumentException("Пользователь не найден");
        }
        return users.get(id);
    }

    @Override
    public void deleteUserById(Long id) {
        if (!users.containsKey(id)) {
            throw new IllegalArgumentException("Пользователь не найден");
        }
        User user = users.remove(id);
        uniqueEmails.remove(user.getEmail());
    }

    private boolean isDuplicateEmail(String email) {
        List<String> emailToDb = users.values().stream()
                .map(User::getEmail)
                .toList();
        return emailToDb.contains(email);
    }
}