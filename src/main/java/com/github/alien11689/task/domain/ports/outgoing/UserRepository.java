package com.github.alien11689.task.domain.ports.outgoing;

import com.github.alien11689.task.domain.ports.dto.user.UserId;

public interface UserRepository {
    boolean exist(UserId userId);
}
