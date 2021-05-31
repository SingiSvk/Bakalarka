package com.example.bakalarka.activities.overview.basic;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bakalarka.R;
import com.example.bakalarka.activities.addRoom.AddRoomActivity;

public class AddRoomFragment extends Fragment {

    View view;

    @NonNull
    public static AddRoomFragment newInstance(@Nullable Bundle args){
        AddRoomFragment fragment = new AddRoomFragment();
        if(args != null){
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_add_room, container, false);

        view.findViewById(R.id.add_room).setOnClickListener(v -> {
            Intent intent = new Intent(this.getContext(), AddRoomActivity.class);
            startActivity(intent);
        });

        return this.view;
    }

}
