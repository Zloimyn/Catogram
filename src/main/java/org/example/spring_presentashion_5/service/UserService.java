package org.example.spring_presentashion_5.service;

import org.example.spring_presentashion_5.model.User;
import org.example.spring_presentashion_5.exception.InvalidEmailException;
import org.example.spring_presentashion_5.exception.UserAlreadyExistException;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class UserService {
    private final HashMap<String, User> userHashMap = new HashMap<>();

    public HashMap<String, User> allUsers() {
        return userHashMap;
    }

    public User addUsers(User user) {
        if (user == null || user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new InvalidEmailException("Неверный email");
        }

        if (userHashMap.containsKey(user.getEmail())) {
            throw new UserAlreadyExistException("Такой email уже занят");
        }

        userHashMap.put(user.getEmail(), user);
        return user;
    }

    public User updateUser(User user) {
        if (user == null || user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new InvalidEmailException("Неверный email");
        }

        userHashMap.put(user.getEmail(), user);
        return user;
    }

    public User findUserByEmail(String email) {
        return userHashMap.get(email);
    }
}