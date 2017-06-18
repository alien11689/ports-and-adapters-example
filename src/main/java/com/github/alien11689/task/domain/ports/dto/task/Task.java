package com.github.alien11689.task.domain.ports.dto.task;

import com.github.alien11689.task.domain.ports.dto.user.UserId;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class Task {
    private final TaskId taskId;
    private final LocalDateTime createdAt;
    private final UserId assignee;
    private final String title;
    private final String description;
    private final TaskStatus status;
    private final LocalDateTime completedAt;
}
