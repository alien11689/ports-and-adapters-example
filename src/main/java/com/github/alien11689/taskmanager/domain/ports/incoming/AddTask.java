package com.github.alien11689.taskmanager.domain.ports.incoming;

import com.github.alien11689.taskmanager.domain.task.TaskId;

public interface AddTask {
    TaskId add(NewTask newTask);
}
