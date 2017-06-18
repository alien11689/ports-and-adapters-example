package com.github.alien11689.taskmanager.domain.task.event;

import com.github.alien11689.taskmanager.domain.task.TaskId;
import com.github.alien11689.taskmanager.domain.task.UserId;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class TaskAssigned extends TaskEvent {
    TaskId taskId;
    UserId assignee;
    LocalDateTime assignedAt;
    long version;

    @Override
    public EventType getEventType() {
        return EventType.TASK_ASSIGNED;
    }
}
