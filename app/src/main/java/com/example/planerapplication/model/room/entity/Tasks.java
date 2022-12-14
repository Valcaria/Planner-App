package com.example.planerapplication.model.room.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "task_table",
foreignKeys = {
        @ForeignKey(entity = Type.class,
                parentColumns = "id",
                childColumns = "typeID"),
        @ForeignKey(entity = Status.class,
                parentColumns = "id",
                childColumns = "statusID"),
        @ForeignKey(entity = Priority.class,
                parentColumns = "id",
                childColumns = "priorityID")
})
public class Tasks {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int position;
    private String taskEventName;
    private int typeID;
    private Integer  priorityID;
    private Integer  statusID;
    private String description;
    private long dateTime;
    private short alarmSet;
    private short alarmDisabled;

    public Tasks(int position, String taskEventName, int typeID, Integer priorityID, Integer statusID, String description, long dateTime, short alarmSet, short alarmDisabled) {
        this.position = position;
        this.taskEventName = taskEventName;
        this.typeID = typeID;
        this.priorityID = priorityID;
        this.statusID = statusID;
        this.description = description;
        this.dateTime = dateTime;
        this.alarmSet = alarmSet;
        this.alarmDisabled = alarmDisabled;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public String getTaskEventName() {
        return taskEventName;
    }

    public int getTypeID() {
        return typeID;
    }

    public Integer getPriorityID() {
        return priorityID;
    }

    public void setStatusID(Integer statusID) {
        this.statusID = statusID;
    }

    public Integer getStatusID() {
        return statusID;
    }

    public String getDescription() {
        return description;
    }

    public long getDateTime() {
        return dateTime;
    }

    public short getAlarmSet() {
        return alarmSet;
    }

    public short getAlarmDisabled() {
        return alarmDisabled;
    }
}
