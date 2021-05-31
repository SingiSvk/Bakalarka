package com.example.bakalarka.data;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.example.bakalarka.R;
import com.example.bakalarka.activities.MainActivity;
import com.example.bakalarka.data.notifications.NotificationController;
import com.example.bakalarka.data.risks.ConditionsController;
import com.example.bakalarka.data.room.Room;
import com.example.bakalarka.data.room.RoomController;
import com.example.bakalarka.data.room.RoomData;
import com.example.bakalarka.data.room.events.Event;
import com.example.bakalarka.data.room.events.EventController;
import com.example.bakalarka.data.wear.WearController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {

    private static final int NOTIF_ID = 1;
    private static final String NOTIF_CHANNEL_ID = "Channel_Id";

    RoomController roomController;
    WearController wearController;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        int roomId = intent.getIntExtra("roomId", 0);

        this.roomController = new RoomController();
        this.wearController = new WearController();

        Room room = roomController.getRoomById(roomId);

        roomController.addRoom(room);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {

                if (!roomController.containsRoom(room.getId())){
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


        startForeground();

        return super.onStartCommand(intent, flags, startId);
    }

    private void startForeground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "foreground";
            String description = "foreground";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIF_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        Intent notificationIntent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        startForeground(NOTIF_ID, new NotificationCompat.Builder(this,
                NOTIF_CHANNEL_ID) // don't forget create a notification channel first
                .setOngoing(true)
                .setSmallIcon(R.drawable.add_circle)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Service is running background")
                .setContentIntent(pendingIntent)
                .build());
    }
}
