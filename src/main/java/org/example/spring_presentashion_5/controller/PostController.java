package org.example.spring_presentashion_5.controller;

import org.example.spring_presentashion_5.model.Post;
import org.example.spring_presentashion_5.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
    private static final Logger log = LoggerFactory.getLogger(PostController.class);
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public List<Post> findAll() {
        log.debug("Текущее количество постов: {}", postService.getPosts().size());
        return postService.getPosts();
    }

    @PostMapping
    public Post create(@RequestBody Post post) {
        return postService.create(post);
    }
}