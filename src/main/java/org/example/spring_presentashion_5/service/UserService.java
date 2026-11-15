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
        Map<String, User> copy = new HashMap<>();
        for (Map.Entry<String, User> entry : userHashMap.entrySet()) {
            copy.put(entry.getKey(), new User(entry.getValue()));
        }
        return copy;
    }

    public User addUser(User user) {
        if (user == null || user.getEmail() == null || user.getEmail().isBlank()) {
            throw new InvalidEmailException("Неверный email");
        }

        if (userHashMap.containsKey(user.getEmail())) {
            throw new UserAlreadyExistException("Такой email уже занят");
        }

        User newUser = new User(user);
        userHashMap.put(newUser.getEmail(), newUser);
        return new User(newUser);
    }

    public User updateUser(User user) {
        if (user == null || user.getEmail() == null || user.getEmail().isBlank()) {
            throw new InvalidEmailException("Неверный email");
        }

        User existingUser = userHashMap.get(user.getEmail());
        if (existingUser == null) {
            throw new UserNotFoundException("Пользователь не найден");
        }

        existingUser.setName(user.getName());
        return new User(existingUser);
    }

    public User updateUserEmail(String oldEmail, String newEmail) {
        if (oldEmail == null || oldEmail.isBlank() || newEmail == null || newEmail.isBlank()) {
            throw new InvalidEmailException("Неверный email");
        }

        User existingUser = userHashMap.get(oldEmail);
        if (existingUser == null) {
            throw new UserNotFoundException("Пользователь не найден");
        }

        if (oldEmail.equals(newEmail)) {
            return new User(existingUser);
        }

        if (userHashMap.containsKey(newEmail)) {
            throw new UserAlreadyExistException("Такой email уже занят");
        }

        userHashMap.remove(oldEmail);
        User updatedUser = new User(newEmail, existingUser.getName());
        userHashMap.put(newEmail, updatedUser);
        return new User(updatedUser);
    }

    public User findUserByEmail(String email) {
        User user = userHashMap.get(email);
        if (user == null) {
            return null;
        }
        return new User(user);
    }
}