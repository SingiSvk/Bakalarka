package com.example.bakalarka.data.room;

import androidx.annotation.NonNull;

import com.example.bakalarka.activities.MainActivity;
import com.example.bakalarka.data.notifications.Notification;
import com.example.bakalarka.data.risks.Conditions;
import com.example.bakalarka.data.room.events.Event;
import com.example.bakalarka.data.room.patient.Patient;
import com.example.bakalarka.data.sensor.GasensSensor;
import com.example.bakalarka.data.sensor.MqttGaSensClient;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
    Izba
        Parametre:
            int id - id izby

            float temperature   - teplota izby
            float humidity      - vlhkosť vzduchu v izbe
            float bedPressure   - tlak na posteli

            MqttGaSensClient client - klient pripojený k serveru (neskôr ho nahradí GasensSensor)
 */
public class Room {

    private final int id;
    private String roomName;
    private RoomData roomData;
    @NonNull
    private final RoomDataRecords roomDataRecords;
    private final Patient patient;
    private int riskLevel;
    @NonNull
    private final GasensSensor gasensSensor;
    @NonNull
    private final Notification notification;

    final MqttGaSensClient mqttClient;

    private Map<String, Conditions> conditionsMap;
    private final List<Event> events;

    public Room(int id, Patient patient) {
        this.id = id;
        this.roomData = new RoomData();
        this.roomDataRecords = new RoomDataRecords();
        this.roomName = "Izba " + id;
        this.patient = patient;
        this.gasensSensor = new GasensSensor(id);
        this.mqttClient = new MqttGaSensClient(this.gasensSensor, MainActivity.context);
        //this.mqttClient = null;
        this.notification = new Notification(id, this.roomName);
        //this.notification = null;
        this.events = new ArrayList<>();

        try {
            this.mqttClient.startMqtt();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public Room(int id, String roomName, Patient patient) {
        this.id = id;
        this.roomData = new RoomData();
        this.roomDataRecords = new RoomDataRecords();
        this.roomName = roomName;
        this.patient = patient;
        this.gasensSensor = new GasensSensor(id);
        this.mqttClient = new MqttGaSensClient(this.gasensSensor, MainActivity.context);
        this.notification = new Notification(id, this.roomName);
        this.events = new ArrayList<>();

        try {
            this.mqttClient.startMqtt();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public Room(int id, String roomName, Patient patient, Map<String, Conditions> conditionsMap, List<Event> events) {
        this.id = id;
        this.roomData = new RoomData();
        this.roomDataRecords = new RoomDataRecords();
        this.roomName = roomName;
        this.patient = patient;
        this.conditionsMap = conditionsMap;
        this.gasensSensor = new GasensSensor(id);
        this.mqttClient = new MqttGaSensClient(this.gasensSensor, MainActivity.context);
        this.notification = new Notification(id, this.roomName);
        this.events = events;

        try {
            this.mqttClient.startMqtt();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    // Setters
    public void setRiskLevel(int riskLevel) {
        this.riskLevel = riskLevel;
    }

    public void setRoomData(RoomData roomData) {
        this.roomData = roomData;
    }

    public void setConditionsMap(Map<String, Conditions> conditionsMap) {
        this.conditionsMap = conditionsMap;
    }

    @NonNull
    public MqttGaSensClient getMqttClient() {
        return mqttClient;
    }

    // Getters
    public int getRiskLevel() {
        return riskLevel;
    }

    public RoomData getRoomData() {
        return roomData;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public RoomDataRecords getRoomDataRecords() {
        return roomDataRecords;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Patient getPatient() {
        return patient;
    }

    public Map<String, Conditions> getConditionsMap() {
        return conditionsMap;
    }

    @NonNull
    public Notification getNotification() {
        return notification;
    }

    public List<Event> getEvents() {
        return events;
    }
}
