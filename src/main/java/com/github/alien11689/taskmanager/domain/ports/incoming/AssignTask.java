package com.github.alien11689.taskmanager.domain.ports.incoming;

import com.github.alien11689.taskmanager.domain.task.TaskId;
import com.github.alien11689.taskmanager.domain.task.UserId;

public interface AssignTask {
    void assign(TaskId taskId, UserId userId);
}
