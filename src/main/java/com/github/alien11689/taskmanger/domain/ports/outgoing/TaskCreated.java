package com.github.alien11689.taskmanger.domain.ports.outgoing;

import com.github.alien11689.taskmanger.domain.ports.dto.task.TaskId;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class TaskCreated {
    TaskId taskId;
    LocalDateTime createdAt;
    String title;
}
