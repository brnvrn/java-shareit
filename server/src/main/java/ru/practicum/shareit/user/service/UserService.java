package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.NotFoundException;
import ru.practicum.shareit.user.exception.NotUniqueEmailException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final Set<String> uniqueEmails = new HashSet<>();

    @Transactional
    public UserDto addNewUser(UserDto userDto) {
        if (uniqueEmails.contains(userDto.getEmail())) {
            throw new NotUniqueEmailException("Пользователь с таким эмейлом уже существует");
        }
        User user = userRepository.save(userMapper.toUser(userDto));
        uniqueEmails.add(user.getEmail());
        log.info("Добавление пользователя с id = {}", userDto.getId());
        return userMapper.toUserDto(user);
    }

    @Transactional
    public UserDto updateUser(Long userId, UserDto userDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));

        User toUser = userMapper.toUser(userDto);

        if (toUser.getName() != null) {
            user.setName(toUser.getName());
        }
        if (toUser.getEmail() != null && !toUser.getEmail().equals(user.getEmail())) {
            checkEmail(toUser.getEmail());
            uniqueEmails.remove(user.getEmail());
            uniqueEmails.add(toUser.getEmail());
            user.setEmail(toUser.getEmail());
        }

        log.info("Пользователь с id = {} успешно обновлен", userId);
        return userMapper.toUserDto(userRepository.save(user));
    }

    public List<UserDto> getAllUsers() {
        log.info("Получение всех пользователей");
        return userRepository.findAll().stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден"));
        log.info("Получение пользователя с id = {}", id);
        return userMapper.toUserDto(user);
    }

    @Transactional
    public void deleteUserById(Long id) {
        log.info("Удаление пользователя с id = {}", id);
        userRepository.deleteById(id);
    }

    private void checkEmail(String email) {
        if (uniqueEmails.contains(email)) {
            throw new NotUniqueEmailException("Пользователь с таким эмейлом уже существует");
        }
    }
}
