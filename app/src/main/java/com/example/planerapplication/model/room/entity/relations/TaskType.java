package com.example.planerapplication.model.room.entity.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.planerapplication.model.room.entity.Tasks;
import com.example.planerapplication.model.room.entity.Type;

import java.util.List;

public class TaskType {
    @Embedded private Type type;
    @Relation(
            parentColumn = "id",
            entityColumn = "typeID"
    )
    public List<Tasks> tasks;


    public TaskType(Type type, List<Tasks> tasks) {
        this.type = type;
        this.tasks = tasks;
    }
}
