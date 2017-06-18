package com.github.alien11689.taskmanager.domain.task.event;

import com.github.alien11689.taskmanager.domain.task.TaskId;
import com.github.alien11689.taskmanager.domain.task.UserId;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class TaskAssigned implements TaskEvent {
    TaskId taskId;
    UserId assignee;
    LocalDateTime assignedAt;
}
