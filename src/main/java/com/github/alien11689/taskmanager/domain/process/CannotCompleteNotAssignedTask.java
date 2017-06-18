package com.github.alien11689.taskmanager.domain.process;

import com.github.alien11689.taskmanager.domain.task.TaskId;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CannotCompleteNotAssignedTask extends RuntimeException {
    private final TaskId taskId;
}
