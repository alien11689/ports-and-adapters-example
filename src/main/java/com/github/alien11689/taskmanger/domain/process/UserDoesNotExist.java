package com.github.alien11689.taskmanger.domain.process;

import com.github.alien11689.taskmanger.domain.ports.dto.user.UserId;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserDoesNotExist extends RuntimeException {
    private final UserId userId;
}
