package com.github.alien11689.taskmanager.domain.ports.incoming;

import com.github.alien11689.taskmanager.domain.task.UserId;
import lombok.Value;

@Value
public class NewTask {
    String title;
    String description;
    UserId assignee;
}
