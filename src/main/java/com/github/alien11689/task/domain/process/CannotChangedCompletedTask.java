package com.github.alien11689.task.domain.process;

import com.github.alien11689.task.domain.ports.dto.task.TaskId;

public class CannotChangedCompletedTask extends RuntimeException {
    private final TaskId taskId;

    public CannotChangedCompletedTask(TaskId taskId) {
        this.taskId = taskId;
    }
}
