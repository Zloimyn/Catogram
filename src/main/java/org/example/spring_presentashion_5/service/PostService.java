package org.example.spring_presentashion_5.service;

import org.example.spring_presentashion_5.event.UserEmailChangedEvent;
import org.example.spring_presentashion_5.exception.InvalidAuthorException;
import org.example.spring_presentashion_5.exception.InvalidPostException;
import org.example.spring_presentashion_5.exception.UserNotFoundException;
import org.example.spring_presentashion_5.model.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class PostService {
    private static final Logger log = LoggerFactory.getLogger(PostService.class);

    private final List<Post> posts = new CopyOnWriteArrayList<>();
    private final UserService userService;

    @Autowired
    public PostService(UserService userService) {
        this.userService = userService;
    }

    public List<Post> findAll() {
        List<Post> copy = new ArrayList<>();
        for (Post post : posts) {
            copy.add(new Post(post));
        }
        return copy;
    }

    public Post create(Post post) {
        if (post == null) {
            throw new InvalidPostException("Пост не существует");
        }

        if (post.getAuthor() == null || post.getAuthor().isBlank()) {
            throw new InvalidAuthorException("Автор не указан");
        }

        if (post.getContent() == null || post.getContent().isBlank()) {
            throw new InvalidPostException("Текст поста не может быть пустым");
        }

        String emailAuthor = post.getAuthor();

        if (userService.findUserByEmail(emailAuthor) == null) {
            throw new UserNotFoundException("Пользователь " + emailAuthor + " не найден");
        }

        Post newPost = new Post(post);
        posts.add(newPost);
        log.info("Создан новый пост от автора: {}", emailAuthor);
        return new Post(newPost);
    }

    @EventListener
    public void onUserEmailChanged(UserEmailChangedEvent event) {
        for (Post post : posts) {
            if (event.getOldEmail().equals(post.getAuthor())) {
                post.setAuthor(event.getNewEmail());
            }
        }
        log.info("Обновлен автор постов с {} на {}", event.getOldEmail(), event.getNewEmail());
    }
}