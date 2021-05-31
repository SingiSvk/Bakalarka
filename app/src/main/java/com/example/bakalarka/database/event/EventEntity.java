package com.example.bakalarka.database.event;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class EventEntity {
    @PrimaryKey(autoGenerate = true)
    int id;
    final int roomId;
    final String time;
    final String event;
    final int risk;

    public EventEntity(int roomId, String time, String event, int risk) {
        this.roomId = roomId;
        this.time = time;
        this.event = event;
        this.risk = risk;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getTime() {
        return time;
    }

    public String getEvent() {
        return event;
    }


    public int getRisk() {
        return risk;
    }


    @Override
    public String toString() {
        return "EventEntity{" +
                "id=" + id +
                ", roomId=" + roomId +
                ", time=" + time +
                ", event='" + event + '\'' +
                ", risk=" + risk +
                '}';
    }
}
