package com.example.bakalarka.activities.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.bakalarka.R;
import com.example.bakalarka.activities.addRoom.AddRoomFormActivity;
import com.example.bakalarka.data.room.RoomController;

import org.jetbrains.annotations.NotNull;

public class ExistingRoomDialog extends DialogFragment {
    @NonNull
    public static ExistingRoomDialog newInstance(int roomId) {
        ExistingRoomDialog fragment = new ExistingRoomDialog();

        Bundle bundle = new Bundle();
        bundle.putInt("roomId", roomId);
        fragment.setArguments(bundle);

        return fragment;
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        int roomId = getArguments().getInt("roomId");
        builder.setMessage(R.string.existing_room)
                .setPositiveButton(R.string.Nahradit, (dialog, id) -> {
                    new RoomController().removeRoom(roomId);

                    Intent intent = new Intent(getActivity().getApplicationContext(), AddRoomFormActivity.class);
                    intent.putExtra("roomId", roomId);
                    startActivity(intent);
                    getActivity().finish();
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> {
                    // User cancelled the dialog
                });
        return builder.create();
    }
}
