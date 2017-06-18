package com.github.alien11689.taskmanger.domain.ports.outgoing;

import com.github.alien11689.taskmanger.domain.ports.dto.user.UserId;

public interface UserRepository {
    boolean exist(UserId userId);
}
