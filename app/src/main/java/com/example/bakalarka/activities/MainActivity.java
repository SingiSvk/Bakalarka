package com.example.bakalarka.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bakalarka.R;
import com.example.bakalarka.data.MyService;
import com.example.bakalarka.data.room.Room;
import com.example.bakalarka.data.room.RoomController;
import com.example.bakalarka.activities.overview.OverviewAllRoomsActivity;
import com.example.bakalarka.activities.overview.OverviewRiskyRoomsActivity;
import com.example.bakalarka.data.room.events.Event;
import com.example.bakalarka.data.room.events.EventController;
import com.example.bakalarka.database.RoomDatabaseController;

import java.text.ParseException;

public class MainActivity extends BaseActivity {

    //@SuppressLint("StaticFieldLeak")
    @SuppressLint("StaticFieldLeak")
    public static Context context;
    private static boolean start = true;
    public static final int REQUEST_EXIT = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EXIT) {
            if (resultCode == RESULT_OK) {
                this.finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        ImageView allRoomsBtn = findViewById(R.id.all_rooms_btn);
        ImageView riskyRoomsBtn = findViewById(R.id.risky_rooms_btn);

        MainActivity.context = getApplicationContext();

        RoomDatabaseController roomDatabaseController = new RoomDatabaseController();
        RoomController roomController = new RoomController();
        EventController eventController = new EventController();

        System.out.println("------------ Rooms ------------");
        roomDatabaseController.listRooms();
        System.out.println("----------- Persons -----------");
        roomDatabaseController.listPersons();
        System.out.println("---------- Illnesses ----------");
        roomDatabaseController.listIllnesses();
        System.out.println("--------- Conditions ----------");
        roomDatabaseController.listConditions();
        System.out.println("----------- Events ------------");
        roomDatabaseController.listEvents();

        if(MainActivity.start){
            MainActivity.start = false;
            roomController.addRooms(roomDatabaseController.getRoomsFromDB());
            /*for (Room room: roomDatabaseController.getRoomsFromDB()){
                Intent intent = new Intent(this, MyService.class);
                intent.putExtra("roomId", room.getId());
                startService(intent);
            }*/
        }

        StringBuilder events = new StringBuilder();
        for (Event event: eventController.getAllEventsLast50()){
            try {
                events.append("<font color='black'>").append(roomController.getRoomById(event.getRoomId()).getRoomName()).append("</font>").append(" ").append(eventController.eventToString(event));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        TextView textView = findViewById(R.id.general_events);
        textView.setText(Html.fromHtml(events.toString()));

        allRoomsBtn.setOnClickListener(v -> {
            // Spustenie Prehľadu izieb
            Intent intent = new Intent(getApplicationContext(), OverviewAllRoomsActivity.class);
            startActivityForResult(intent, REQUEST_EXIT);
        });

        riskyRoomsBtn.setOnClickListener(v -> {
            // Spustenie Prehľadu izieb
            Intent intent = new Intent(getApplicationContext(), OverviewRiskyRoomsActivity.class);
            startActivityForResult(intent, REQUEST_EXIT);
        });

    }
}