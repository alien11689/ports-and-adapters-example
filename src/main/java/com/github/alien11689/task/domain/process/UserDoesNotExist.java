package com.github.alien11689.task.domain.process;

import com.github.alien11689.task.domain.ports.dto.user.UserId;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserDoesNotExist extends RuntimeException {
    private final UserId userId;
}
