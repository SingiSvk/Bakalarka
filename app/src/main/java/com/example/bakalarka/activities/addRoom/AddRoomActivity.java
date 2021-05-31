package com.example.bakalarka.activities.addRoom;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bakalarka.R;
import com.example.bakalarka.activities.dialogs.ExistingRoomDialog;
import com.example.bakalarka.data.room.RoomController;

public class AddRoomActivity extends AppCompatActivity {

    Button submitButton, scanButton;
    EditText id/*url, port, username, password*/;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);


        submitButton = findViewById(R.id.submit);
        id = findViewById(R.id.add_room_gasens_id);
        /*
        url = findViewById(R.id.url);
        port = findViewById(R.id.port);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
         */

        scanButton = findViewById(R.id.scan_btn);

        submitButton.setOnClickListener(v -> {
            int roomId = Integer.parseInt(id.getText().toString());
            if(new RoomController().containsRoom(roomId)){
                ExistingRoomDialog existingRoomDialog = ExistingRoomDialog.newInstance(roomId);
                existingRoomDialog.show(getSupportFragmentManager(), "Existing room");
            }else{
                Intent intent = new Intent(getApplicationContext(), AddRoomFormActivity.class);
                intent.putExtra("roomId", roomId);
                startActivity(intent);
                finish();
            }
        });

        scanButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), QRCodeScanner.class);
            startActivity(intent);
            finish();
        });
    }
}
