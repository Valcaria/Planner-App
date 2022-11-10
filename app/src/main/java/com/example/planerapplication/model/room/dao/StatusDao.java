package com.example.planerapplication.model.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.planerapplication.model.room.entity.relations.TaskType;
import com.example.planerapplication.model.room.entity.Status;

import java.util.List;

@Dao
public interface StatusDao {

    @Insert
    void insert(Status... status);

    @Query("SELECT id FROM status_table WHERE status = :status")
    LiveData<Integer> selectStatus(String status);

    @Transaction
    @Query("SELECT * FROM status_table WHERE id = :id")
    LiveData<List<TaskType>> getStatusOfTask(int id);
}
