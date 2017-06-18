package com.github.alien11689.taskmanager.domain.ports.outgoing;

import com.github.alien11689.taskmanager.domain.task.Task;
import com.github.alien11689.taskmanager.domain.task.TaskId;

public interface TaskRepository {
    void save(Task task);
    Task getTask(TaskId taskId);
}
