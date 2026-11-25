package org.example.spring_presentashion_5;

import org.example.spring_presentashion_5.exception.UserAlreadyExistException;
import org.example.spring_presentashion_5.exception.UserNotFoundException;
import org.example.spring_presentashion_5.model.Post;
import org.example.spring_presentashion_5.model.User;
import org.example.spring_presentashion_5.service.PostService;
import org.example.spring_presentashion_5.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class SpringPresentashion5ApplicationTests {

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Test
    void createUserWithValidEmail() {
        User user = new User("ivan@example.com", "Иван");
        User created = userService.addUser(user);

        assertNotNull(created);
        assertEquals("ivan@example.com", created.getEmail());
        assertEquals("Иван", created.getName());
        assertNotSame(user, created);
    }

    @Test
    void createDuplicateUserThrowsException() {
        User user = new User("ivan@example.com", "Иван");
        userService.addUser(user);

        User duplicate = new User("ivan@example.com", "Петр");
        assertThrows(UserAlreadyExistException.class, () -> userService.addUser(duplicate));
    }

    @Test
    void updateNonExistentUserThrowsException() {
        User nonExistent = new User("missing@example.com", "Неизвестный");
        assertThrows(UserNotFoundException.class, () -> userService.updateUser(nonExistent));
    }

    @Test
    void findExistingAndNonExistingUser() {
        userService.addUser(new User("ivan@example.com", "Иван"));

        User found = userService.findUserByEmail("ivan@example.com");
        assertNotNull(found);
        assertEquals("Иван", found.getName());

        User notFound = userService.findUserByEmail("unknown@example.com");
        assertNull(notFound);
    }

    @Test
    void createPostWithExistingAuthor() {
        userService.addUser(new User("author@example.com", "Автор"));
        Post post = new Post("author@example.com", "Тестовый пост");

        Post created = postService.create(post);

        assertNotNull(created);
        assertEquals("author@example.com", created.getAuthor());
        assertEquals("Тестовый пост", created.getContent());
        assertNotSame(post, created);
    }

    @Test
    void createPostWithNonExistentAuthorThrowsException() {
        Post post = new Post("ghost@example.com", "Пост без автора");
        assertThrows(UserNotFoundException.class, () -> postService.create(post));
    }

    @Test
    void updateUserEmailUpdatesAuthorsInOldPosts() {
        userService.addUser(new User("old@example.com", "Автор"));
        postService.create(new Post("old@example.com", "Первый пост"));
        postService.create(new Post("old@example.com", "Второй пост"));

        User updated = userService.updateUserEmail("old@example.com", "new@example.com");

        assertEquals("new@example.com", updated.getEmail());
        assertNull(userService.findUserByEmail("old@example.com"));
        assertNotNull(userService.findUserByEmail("new@example.com"));

        List<Post> allPosts = postService.findAll();
        assertEquals(2, allPosts.size());
        assertTrue(allPosts.stream().allMatch(p -> "new@example.com".equals(p.getAuthor())));
    }
}