package com.github.alien11689.taskmanager.domain.task.event;

import com.github.alien11689.taskmanager.domain.task.TaskId;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class TaskCreated extends TaskEvent {
    TaskId taskId;
    LocalDateTime createdAt;
    String title;
    String description;
    long version;


    @Override
    public EventType getEventType() {
        return EventType.TASK_CREATED;
    }
}
