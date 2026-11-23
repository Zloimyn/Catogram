package org.example.spring_presentashion_5.controller;

import org.example.spring_presentashion_5.model.User;
import org.example.spring_presentashion_5.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Map<String, User> allUsers() {
        Map<String, User> users = userService.allUsers();
        log.debug("Текущее количество пользователей: {}", users.size());
        return users;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @PatchMapping("/{oldEmail}/email")
    public User updateUserEmail(@PathVariable String oldEmail, @RequestParam String newEmail) {
        return userService.updateUserEmail(oldEmail, newEmail);
    }

    @GetMapping("/{email}")
    public User findUserByEmail(@PathVariable String email) {
        return userService.findUserByEmail(email);
    }
}