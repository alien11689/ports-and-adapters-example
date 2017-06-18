package com.github.alien11689.taskmanager.domain.ports.outgoing;

import com.github.alien11689.taskmanager.domain.task.TaskId;
import com.github.alien11689.taskmanager.domain.task.event.TaskEvent;

import java.util.List;

public interface TaskEventStore {
    void publish(List<? extends TaskEvent> taskEvents);
    List<TaskEvent> get(TaskId taskId);
}
