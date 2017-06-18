package com.github.alien11689.task.domain.process;

import com.github.alien11689.task.domain.ports.dto.task.TaskId;

public class CannotCompleteNotAssignedTask extends RuntimeException {
    private final TaskId taskId;

    public CannotCompleteNotAssignedTask(TaskId taskId) {
        this.taskId = taskId;
    }
}
