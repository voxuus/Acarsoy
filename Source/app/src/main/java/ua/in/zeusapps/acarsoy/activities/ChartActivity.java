package ua.in.zeusapps.acarsoy.activities;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import ua.in.zeusapps.acarsoy.R;
import ua.in.zeusapps.acarsoy.common.Const;
import ua.in.zeusapps.acarsoy.common.IAsyncCommand;
import ua.in.zeusapps.acarsoy.models.PlantProductivity;
import ua.in.zeusapps.acarsoy.services.AcarsoyService;
import ua.in.zeusapps.acarsoy.services.FakePlantService;
import ua.in.zeusapps.acarsoy.services.IPlantService;
import ua.in.zeusapps.acarsoy.services.api.TrendlerResponse;

public class ChartActivity extends BaseNavActivity {

    private final IPlantService _plantService = new FakePlantService();

    @BindView(R.id.activity_chart_chart)
    LineChart _chart;

    @BindView(R.id.activity_chart_by_day)
    Button mBtnByDay;

    @BindView(R.id.activity_chart_by_week)
    Button mBtnByWeek;

    @BindView(R.id.activity_chart_by_month)
    Button mBtnByMonth;

    @BindView(R.id.activity_chart_by_year)
    Button mBtnByYear;

    @BindView(R.id.activity_chart_txt_date)
    TextView mTxtDate;

    @BindView(R.id.activity_chart_txt_period)
    TextView mTxtPeriod;

    private String mPlantName;

    private AcarsoyService mAcarsoyService;

    private PeriodEnum mPeriod;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chart;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initServices();
        mPlantName = getIntent().getStringExtra(Const.EXTRA_PLANT_NAME);
        onClickByYear();
    }

    private void initServices() {
        mAcarsoyService = new AcarsoyService();
    }

    @Override
    protected int getCheckedNavId() {
        return 0;
    }

    @OnClick(R.id.activity_chart_by_day)
    void onClickByDay() {
        mPeriod = PeriodEnum.Day;
        refresh();
    }

    @OnClick(R.id.activity_chart_by_week)
    void onClickByWeek() {
        mPeriod = PeriodEnum.Week;
        refresh();
    }

    @OnClick(R.id.activity_chart_by_month)
    void onClickByMonth() {
        mPeriod = PeriodEnum.Month;
        refresh();
    }

    @OnClick(R.id.activity_chart_by_year)
    void onClickByYear() {
        mPeriod = PeriodEnum.Year;
        refresh();
    }

    private void refresh() {
        mBtnByDay.setTextColor(ContextCompat.getColor(this, R.color.colorGray500));
        mBtnByWeek.setTextColor(ContextCompat.getColor(this, R.color.colorGray500));
        mBtnByMonth.setTextColor(ContextCompat.getColor(this, R.color.colorGray500));
        mBtnByYear.setTextColor(ContextCompat.getColor(this, R.color.colorGray500));

        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");

        switch (mPeriod) {
            case Day:
                mBtnByDay.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                mTxtPeriod.setText(mBtnByDay.getText());
                mTxtDate.setText(df.format(Calendar.getInstance().getTime()));
                break;
            case Week:
                mBtnByWeek.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));

                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.clear(Calendar.MINUTE);
                cal.clear(Calendar.SECOND);
                cal.clear(Calendar.MILLISECOND);
                cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());

                mTxtDate.setText(df.format(cal.getTime()) + " - " + df.format(Calendar.getInstance().getTime()));
                mTxtPeriod.setText(mBtnByWeek.getText());
                break;
            case Month:
                mBtnByMonth.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                mTxtPeriod.setText(mBtnByMonth.getText());
                break;
            case Year:
                mBtnByYear.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                mTxtPeriod.setText(mBtnByYear.getText());
                break;
        }

        loadDataAsync();
    }

    private void loadDataAsync() {

        mAcarsoyService.getTrendlerAsync(new IAsyncCommand<String, TrendlerResponse>() {
            @Override
            public void onComplete(TrendlerResponse data) {
                Object obj = data;
            }

            @Override
            public void onError(String error) {
                Toast.makeText(ChartActivity.this, error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public String getParameters() {
                return mPlantName;
            }
        });


        //TODO remove fake data
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

        LineDataSet coalDataSet = new LineDataSet(coalEntries, Const.ENERGY_TYPE_COAL);
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

    enum PeriodEnum {
        Day, Week, Month, Year
    }
}
