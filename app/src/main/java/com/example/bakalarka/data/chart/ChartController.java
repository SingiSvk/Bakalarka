package com.example.bakalarka.data.chart;

import android.graphics.Color;
import android.icu.text.DecimalFormat;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.bakalarka.data.risks.ConditionsController;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class ChartController {

    // LineChart
    public void setLineChart(@NonNull LineChart lineChart, List<Float> data){
        lineChart.setData(setLineChartData(data));
        lineChart.animateY(1000);
        lineChart.getAxisRight().setEnabled(false);
        setBasicLineChart(lineChart);
    }

    public void setLimits(LineChart lineChart, float low, float high){
        YAxis leftAxis = lineChart.getAxisLeft();

        LimitLine liH = new LimitLine(high, "Horná hranica")    ;
        liH.setLineColor(Color.RED);
        liH.setLineWidth(2);
        liH.setTextColor(Color.BLACK);
        liH.setTextSize(12f);
        liH.enableDashedLine(10,10,0);

        leftAxis.addLimitLine(liH);
// .. and more styling options

        LimitLine liL = new LimitLine(low, "Spodná hranica");
        liL.setLineColor(Color.RED);
        liL.setLineWidth(2);
        liL.setTextColor(Color.BLACK);
        liL.setTextSize(12f);
        liL.enableDashedLine(10,10,0);

        leftAxis.addLimitLine(liL);
    }

    @NonNull
    public LineData setLineChartData(List<Float> data){
        List<Entry> lineEntries = createLineEntries(data);
        LineDataSet lineDataSet = new LineDataSet(lineEntries, "");
        lineDataSet.setLineWidth(2);
        lineDataSet.setColor(Color.BLACK);
        lineDataSet.setCircleColor(Color.RED);
        lineDataSet.setDrawValues(false);
        lineDataSet.setHighLightColor(Color.BLACK);
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setCircleRadius(4);
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        return new LineData(lineDataSet);
    }

    public void setMinMax(LineChart lineChart, float min, float max){
        lineChart.getAxisLeft().setAxisMaximum(max);
        lineChart.getAxisLeft().setAxisMinimum(min);
    }

    public List<Entry> createLineEntries(List<Float> data){
        List<Entry> lineEntries = new ArrayList<>();
        /*int[] values = {25,74,14,96,54,25,45,35,24,15,36,3,65,5,11,25,64,85,32,42};
        int i = 0;
        for(int value: values){
            lineEntries.add(new Entry(i*2, value ));
            i++;
        }*/
        int i = 0;
        for (float entry: data){
            lineEntries.add(new Entry(2*i, entry));
            i++;
        }
        return lineEntries;
    }

    private void setBasicLineChart(@NonNull LineChart chart){
        chart.setDescription(null);
        chart.getLegend().setEnabled(false);
    }

    // PieChart
    public void setPieChart(@NonNull PieChart pieChart, int riskLevel, float data, @NonNull TextView textView, String unit, float max){
        setText(textView, round(data, 1)+""+unit);
        pieChart.setData(setPieChartData(riskLevel, data, max));
        setBasicPieChart(pieChart);
    }

    private static double round (double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    @NonNull
    public PieData setPieChartData(int riskLevel, float data, float max){
        List<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(data));
        pieEntries.add(new PieEntry(max-data));
        PieDataSet dataSet = new PieDataSet(pieEntries, "");
        String color;
        if (riskLevel == ConditionsController.NONE){
            color = "32CD32";
        }else if(riskLevel == ConditionsController.LOW){
            color = "FF6600";
        }else{
            color = "C00000";
        }
        dataSet.setColors(ColorTemplate.rgb(color), ColorTemplate.rgb("cccccc"));
        dataSet.setDrawValues(false);
        return new PieData(dataSet);
    }


    private void setBasicPieChart(@NonNull PieChart chart){
        chart.setMaxAngle(180);
        chart.setHoleRadius(65);
        chart.setDescription(null);
        chart.getLegend().setEnabled(false);
        chart.setRotationEnabled(false);
        chart.setHighlightPerTapEnabled(false);
    }


    private void setText(@NonNull TextView view, String text){
        view.setText(text);
    }

}
