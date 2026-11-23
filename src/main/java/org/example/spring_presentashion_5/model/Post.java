package org.example.spring_presentashion_5.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class Post {
    private String author;
    private String content;

    @JsonCreator
    public Post(@JsonProperty("author") String author, @JsonProperty("content") String content) {
        this.author = author;
        this.content = content;
    }

    public Post(Post other) {
        this.author = other.author;
        this.content = other.content;
    }
}