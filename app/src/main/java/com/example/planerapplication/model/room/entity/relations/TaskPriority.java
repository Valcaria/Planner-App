package com.example.planerapplication.model.room.entity.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.planerapplication.model.room.entity.Priority;
import com.example.planerapplication.model.room.entity.Tasks;

import java.util.List;

public class TaskPriority {
    @Embedded
    private Priority priority;
    @Relation(
            parentColumn = "id",
            entityColumn = "priorityID"
    )
    public List<Tasks> tasks;

    public TaskPriority(Priority priority, List<Tasks> tasks) {
        this.priority = priority;
        this.tasks = tasks;
    }
}
