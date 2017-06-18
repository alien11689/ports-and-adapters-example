package com.github.alien11689.taskmanager.domain.task.event;

import com.github.alien11689.taskmanager.domain.task.TaskId;

public abstract class TaskEvent implements Comparable<TaskEvent> {
    public abstract TaskId getTaskId();

    public abstract long getVersion();

    public abstract EventType getEventType();

    public int compareTo(TaskEvent o) {
        if (this.getVersion() == o.getVersion()) {
            return 0;
        }
        if (this.getVersion() < o.getVersion()) {
            return -1;
        }
        return 1;
    }
}
