package com.example.bakalarka.data.room;

import android.annotation.SuppressLint;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bakalarka.data.MyService;
import com.example.bakalarka.data.room.events.Event;
import com.example.bakalarka.data.room.events.EventController;
import com.example.bakalarka.data.wear.WearController;
import com.example.bakalarka.database.RoomDatabaseController;
import com.example.bakalarka.data.risks.Conditions;
import com.example.bakalarka.data.risks.ConditionsController;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class RoomController {
    // Rooms list
    public static final ArrayList<Room> rooms = new ArrayList<>();

    // Controller for Android Wear
    @NonNull
    private final WearController wearController;

    // Constructor
    public RoomController() {
        this.wearController = new WearController();
    }

    // Adding rooms
    public void addRooms(@NonNull List<Room> rooms){
        for (Room room: rooms){
            addRoom(room);
        }
    }

    public void addRoom(@NonNull Room room){
        if (room.getConditionsMap() == null){
            createConditions(room);
        }
        startDataSave(room);
        rooms.add(room);
    }

    // Methods for getting rooms
    @NonNull
    public Room getRoomById(@Nullable Integer id){
        if (id == null){
            throw new NullPointerException("GetRoomById: id is null: "+id);
        }
        for (Room room: rooms){
            if (room.getId() == id){
                return room;
            }
        }
        return null;
        //throw new NullPointerException("GetRoomById: No room with id: "+id);
    }

    public int getPositionInList(int id){
        Room room = getRoomById(id);
        return rooms.indexOf(room);
    }

    @NonNull
    public List<Room> getRiskyRooms(){
        List<Room> riskyRooms = new ArrayList<>();
        for(Room room: rooms){
            if(room.getRiskLevel() > ConditionsController.NONE){
                riskyRooms.add(room);
            }
        }
        return riskyRooms;
    }

    @NonNull
    public ArrayList<Integer> getAllIds(){
        ArrayList<Integer> ids = new ArrayList<>();
        for (Room room: rooms){
            ids.add(room.getId());
        }
        return ids;
    }

    public void removeRoom(int roomId) {
        Room room = getRoomById(roomId);
        try {
            room.getMqttClient().getClient().disconnect();
        } catch (MqttException e) {
            System.out.println(e.getMessage());
        }
        new Thread(() -> new RoomDatabaseController().deleteWholeRoom(roomId)).start();
        rooms.remove(room);
    }


    public boolean containsRoom(int id){
        for (Room room: rooms){
            if (room.getId() == id){
                return true;
            }
        }
        return false;
    }

    public void createConditions(@NonNull Room room){
        Map<String, Conditions> conditionsMap = new HashMap<>();

        conditionsMap.put(Conditions.TEMPERATURE_CONDITIONS, new Conditions(Conditions.TEMPERATURE_CONDITIONS));
        conditionsMap.put(Conditions.HUMIDITY_CONDITIONS, new Conditions(Conditions.HUMIDITY_CONDITIONS));
        conditionsMap.put(Conditions.PRESSURE_CONDITIONS, new Conditions(Conditions.PRESSURE_CONDITIONS));
        conditionsMap.put(Conditions.VOC_CONDITIONS, new Conditions(Conditions.VOC_CONDITIONS));

        room.setConditionsMap(conditionsMap);
    }

    // Begin saving data for room
    public void startDataSave(@NonNull Room room){


        //startService(new Intent(this, MyService.class));

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {

                if (!containsRoom(room.getId())){
                    timer.cancel();
                    timer.purge();
                    return;
                }

                RoomData roomData = room.getMqttClient().getRoomData();

                room.setRoomData(roomData);
                room.getRoomDataRecords().addRecord(roomData);

                ConditionsController conditionsController = new ConditionsController();
                Map<String, Integer> risks = conditionsController.getRisks(room);

                int riskLevel = conditionsController.getRiskLevel(room);
                room.setRiskLevel(riskLevel);

                String message = conditionsController.createNotification(room.getNotification(), risks, room.getId());

                if(!message.equals("0")){
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter= new SimpleDateFormat("MM-dd HH:mm");
                    Date date = new Date(System.currentTimeMillis());
                    Event event = new Event(formatter.format(date), message, riskLevel);
                    new EventController().saveEvent(room, event);
                }


                wearController.sendRoomData(room.getId(), roomData);
            }
        }, 2*60*1000, 2*60*1000);
    }
}
