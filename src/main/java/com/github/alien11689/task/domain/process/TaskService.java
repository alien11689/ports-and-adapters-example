package com.github.alien11689.task.domain.process;

import com.github.alien11689.task.domain.ports.dto.task.Task;
import com.github.alien11689.task.domain.ports.dto.task.TaskId;
import com.github.alien11689.task.domain.ports.dto.task.TaskStatus;
import com.github.alien11689.task.domain.ports.dto.user.UserId;
import com.github.alien11689.task.domain.ports.incoming.AddTask;
import com.github.alien11689.task.domain.ports.incoming.ChangeTaskAssignee;
import com.github.alien11689.task.domain.ports.incoming.CompleteTask;
import com.github.alien11689.task.domain.ports.incoming.NewTask;
import com.github.alien11689.task.domain.ports.outgoing.*;

import java.time.LocalDateTime;

public class TaskService implements AddTask, ChangeTaskAssignee, CompleteTask {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final EventSender eventSender;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository, EventSender eventSender) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.eventSender = eventSender;
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
        taskRepository.save(task);
        eventSender.taskCreated(TaskCreated.builder()
                .taskId(taskId)
                .assignee(newTask.getAssignee())
                .createdAt(creationDate)
                .title(newTask.getTitle())
                .build());
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
        if(task.getStatus() != TaskStatus.COMPLETED){
            throw new CannotChangedCompletedTask(taskId);
        }
        taskRepository.changeAssignee(taskId, userId);
        LocalDateTime assignedAt = LocalDateTime.now();
        taskRepository.changeStatus(taskId, TaskStatus.ASSIGNED, assignedAt);
        eventSender.taskAssigned(TaskAssigned.builder()
                .taskId(taskId)
                .assignee(userId)
                .assignedAt(assignedAt)
                .build());
    }

    public void completeTask(TaskId taskId) {
        Task task = taskRepository.getTask(taskId);
        if(task.getStatus() == TaskStatus.COMPLETED){
            return;
        }
        if(task.getStatus() != TaskStatus.ASSIGNED){
            throw new CannotCompleteNotAssignedTask(taskId);
        }
        LocalDateTime completedAt = LocalDateTime.now();
        taskRepository.changeStatus(taskId, TaskStatus.COMPLETED, completedAt);
        eventSender.taskCompleted(TaskCompleted.builder()
                .taskId(taskId)
                .completedAt(completedAt)
                .build());
    }
}
