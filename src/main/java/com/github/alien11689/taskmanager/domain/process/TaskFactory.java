package com.github.alien11689.taskmanager.domain.process;

import com.github.alien11689.taskmanager.domain.task.Task;
import com.github.alien11689.taskmanager.domain.task.TaskId;
import com.github.alien11689.taskmanager.domain.task.TaskStatus;
import com.github.alien11689.taskmanager.domain.ports.incoming.NewTask;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class TaskFactory {

    static Task newTask(NewTask newTask) {
        return Task.builder()
                .taskId(TaskId.generateId())
                .assignee(newTask.getAssignee())
                .createdAt(LocalDateTime.now())
                .description(newTask.getDescription())
                .title(newTask.getTitle())
                .status(newTask.getAssignee() == null ? TaskStatus.NEW : TaskStatus.ASSIGNED)
                .build();
    }
}
