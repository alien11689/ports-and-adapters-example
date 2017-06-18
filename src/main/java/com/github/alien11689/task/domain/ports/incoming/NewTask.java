package com.github.alien11689.task.domain.ports.incoming;

import com.github.alien11689.task.domain.ports.dto.user.UserId;
import lombok.Value;

@Value
public class NewTask {
    String title;
    String description;
    UserId assignee;
}
