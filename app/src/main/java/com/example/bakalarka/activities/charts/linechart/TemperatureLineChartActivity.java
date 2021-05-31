package com.example.bakalarka.activities.charts.linechart;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.example.bakalarka.data.chart.ChartController;
import com.example.bakalarka.data.risks.Conditions;
import com.example.bakalarka.data.room.RoomDataRecords;
import com.example.bakalarka.activities.charts.basic.LineChartActivity;
import com.github.mikephil.charting.charts.LineChart;

import java.util.List;
import java.util.Map;

public class TemperatureLineChartActivity extends LineChartActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public List<Float> setData(@NonNull RoomDataRecords data) {
        return data.getTemperatureList();
    }

    @Override
    public void setLimits(LineChart lineChart, Map<String, Conditions> conditionsMap) {
        Conditions conditions = conditionsMap.get(Conditions.TEMPERATURE_CONDITIONS);
        ChartController chartController = new ChartController();
        chartController.setLimits(lineChart, conditions.getMin(), conditions.getMax());
        chartController.setMinMax(lineChart, 10, 35);
    }
}