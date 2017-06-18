package com.github.alien11689.taskmanger.domain.ports.dto.user;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class UserId {
    private final String raw;

    public static UserId existingUser(String id){
        if (id == null || id.trim().isEmpty()) {
            throw new InvalidUserId();
        }
        return new UserId(id);
    }

    String raw(){
        return raw;
    }
}
