package org.example.spring_presentashion_5.service;

import org.example.spring_presentashion_5.exception.UserNotFoundException;
import org.example.spring_presentashion_5.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {
    private final List<Post> posts = new ArrayList<>();
    private final UserService userService;

    @Autowired
    public PostService(UserService userService) {
        this.userService = userService;
    }

    public List<Post> findAll() {
        return new ArrayList<>(posts);
    }

    public Post create(Post post) {
        if (post == null || post.getAuthor() == null) {
            throw new UserNotFoundException("Автор не указан");
        }

        String emailAuthor = post.getAuthor();

        if (userService.findUserByEmail(emailAuthor) == null) {
            throw new UserNotFoundException("Пользователь " + emailAuthor + " не найден");
        }

        posts.add(post);
        return post;
    }
}