package com.github.alien11689.task.domain.ports.incoming;

import com.github.alien11689.task.domain.ports.dto.task.TaskId;
import com.github.alien11689.task.domain.ports.dto.user.UserId;

public interface ChangeTaskAssignee {
    void assign(TaskId taskId, UserId userId);
}
