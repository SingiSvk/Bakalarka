package com.example.bakalarka.data.room.events;

import android.annotation.SuppressLint;

import com.example.bakalarka.data.risks.ConditionsController;
import com.example.bakalarka.data.room.Room;
import com.example.bakalarka.data.room.patient.Patient;

import junit.framework.TestCase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventControllerTest extends TestCase {

    public List<Event> createData(){
        SimpleDateFormat formatter= new SimpleDateFormat("MM-dd HH:mm");
        Date date = new Date(System.currentTimeMillis());

        List<Event> events = new ArrayList<>();
        Event event1 = new Event(formatter.format(date), "Vysoké riziko: teplota, VOC, Nízke riziko: Vlhkosť,", ConditionsController.LOW);
        Event event2 = new Event(formatter.format(date), "Vysoké riziko: VOC, Nízke riziko: VOC,", ConditionsController.HIGH);
        Event event3 = new Event(formatter.format(date), "Vysoké riziko: teplota, VOC, Nízke riziko: Vlhkosť,", ConditionsController.LOW);
        Event event4 = new Event(formatter.format(date), "Vysoké riziko: Tlak, Nízke riziko: Vlhkosť, Teplota,", ConditionsController.LOW);
        events.add(event1);
        events.add(event2);
        events.add(event3);
        events.add(event4);

        return events;
    }

    public void testGetLast3Events() {
        List<Event> events = createData();
        assertNotNull(new EventController().getLast3Events(events));
        assertEquals(4, events.size());
        events.remove(1);
        events.remove(0);
        assertEquals(2, events.size());
        events.remove(0);
        assertEquals(1, events.size());
    }

    public void testGetEventMessage() {
        List<Event> events = createData();
        assertEquals("<font color='#C00000'> teplota</font>, <font color='#C00000'> VOC</font>, ",new EventController().getEventMessage(events.get(0).getEvent()));

    }

    public void testCompareEvents() {
        List<Event> events = createData();
        assertFalse(new EventController().compareEvents(events.get(0), events.get(1)));
        assertTrue(new EventController().compareEvents(events.get(0), events.get(2)));
    }

    public void testSaveEvent() {
        Patient patient = new Patient("jozo", 15);
        Room room = new Room(1, patient);
        List<Event> events = createData();
        new EventController().saveEvent(room, events.get(0));
        assertEquals(1, room.getEvents().size());
        new EventController().saveEvent(room, events.get(2));
        assertEquals(1, room.getEvents().size());
        new EventController().saveEvent(room, events.get(1));
        assertEquals(2, room.getEvents().size());
    }
}