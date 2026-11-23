package org.example.spring_presentashion_5.event;

import lombok.Getter;

@Getter
public class UserEmailChangedEvent {
    private final String oldEmail;
    private final String newEmail;

    public UserEmailChangedEvent(String oldEmail, String newEmail) {
        this.oldEmail = oldEmail;
        this.newEmail = newEmail;
    }
}