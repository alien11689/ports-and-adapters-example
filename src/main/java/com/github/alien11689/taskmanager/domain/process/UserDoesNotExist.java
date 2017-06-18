package com.github.alien11689.taskmanager.domain.process;

import com.github.alien11689.taskmanager.domain.task.UserId;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserDoesNotExist extends RuntimeException {
    private final UserId userId;
}
