package com.example.bakalarka.database.event;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EventDao {
    @NonNull
    @Query("SELECT * FROM EventEntity ORDER BY time")
    List<EventEntity> getAll();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(EventEntity eventEntity);

    @Delete
    void delete(EventEntity eventEntity);

    @NonNull
    @Query("SELECT * FROM EventEntity WHERE roomId=:roomId ORDER BY time DESC")
    List<EventEntity> findEventsByRoomId(int roomId);

    @NonNull
    @Query("SELECT * FROM EventEntity ORDER BY time DESC LIMIT 50")
    List<EventEntity> get50();

}