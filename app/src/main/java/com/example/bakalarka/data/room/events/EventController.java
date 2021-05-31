package com.example.bakalarka.data.room.events;

import android.annotation.SuppressLint;

import com.example.bakalarka.data.room.Room;
import com.example.bakalarka.database.RoomDatabaseController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class EventController {
    public static final int NUM_OF_EVENTS = 3;
    public static final int NUM_OF_EVENTS_IN_ROOM = 50;


    public List<Event> getLast3Events(List<Event> events){
        if (events.size() <= NUM_OF_EVENTS){
            return events;
        }else {
            List<Event> lastEvents = new ArrayList<>();
            for (int i = 0; i<NUM_OF_EVENTS; i++){
                lastEvents.add(0, events.get(i));
            }
            Collections.reverse(lastEvents);
            return lastEvents;
        }
    }

    public String getTime(String time) throws ParseException {
        String[] timeArr = time.split(" ");

        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");

        Date eventDate = formatter.parse(timeArr[0]);
        Date currentDate = formatter.parse(formatter.format(new Date()));

        assert currentDate != null;
        assert eventDate != null;
        long diffInMillis = Math.abs(currentDate.getTime() - eventDate.getTime());
        long diff = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);

        String timeText = "";
        if (diff == 0){
            timeText += "Dnes " + timeArr[1];
        }else if (diff == 1) {
            timeText += "Včera " + timeArr[1];
        }else{
            timeText += "Pred " + diff + " dňami";
        }

        return  timeText;
    }

    public String getEventMessage(String message){

        // int high = message.lastIndexOf("Vysoké riziko");
        int low = message.lastIndexOf("Nízke riziko");

        String[] highRisks = message.substring(14, low).split(",");
        String[] lowRisks = message.substring(low + 13).split(",");

        StringBuilder newMessage = new StringBuilder();
        for (int i = 0; i<highRisks.length-1; i++){
            newMessage.append("<font color='#C00000'>").append(highRisks[i]).append("</font>").append(", ");
        }
        for (int i = 0; i<lowRisks.length-1; i++){
            newMessage.append("<font color='#FF6600'>").append(lowRisks[i]).append("</font>").append(", ");
        }
        return newMessage.toString();
    }

    public String eventToString(Event event) throws ParseException {
        String message = "";
        message += getTime(event.getTime()) + ":<br>";
        message += getEventMessage(event.event) + "<br>";
        return message;
    }

    public boolean compareEvents(Event event1, Event event2){
        return event1.getEvent().equals(event2.getEvent());
    }

    public void saveEvent(Room room, Event event){
        List<Event> events = room.getEvents();
        if (events.size() > 0){
            Event lastEvent = events.get(0);
            if(!compareEvents(lastEvent, event)){
                addEvent(room, event);
            }
        }else{
            addEvent(room, event);
        }
    }

    public void addEvent(Room room, Event event){
        List<Event> events = room.getEvents();
        events.add(0, event);
        while (events.size() > NUM_OF_EVENTS_IN_ROOM){
            events.remove(events.size()-1);
        }
        new RoomDatabaseController().saveEventInDB(room.getId(), event);
    }

    public List<Event> getAllEventsLast50(){
        RoomDatabaseController roomDatabaseController = new RoomDatabaseController();
        return roomDatabaseController.createEventsFromDB(roomDatabaseController.list50Events());
    }
}
