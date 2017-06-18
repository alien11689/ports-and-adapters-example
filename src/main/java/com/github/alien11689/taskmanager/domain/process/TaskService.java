package com.github.alien11689.taskmanager.domain.process;

import com.github.alien11689.taskmanager.domain.task.Task;
import com.github.alien11689.taskmanager.domain.task.TaskId;
import com.github.alien11689.taskmanager.domain.task.UserId;
import com.github.alien11689.taskmanager.domain.task.event.TaskAssigned;
import com.github.alien11689.taskmanager.domain.task.event.TaskCompleted;
import com.github.alien11689.taskmanager.domain.task.event.TaskCreated;
import com.github.alien11689.taskmanager.domain.ports.incoming.AddTask;
import com.github.alien11689.taskmanager.domain.ports.incoming.AssignTask;
import com.github.alien11689.taskmanager.domain.ports.incoming.CompleteTask;
import com.github.alien11689.taskmanager.domain.ports.incoming.NewTask;
import com.github.alien11689.taskmanager.domain.ports.outgoing.EventPublisher;
import com.github.alien11689.taskmanager.domain.ports.outgoing.TaskRepository;
import com.github.alien11689.taskmanager.domain.ports.outgoing.UserRepository;

import java.time.LocalDateTime;

public class TaskService implements AddTask, AssignTask, CompleteTask {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final EventPublisher eventPublisher;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository, EventPublisher eventPublisher) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
    }

    public TaskId add(NewTask newTask) {
        UserId userId = newTask.getAssignee();
        if (userId != null && !userRepository.exist(userId)) {
            throw new UserDoesNotExist(userId);
        }
        Task task = TaskFactory.newTask(newTask);
        taskRepository.save(task);
        TaskId taskId = task.taskId();
        LocalDateTime createdAt = task.createdAt();
        eventPublisher.publish(TaskCreated.builder()
                .taskId(taskId)
                .createdAt(createdAt)
                .title(newTask.getTitle())
                .build());
        if (newTask.getAssignee() != null) {
            eventPublisher.publish(TaskAssigned.builder()
                    .taskId(taskId)
                    .assignedAt(createdAt)
                    .assignee(newTask.getAssignee())
                    .build());
        }
        return taskId;
    }

    public void assign(TaskId taskId, UserId userId) {
        if (!userRepository.exist(userId)) {
            throw new UserDoesNotExist(userId);
        }
        Task task = taskRepository.getTask(taskId);
        task.assignTo(userId);
        taskRepository.save(task);
        eventPublisher.publish(TaskAssigned.builder()
                .taskId(taskId)
                .assignee(userId)
                .assignedAt(LocalDateTime.now())
                .build());
    }

    public void completeTask(TaskId taskId) {
        Task task = taskRepository.getTask(taskId);
        task.complete();
        taskRepository.save(task);
        eventPublisher.publish(TaskCompleted.builder()
                .taskId(taskId)
                .completedAt(task.completedAt())
                .build());
    }
}
