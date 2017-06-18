package com.github.alien11689.taskmanager.domain.task;

import com.github.alien11689.taskmanager.domain.process.CannotChangedCompletedTask;
import com.github.alien11689.taskmanager.domain.process.CannotCompleteNotAssignedTask;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class Task {
    private final TaskId taskId;
    private final LocalDateTime createdAt;
    private UserId assignee;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDateTime completedAt;

    public Task(TaskId taskId, LocalDateTime createdAt, UserId assignee, String title, String description, TaskStatus status, LocalDateTime completedAt) {
        this.taskId = taskId;
        this.createdAt = createdAt;
        this.assignee = assignee;
        this.title = title;
        this.description = description;
        this.status = status;
        this.completedAt = completedAt;
        new TaskValidator().validate();
    }

    public TaskId taskId() {
        return taskId;
    }

    public LocalDateTime createdAt() {
        return createdAt;
    }

    public UserId assignee() {
        return assignee;
    }

    public String title() {
        return title;
    }

    public String description() {
        return description;
    }

    public TaskStatus status() {
        return status;
    }

    public LocalDateTime completedAt() {
        return completedAt;
    }

    public void assignTo(UserId userId) {
        if (status == TaskStatus.COMPLETED) {
            throw new CannotChangedCompletedTask(taskId);
        }
        status = TaskStatus.ASSIGNED;
        assignee = userId;
    }

    public void complete() {
        if (status == TaskStatus.COMPLETED) {
            return;
        }
        if (status != TaskStatus.ASSIGNED) {
            throw new CannotCompleteNotAssignedTask(taskId);
        }
        status = TaskStatus.COMPLETED;
        completedAt = LocalDateTime.now();
    }

    private class TaskValidator {
        private void validate() {
            if (taskId == null) {
                throw new InvalidTask("Task must have id");
            }
            if (createdAt == null) {
                throw new InvalidTask("Task must have creation date");
            }
            if (createdAt.isAfter(LocalDateTime.now())) {
                throw new InvalidTask("Creation cannot be in future");
            }
            if (title == null) {
                throw new InvalidTask("Task must have title");
            }
            if (description == null) {
                throw new InvalidTask("Task must have description");
            }
            if (status == null) {
                throw new InvalidTask("Task must have status");
            }
            switch (status) {
                case NEW:
                    validateNew();
                    break;
                case ASSIGNED:
                    validateAssigned();
                    break;
                case COMPLETED:
                    validateCompleted();
                    break;
                default:
                    throw new UnknownTaskStatus(status);
            }
        }

        private void validateAssigned() {
            if (completedAt != null) {
                throw new InvalidTask("Assigned task cannot have complete date");
            }
            if (assignee == null) {
                throw new InvalidTask("Assigned task must have assignee");
            }
        }

        private void validateNew() {
            if (completedAt != null) {
                throw new InvalidTask("New task cannot have complete date");
            }
            if (assignee != null) {
                throw new InvalidTask("New task cannot have assignee");
            }
        }

        private void validateCompleted() {
            if (completedAt == null) {
                throw new InvalidTask("Completed task must have complete date");
            }
            if (completedAt.isAfter(LocalDateTime.now())) {
                throw new InvalidTask("Completed task must have complete date in past");
            }
        }
    }
}
