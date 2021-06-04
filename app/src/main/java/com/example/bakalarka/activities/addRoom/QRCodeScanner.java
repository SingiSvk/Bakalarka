package com.example.bakalarka.activities.addRoom;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bakalarka.R;
import com.example.bakalarka.activities.addRoom.AddRoomFormActivity;
import com.example.bakalarka.activities.dialogs.ExistingRoomDialog;
import com.example.bakalarka.activities.addRoom.CaptureActivity;
import com.example.bakalarka.data.room.RoomController;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

// Skenner na QR kód
public class QRCodeScanner extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        scanCode();
    }

    // Nastavenie skenera
    private void scanCode(){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureActivity.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning Code");

        // spustenie skenera
        integrator.initiateScan();
    }

    // Po naskenovaní
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Dekódovanie
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        // Ak sme niečo naskenovali a máme nejaký výstup zobrazí sa dekódovaná správa
        if(result != null){
            if (result.getContents() != null){
                String[] scannerData = result.getContents().split("=");
                int roomId = Integer.parseInt(scannerData[1]);

                Intent intent = new Intent(getApplicationContext(), AddRoomFormActivity.class);
                intent.putExtra("roomId", roomId);
                startActivity(intent);
                finish();

            }else{
                Toast.makeText(this, "No Result", Toast.LENGTH_LONG).show();
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
