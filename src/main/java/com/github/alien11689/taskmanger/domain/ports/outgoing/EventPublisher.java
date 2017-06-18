package com.github.alien11689.taskmanger.domain.ports.outgoing;

public interface EventPublisher {
    void taskCreated(TaskCreated taskCreated);
    void taskAssigned(TaskAssigned taskAssigned);
    void taskCompleted(TaskCompleted taskCompleted);
}
