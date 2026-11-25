package org.example.spring_presentashion_5.service;

import org.example.spring_presentashion_5.event.UserEmailChangedEvent;
import org.example.spring_presentashion_5.exception.InvalidEmailException;
import org.example.spring_presentashion_5.exception.UserAlreadyExistException;
import org.example.spring_presentashion_5.exception.UserNotFoundException;
import org.example.spring_presentashion_5.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private final Map<String, User> users = new ConcurrentHashMap<>();
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public UserService(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public Map<String, User> allUsers() {
        Map<String, User> copy = new HashMap<>();
        for (Map.Entry<String, User> entry : users.entrySet()) {
            copy.put(entry.getKey(), new User(entry.getValue()));
        }
        return copy;
    }

    public User addUser(User user) {
        if (user == null) {
            throw new InvalidEmailException("Пользователь не передан");
        }
        validateEmail(user.getEmail());

        User newUser = new User(user);
        if (users.putIfAbsent(newUser.getEmail(), newUser) != null) {
            throw new UserAlreadyExistException("Такой email уже занят");
        }

        log.info("Добавлен пользователь: {}", newUser.getEmail());
        return new User(newUser);
    }

    public User updateUser(User user) {
        if (user == null) {
            throw new InvalidEmailException("Пользователь не передан");
        }
        validateEmail(user.getEmail());

        User existingUser = users.get(user.getEmail());
        if (existingUser == null) {
            throw new UserNotFoundException("Пользователь не найден");
        }

        existingUser.setName(user.getName());
        log.info("Обновлен пользователь: {}", existingUser.getEmail());
        return new User(existingUser);
    }

    public User updateUserEmail(String oldEmail, String newEmail) {
        validateEmail(oldEmail);
        validateEmail(newEmail);

        User existingUser = users.get(oldEmail);
        if (existingUser == null) {
            throw new UserNotFoundException("Пользователь не найден");
        }

        if (oldEmail.equals(newEmail)) {
            return new User(existingUser);
        }

        User updatedUser = new User(newEmail, existingUser.getName());
        if (users.putIfAbsent(newEmail, updatedUser) != null) {
            throw new UserAlreadyExistException("Такой email уже занят");
        }

        users.remove(oldEmail);
        eventPublisher.publishEvent(new UserEmailChangedEvent(oldEmail, newEmail));
        log.info("Email пользователя изменен с {} на {}", oldEmail, newEmail);
        return new User(updatedUser);
    }

    public User findUserByEmail(String email) {
        if (email == null || email.isBlank()) {
            return null;
        }

        User user = users.get(email);
        if (user == null) {
            return null;
        }
        return new User(user);
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank() || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new InvalidEmailException("Неверный email");
        }
    }
}