package org.example.spring_presentashion_5.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class User {
    private final String email;
    private String name;

    @JsonCreator
    public User(@JsonProperty("email") String email, @JsonProperty("name") String name) {
        this.email = email;
        this.name = name;
    }

    public User(User other) {
        this.email = other.email;
        this.name = other.name;
    }
}