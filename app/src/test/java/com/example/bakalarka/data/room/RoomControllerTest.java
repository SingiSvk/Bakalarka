package com.example.bakalarka.data.room;

import com.example.bakalarka.data.room.patient.Patient;
import com.example.bakalarka.database.RoomDatabaseController;

import junit.framework.TestCase;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RoomControllerTest extends TestCase {

    public void testAddRoom(){
        Patient patient = new Patient("jozo", 15);
        Room room = new Room(1, patient);
        new RoomController().addRoom(room);
    }

    public void testGetRoomById() {
        assertNotNull(new RoomController().getRoomById(1));
        assertNull(new RoomController().getRoomById(2));
    }

    public void testGetRiskyRooms() {
        assertEquals(new ArrayList<>(), new RoomController().getRiskyRooms());
    }

    public void testGetAllIds() {
        assertNotNull(new RoomController().getAllIds());
    }

    public void testContainsRoom() {
        assertTrue(new RoomController().containsRoom(1));
        assertFalse(new RoomController().containsRoom(2));
    }

    public void testCreateConditions() {
        RoomController roomController = new RoomController();
        roomController.createConditions(roomController.getRoomById(1));
    }
}