package com.github.alien11689.taskmanager.domain.process;

import com.github.alien11689.taskmanager.domain.ports.incoming.AddTask;
import com.github.alien11689.taskmanager.domain.ports.incoming.AssignTask;
import com.github.alien11689.taskmanager.domain.ports.incoming.CompleteTask;
import com.github.alien11689.taskmanager.domain.ports.incoming.NewTask;
import com.github.alien11689.taskmanager.domain.ports.outgoing.TaskEventStore;
import com.github.alien11689.taskmanager.domain.ports.outgoing.UserRepository;
import com.github.alien11689.taskmanager.domain.task.Task;
import com.github.alien11689.taskmanager.domain.task.TaskId;
import com.github.alien11689.taskmanager.domain.task.UserId;
import com.github.alien11689.taskmanager.domain.task.event.TaskCompleted;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TaskService implements AddTask, AssignTask, CompleteTask {

    private final UserRepository userRepository;
    private final TaskEventStore taskEventStore;

    public TaskId add(NewTask newTask) {
        UserId userId = newTask.getAssignee();
        if (userId != null && !userRepository.exist(userId)) {
            throw new UserDoesNotExist(userId);
        }
        Task task = Task.newTask(newTask.getTitle(), newTask.getDescription());
        if (newTask.getAssignee() != null) {
            task.assignTo(userId);
        }
        taskEventStore.publish(task.unpublishedEvents());
        return task.taskId();
    }

    public void assign(TaskId taskId, UserId userId) {
        if (!userRepository.exist(userId)) {
            throw new UserDoesNotExist(userId);
        }
        Task task = Task.from(taskEventStore.get(taskId));
        task.assignTo(userId);
        taskEventStore.publish(task.unpublishedEvents());
    }

    public void completeTask(TaskId taskId) {
        Task task = Task.from(taskEventStore.get(taskId));
        task.complete();
        taskEventStore.publish(task.unpublishedEvents());
    }
}
