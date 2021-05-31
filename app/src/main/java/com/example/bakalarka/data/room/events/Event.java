package com.example.bakalarka.data.room.events;

public class Event {

    int roomId;
    final String time;
    final String event;
    final int risk;

    public Event(int roomId, String time, String event, int risk) {
        this.roomId = roomId;
        this.time = time;
        this.event = event;
        this.risk = risk;
    }

    public Event(String time, String event, int risk) {
        this.time = time;
        this.event = event;
        this.risk = risk;
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
        return time + ": " + event;
    }
}
