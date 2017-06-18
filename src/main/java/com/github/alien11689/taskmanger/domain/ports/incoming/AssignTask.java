package com.github.alien11689.taskmanger.domain.ports.incoming;

import com.github.alien11689.taskmanger.domain.ports.dto.task.TaskId;
import com.github.alien11689.taskmanger.domain.ports.dto.user.UserId;

public interface AssignTask {
    void assign(TaskId taskId, UserId userId);
}
