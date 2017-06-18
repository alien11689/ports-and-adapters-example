package com.github.alien11689.taskmanager.domain.task;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UnknownTaskStatus extends RuntimeException {
    private final TaskStatus status;
}
