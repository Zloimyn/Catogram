package org.example.spring_presentashion_5.service;

import org.example.spring_presentashion_5.exception.InvalidEmailException;
import org.example.spring_presentashion_5.exception.UserAlreadyExistException;
import org.example.spring_presentashion_5.exception.UserNotFoundException;
import org.example.spring_presentashion_5.model.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    private final Map<String, User> userHashMap = new HashMap<>();

    public Map<String, User> allUsers() {
        return new HashMap<>(userHashMap);
    }

    public User addUser(User user) {
        if (user == null || user.getEmail() == null || user.getEmail().isBlank()) {
            throw new InvalidEmailException("Неверный email");
        }

        if (userHashMap.containsKey(user.getEmail())) {
            throw new UserAlreadyExistException("Такой email уже занят");
        }

        userHashMap.put(user.getEmail(), user);
        return user;
    }

    public User updateUser(User user) {
        if (user == null || user.getEmail() == null || user.getEmail().isBlank()) {
            throw new InvalidEmailException("Неверный email");
        }
        if (!userHashMap.containsKey(user.getEmail())) {
            throw new UserNotFoundException("Пользователь не найден");
        }

        userHashMap.put(user.getEmail(), user);
        return user;
    }

    public User findUserByEmail(String email) {
        return userHashMap.get(email);
    }
}