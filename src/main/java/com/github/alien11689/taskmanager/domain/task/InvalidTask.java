package com.github.alien11689.taskmanager.domain.task;

public class InvalidTask extends RuntimeException {
    public InvalidTask(String message) {
        super(message);
    }
}
