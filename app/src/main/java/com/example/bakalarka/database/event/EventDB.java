package com.example.bakalarka.database.event;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {EventEntity.class}, version = 1, exportSchema = false)
public abstract class EventDB extends RoomDatabase {
    public abstract EventDao eventDao();

    private static volatile EventDB INSTANCE;

    public static EventDB getDatabase(@NonNull final Context context) {
        if (INSTANCE == null) {
            //context.getApplicationContext().deleteDatabase("event_database");
            synchronized (EventDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            EventDB.class, "event_database")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
