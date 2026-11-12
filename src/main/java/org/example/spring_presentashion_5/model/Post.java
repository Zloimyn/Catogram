package org.example.spring_presentashion_5.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class Post {
    private String author;
    private String content;
}