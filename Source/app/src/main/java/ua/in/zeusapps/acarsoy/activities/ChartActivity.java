package ua.in.zeusapps.acarsoy.activities;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ua.in.zeusapps.acarsoy.R;
import ua.in.zeusapps.acarsoy.common.Const;
import ua.in.zeusapps.acarsoy.models.PlantProductivity;
import ua.in.zeusapps.acarsoy.services.FakePlantService;
import ua.in.zeusapps.acarsoy.services.IPlantService;

public class ChartActivity extends AppCompatActivity {

    private final IPlantService _plantService = new FakePlantService();
    @BindView(R.id.activity_chart_chart)
    LineChart _chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        ButterKnife.bind(this);

        populateChart();
    }

    private void populateChart() {
        List<PlantProductivity> productivities = _plantService.getPlantsProductivityByHours();

        List<Entry> coalEntries = new ArrayList<>();
        List<Entry> windEntries = new ArrayList<>();

        for (PlantProductivity productivity : productivities) {
            Entry entry = new Entry(productivity.getHour(), (float) productivity.getPower());
            if (productivity.getPlantType().equals(Const.ENERGY_TYPE_COAL)) {
                coalEntries.add(entry);
            } else {
                windEntries.add(entry);
            }
        }

        LineDataSet coalDataSet = new LineDataSet(coalEntries,Const.ENERGY_TYPE_COAL);
        coalDataSet.setColor(ContextCompat.getColor(this, android.R.color.holo_green_dark));
        coalDataSet.setLineWidth(3f);
        LineDataSet windDataSet = new LineDataSet(windEntries, Const.ENERGY_TYPE_WIND);
        windDataSet.setColor(ContextCompat.getColor(this, android.R.color.holo_blue_dark));
        windDataSet.setLineWidth(3f);
        LineData line = new LineData(coalDataSet, windDataSet);

        _chart.setData(line);

        setXAxis(_chart.getXAxis());
        setYAxis(_chart.getAxisLeft());
        setYAxis(_chart.getAxisRight());
    }

    private void setXAxis(XAxis axis) {
        axis.setPosition(XAxis.XAxisPosition.BOTTOM);
        axis.setTextSize(10f);
        axis.setDrawAxisLine(true);
        axis.setDrawGridLines(false);

        axis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return (int) value + ":00";
            }
        });
    }

    private void setYAxis(YAxis axis) {
        axis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axis.setTextSize(10f);
        axis.setDrawAxisLine(true);
        axis.setDrawGridLines(true);
    }
}
