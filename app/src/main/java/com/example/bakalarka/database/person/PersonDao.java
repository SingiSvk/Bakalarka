package com.example.bakalarka.database.person;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PersonDao {
    @NonNull
    @Query("SELECT * FROM PersonEntity")
    List<PersonEntity> getAll();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertPerson(PersonEntity personEntity);

    @Delete
    void delete(PersonEntity personEntity);

    @Query("SELECT * FROM PersonEntity WHERE roomId=:roomId")
    PersonEntity findPersonByRoomId(int roomId);
}