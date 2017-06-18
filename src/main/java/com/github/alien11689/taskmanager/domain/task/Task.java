package com.github.alien11689.taskmanager.domain.task;

import com.github.alien11689.taskmanager.domain.process.CannotChangedCompletedTask;
import com.github.alien11689.taskmanager.domain.process.CannotCompleteNotAssignedTask;
import com.github.alien11689.taskmanager.domain.task.event.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Task {
    private final TaskId taskId;
    private final LocalDateTime createdAt;
    private UserId assignee;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDateTime completedAt;
    private long version = 0;
    private List<TaskEvent> unPublishedEvents = new ArrayList<>();

    private Task(TaskId taskId, LocalDateTime createdAt) {
        this.taskId = taskId;
        this.createdAt = createdAt;
        this.status = TaskStatus.NEW;
    }

    public static Task newTask(String description, String title) {
        Task task = new Task(TaskId.generateId(), LocalDateTime.now());
        task.title = title;
        task.description = description;
        TaskCreated taskCreated = TaskCreated.builder()
                .createdAt(task.createdAt)
                .description(task.description)
                .taskId(task.taskId)
                .title(task.title)
                .version(task.version)
                .build();
        task.unPublishedEvents.add(taskCreated);
        task.validate();
        return task;
    }

    private void validate() {
        new TaskValidator().validate();
    }

    public TaskId taskId() {
        return taskId;
    }

    public void assignTo(UserId userId) {
        assignToWithEvent(userId, true);
    }

    public void complete() {
        completeWithEvent(LocalDateTime.now(), true);
    }

    private void completeWithEvent(LocalDateTime completedAt, boolean withEvent) {
        if (status == TaskStatus.COMPLETED) {
            return;
        }
        if (status != TaskStatus.ASSIGNED) {
            throw new CannotCompleteNotAssignedTask(taskId);
        }
        status = TaskStatus.COMPLETED;
        this.completedAt = completedAt;
        ++version;
        if (withEvent) {
            unPublishedEvents.add(TaskCompleted.builder()
                    .taskId(taskId)
                    .completedAt(completedAt)
                    .version(version)
                    .build());
        }
        validate();
    }

    public static Task from(List<TaskEvent> events) {
        if (events.isEmpty()) {
            throw new InvalidTaskEventStream();
        }
        events.sort(TaskEvent::compareTo);
        TaskEvent firstEvent = events.get(0);
        if (firstEvent.getEventType() != EventType.TASK_CREATED) {
            throw new InvalidTaskEventStream();
        }
        TaskCreated taskCreated = (TaskCreated) firstEvent;
        Task task = new Task(taskCreated.getTaskId(), taskCreated.getCreatedAt());
        task.title = taskCreated.getTitle();
        task.description = taskCreated.getDescription();
        IntStream.range(1, events.size())
                .mapToObj(events::get)
                .forEach(task::apply);
        return task;
    }

    private void apply(TaskEvent taskEvent) {
        ++version;
        if (taskEvent.getEventType() == EventType.TASK_ASSIGNED) {
            TaskAssigned taskAssigned = (TaskAssigned) taskEvent;
            assignToWithEvent(taskAssigned.getAssignee(), false);
        } else if (taskEvent.getEventType() == EventType.TASK_COMPLETED) {
            TaskCompleted taskCompleted = (TaskCompleted) taskEvent;
            completeWithEvent(taskCompleted.getCompletedAt(), false);
        } else {
            throw new InvalidTaskEventStream();
        }
        validate();
    }

    private void assignToWithEvent(UserId userId, boolean withEvent) {
        if (status == TaskStatus.COMPLETED) {
            throw new CannotChangedCompletedTask(taskId);
        }
        status = TaskStatus.ASSIGNED;
        assignee = userId;
        ++version;
        if (withEvent) {
            unPublishedEvents.add(TaskAssigned.builder()
                    .taskId(taskId)
                    .assignedAt(LocalDateTime.now())
                    .version(version)
                    .assignee(assignee)
                    .build());
        }
        validate();
    }

    public List<TaskEvent> unpublishedEvents() {
        return this.unPublishedEvents;
    }

    public void resetEvents(){
        unPublishedEvents = new ArrayList<>();
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
