package com.github.alien11689.taskmanager.domain.ports.outgoing;

import com.github.alien11689.taskmanager.domain.task.event.TaskEvent;

public interface EventPublisher {
    void publish(TaskEvent taskEvent);
}
