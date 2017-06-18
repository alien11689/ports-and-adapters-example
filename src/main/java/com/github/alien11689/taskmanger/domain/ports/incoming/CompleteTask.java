package com.github.alien11689.taskmanger.domain.ports.incoming;

import com.github.alien11689.taskmanger.domain.ports.dto.task.TaskId;

public interface CompleteTask {
    void completeTask(TaskId taskId);
}
