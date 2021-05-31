package com.example.bakalarka.activities.addRoom;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bakalarka.R;
import com.example.bakalarka.activities.overview.OverviewAllRoomsActivity;
import com.example.bakalarka.data.MyService;
import com.example.bakalarka.data.room.Room;
import com.example.bakalarka.data.room.RoomController;
import com.example.bakalarka.data.room.patient.Patient;
import com.example.bakalarka.data.sensor.GasensSensor;
import com.example.bakalarka.database.RoomDatabaseController;

public class AddRoomFormActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room_form);

        int roomId = getIntent().getIntExtra("roomId", 0);
        findViewById(R.id.add_room).setOnClickListener(v -> {
            createRoom(roomId);
            Intent intent = new Intent(getApplicationContext(), OverviewAllRoomsActivity.class);
            startActivity(intent);
            finish();
        });
        findViewById(R.id.cancel_button).setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), OverviewAllRoomsActivity.class);
            startActivity(intent);
            finish();
        });
    }

    public void createRoom(int roomId){
        EditText patientNameEditText = findViewById(R.id.add_room_patient_name);
        EditText patientAgeEditText = findViewById(R.id.add_room_patient_age);
        EditText roomNameEditText = findViewById(R.id.add_room_room_name);

        RoomController roomController = new RoomController();
        GasensSensor gasensSensor = new GasensSensor(roomId /*url.getText().toString(), port.getText().toString(), username.getText().toString(), password.getText().toString()*/);

        String patientName = patientNameEditText.getText().toString();

        int patientAge;
        try{
            patientAge = Integer.parseInt(patientAgeEditText.getText().toString());
        } catch(NumberFormatException ex){ // handle your exception
            patientAge = 0;
        }

        String roomName = roomNameEditText.getText().toString();

        Patient patient = createPatient(patientName, patientAge);
        Room room = createRoom(gasensSensor, roomName, patient);
/*
        Intent intent = new Intent(this, MyService.class);
        intent.putExtra("roomId", room.getId());
        startService(intent);
*/
        roomController.addRoom(room);

        new RoomDatabaseController().saveWholeRoomInDB(roomController.getRoomById(roomId));
    }

    public Room createRoom(GasensSensor gasensSensor, String roomName, Patient patient){
        if (!roomName.equals("")){
            return new Room(gasensSensor.getId(), roomName, patient);
        }else {
            return new Room(gasensSensor.getId(), patient);
        }
    }

    public Patient createPatient(String patientName, int patientAge){
        if (!patientName.equals("") && patientAge > 0){
            return new Patient(patientName, patientAge);
        }else if (!patientName.equals("")){
            return new Patient(patientName);
        }else if (patientAge > 0){
            return new Patient(patientAge);
        }else{
            return new Patient();
        }
    }
}
