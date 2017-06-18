package com.github.alien11689.taskmanager.domain.task;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class TaskId {
    private final UUID raw;

    public static TaskId generateId() {
        return new TaskId(UUID.randomUUID());
    }

    public static TaskId existing(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new InvalidTaskId();
        }
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new InvalidTaskId();
        }
        return new TaskId(uuid);
    }

    String raw(){
        return raw.toString();
    }
}
