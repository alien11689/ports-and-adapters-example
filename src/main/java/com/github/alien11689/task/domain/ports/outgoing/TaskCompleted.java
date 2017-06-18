package com.github.alien11689.task.domain.ports.outgoing;

import com.github.alien11689.task.domain.ports.dto.task.TaskId;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class TaskCompleted {
    TaskId taskId;
    LocalDateTime completedAt;
}
