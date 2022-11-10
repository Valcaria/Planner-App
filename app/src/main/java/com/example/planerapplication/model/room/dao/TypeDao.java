package com.example.planerapplication.model.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.planerapplication.model.room.entity.relations.TaskType;
import com.example.planerapplication.model.room.entity.Type;

import java.util.List;

@Dao
public interface TypeDao {

    @Insert
    void insert(Type... Type);

    @Query("SELECT id FROM type_table WHERE type = :type")
    LiveData<Integer> selectType(String type);

    @Transaction
    @Query("SELECT * FROM type_table WHERE id = :id")
    LiveData<List<TaskType>> getTypeOfTask(int id);
}
