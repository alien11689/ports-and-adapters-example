package com.github.alien11689.taskmanager.domain.task.event;

import com.github.alien11689.taskmanager.domain.task.TaskId;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class TaskCompleted implements TaskEvent {
    TaskId taskId;
    LocalDateTime completedAt;
}
