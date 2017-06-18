package com.github.alien11689.task.domain.ports.outgoing;

public interface EventSender {
    void taskCreated(TaskCreated taskCreated);
    void taskAssigned(TaskAssigned taskAssigned);
    void taskCompleted(TaskCompleted taskCompleted);
}
