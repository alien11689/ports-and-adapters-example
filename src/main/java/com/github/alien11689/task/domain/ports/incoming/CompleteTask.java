package com.github.alien11689.task.domain.ports.incoming;

import com.github.alien11689.task.domain.ports.dto.task.TaskId;

public interface CompleteTask {
    void completeTask(TaskId taskId);
}
