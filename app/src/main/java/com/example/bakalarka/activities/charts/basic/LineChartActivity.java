package com.example.bakalarka.activities.charts.basic;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bakalarka.R;
import com.example.bakalarka.activities.overview.OverviewAllRoomsActivity;
import com.example.bakalarka.data.chart.ChartController;
import com.example.bakalarka.data.risks.Conditions;
import com.example.bakalarka.data.room.Room;
import com.example.bakalarka.data.room.RoomController;
import com.example.bakalarka.data.room.RoomDataRecords;
import com.github.mikephil.charting.charts.LineChart;

import java.util.List;
import java.util.Map;

// Základná aktivita pre zobrazenie stĺpcového grafu
public abstract class LineChartActivity extends ChartActivity {

    ChartController chartController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);

        this.chartController = new ChartController();
        int roomId = getIntent().getIntExtra("room_id",0);

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), OverviewAllRoomsActivity.class);
            intent.putExtra("page", roomId);
            startActivity(intent);
            finish();
        });

        Room room = new RoomController().getRoomById(roomId);
        RoomDataRecords dataRecords = room.getRoomDataRecords();
        List<Float> data = setData(dataRecords);

        // Graf
        LineChart chart = findViewById(R.id.line_chart);

        setLimits(chart, room.getConditionsMap());
        chartController.setLineChart(chart, data);
    }

    @NonNull
    public abstract List<Float> setData(RoomDataRecords data);

    public abstract void setLimits(LineChart lineChart, Map<String, Conditions> conditionsMap);

/*
    public static LineData setLineChartData(LineChart lineChart, ArrayList<Entry> data, String name){

        LineDataSet lineDataSet = new LineDataSet(data, name);
       /* lineDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        lineDataSet.setValueTextColor(Color.BLACK);
        lineDataSet.setValueTextSize(16f);

        LineData lineData = new LineData(lineDataSet);

        lineChart.setData(lineData);
        lineChart.animateY(1000);

        return lineData;
    }*/
}
