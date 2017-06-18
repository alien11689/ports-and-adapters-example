package com.github.alien11689.taskmanager.domain.ports.outgoing;

import com.github.alien11689.taskmanager.domain.task.UserId;

public interface UserRepository {
    boolean exist(UserId userId);
}
