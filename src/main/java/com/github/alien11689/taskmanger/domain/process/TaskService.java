package com.github.alien11689.taskmanger.domain.process;

import com.github.alien11689.taskmanger.domain.ports.dto.task.Task;
import com.github.alien11689.taskmanger.domain.ports.dto.task.TaskId;
import com.github.alien11689.taskmanger.domain.ports.dto.task.TaskStatus;
import com.github.alien11689.taskmanger.domain.ports.dto.user.UserId;
import com.github.alien11689.taskmanger.domain.ports.incoming.AddTask;
import com.github.alien11689.taskmanger.domain.ports.incoming.AssignTask;
import com.github.alien11689.taskmanger.domain.ports.incoming.CompleteTask;
import com.github.alien11689.taskmanger.domain.ports.incoming.NewTask;
import com.github.alien11689.taskmanger.domain.ports.outgoing.*;

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
        validateUserExistence(newTask.getAssignee());
        TaskId taskId = TaskId.generateId();
        LocalDateTime creationDate = LocalDateTime.now();
        Task task = Task.builder()
                .taskId(taskId)
                .createdAt(creationDate)
                .assignee(newTask.getAssignee())
                .description(newTask.getDescription())
                .title(newTask.getTitle())
                .status(newTask.getAssignee() == null ? TaskStatus.NEW : TaskStatus.ASSIGNED)
                .build();
        taskRepository.add(task);
        eventPublisher.taskCreated(TaskCreated.builder()
                .taskId(taskId)
                .createdAt(creationDate)
                .title(newTask.getTitle())
                .build());
        if (newTask.getAssignee() != null) {
            eventPublisher.taskAssigned(TaskAssigned.builder()
                    .taskId(taskId)
                    .assignedAt(creationDate)
                    .assignee(newTask.getAssignee())
                    .build());
        }
        return taskId;
    }

    private void validateUserExistence(UserId userId) {
        if (userId != null && !userRepository.exist(userId)) {
            throw new UserDoesNotExist(userId);
        }
    }

    public void assign(TaskId taskId, UserId userId) {
        validateUserExistence(userId);
        Task task = taskRepository.getTask(taskId);
        if (task.getStatus() == TaskStatus.COMPLETED) {
            throw new CannotChangedCompletedTask(taskId);
        }
        taskRepository.changeAssignee(taskId, userId);
        LocalDateTime assignedAt = LocalDateTime.now();
        taskRepository.changeStatus(taskId, TaskStatus.ASSIGNED, assignedAt);
        eventPublisher.taskAssigned(TaskAssigned.builder()
                .taskId(taskId)
                .assignee(userId)
                .assignedAt(assignedAt)
                .build());
    }

    public void completeTask(TaskId taskId) {
        Task task = taskRepository.getTask(taskId);
        if (task.getStatus() == TaskStatus.COMPLETED) {
            return;
        }
        if (task.getStatus() != TaskStatus.ASSIGNED) {
            throw new CannotCompleteNotAssignedTask(taskId);
        }
        LocalDateTime completedAt = LocalDateTime.now();
        taskRepository.changeStatus(taskId, TaskStatus.COMPLETED, completedAt);
        eventPublisher.taskCompleted(TaskCompleted.builder()
                .taskId(taskId)
                .completedAt(completedAt)
                .build());
    }
}
