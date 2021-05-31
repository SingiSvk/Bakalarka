package com.example.bakalarka.data.wear;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.bakalarka.activities.MainActivity;
import com.example.bakalarka.data.risks.Conditions;
import com.example.bakalarka.data.room.RoomController;
import com.example.bakalarka.data.room.Room;
import com.example.bakalarka.data.room.RoomData;
import com.example.bakalarka.data.room.patient.Patient;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class WearController {

    public static final String TAG = "Main Activity";

    public void synchronize() {
        RoomController roomController = new RoomController();
        ArrayList<Integer> ids = roomController.getAllIds();
        sendIds(ids);
        for (Room room: RoomController.rooms){
            sendBasicData(room);
        }
    }

    public void sendIds(ArrayList<Integer> ids){
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/ids");

        putDataMapReq.getDataMap().putIntegerArrayList("ids", ids);

        putDataMapReq.setUrgent();

        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
        Task<DataItem> putDataTask = Wearable.getDataClient(MainActivity.context).putDataItem(putDataReq);

        putDataTask.addOnSuccessListener(dataItem -> Log.d(TAG, "Sending text was successful: " + dataItem + " \n" + putDataMapReq.getDataMap().toString()));
    }

    public void sendConditions(int id, @NonNull Map<String, Conditions> conditionsMap){
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/conditions");

        putDataMapReq.getDataMap().putInt("id", id);

        insertConditionsData(putDataMapReq, conditionsMap);

        putDataMapReq.setUrgent();

        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
        Task<DataItem> putDataTask = Wearable.getDataClient(MainActivity.context).putDataItem(putDataReq);

        putDataTask.addOnSuccessListener(dataItem -> Log.d(TAG, "Sending text was successful: " + dataItem + " \n" + putDataMapReq.getDataMap().toString()));
    }

    public void sendRoomData(int id, @NonNull RoomData roomData){
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/data");

        putDataMapReq.getDataMap().putInt("id", id);
        insertRoomData(putDataMapReq, roomData);

        putDataMapReq.setUrgent();

        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
        Task<DataItem> putDataTask = Wearable.getDataClient(MainActivity.context).putDataItem(putDataReq);

        putDataTask.addOnSuccessListener(dataItem -> Log.d(TAG, "Sending text was successful: " + dataItem + " \n" + putDataMapReq.getDataMap().toString()));
    }

    public void sendRoomName(int id, String roomName){
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/room_name");

        putDataMapReq.getDataMap().putInt("id", id);
        putDataMapReq.getDataMap().putString("room_name", roomName);

        putDataMapReq.setUrgent();

        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
        Task<DataItem> putDataTask = Wearable.getDataClient(MainActivity.context).putDataItem(putDataReq);

        putDataTask.addOnSuccessListener(dataItem -> Log.d(TAG, "Sending text was successful: " + dataItem + " \n" + putDataMapReq.getDataMap().toString()));
    }

    public void sendPatientData(int id, @NonNull Patient patient){
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/patient");

        putDataMapReq.getDataMap().putInt("id", id);
        insertPatientInfo(putDataMapReq, patient);

        putDataMapReq.setUrgent();

        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
        Task<DataItem> putDataTask = Wearable.getDataClient(MainActivity.context).putDataItem(putDataReq);

        putDataTask.addOnSuccessListener(dataItem -> Log.d(TAG, "Sending text was successful: " + dataItem + " \n" + putDataMapReq.getDataMap().toString()));
    }

    public void sendBasicData(@NonNull Room room){
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/basic_"+room.getId());

        putDataMapReq.getDataMap().putLong("time", new Date().getTime());
        putDataMapReq.getDataMap().putInt("id", room.getId());
        putDataMapReq.getDataMap().putString("room_name", room.getRoomName());
        insertConditionsData(putDataMapReq, room.getConditionsMap());
        insertPatientInfo(putDataMapReq, room.getPatient());

        putDataMapReq.setUrgent();

        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
        Task<DataItem> putDataTask = Wearable.getDataClient(MainActivity.context).putDataItem(putDataReq);

        putDataTask.addOnSuccessListener(dataItem -> Log.d(TAG, "Sending text was successful: " + dataItem + " \n" + putDataMapReq.getDataMap().toString()));
    }

    private void insertRoomData(@NonNull PutDataMapRequest putDataMapRequest, @NonNull RoomData roomData){
        putDataMapRequest.getDataMap().putFloat("temperature", roomData.getTemperature());
        putDataMapRequest.getDataMap().putFloat("humidity", roomData.getHumidity());
        putDataMapRequest.getDataMap().putFloat("pressure", roomData.getPressure());
        putDataMapRequest.getDataMap().putFloat("voc", roomData.getVoc());
    }

    private void insertConditionsData(@NonNull PutDataMapRequest putDataMapRequest, @NonNull Map<String, Conditions> conditionsMap){
        for (String key: conditionsMap.keySet()){
            putDataMapRequest.getDataMap().putFloat(key + "_max", conditionsMap.get(key).getMax());
            putDataMapRequest.getDataMap().putFloat(key + "_high", conditionsMap.get(key).getHigh());
            putDataMapRequest.getDataMap().putFloat(key + "_low", conditionsMap.get(key).getLow());
            putDataMapRequest.getDataMap().putFloat(key + "_min", conditionsMap.get(key).getMin());
        }
    }

    private void insertPatientInfo(@NonNull PutDataMapRequest putDataMapRequest, @NonNull Patient patient){
        putDataMapRequest.getDataMap().putString("patient_name", patient.getName());
        putDataMapRequest.getDataMap().putInt("patient_age", patient.getAge());
    }
}
