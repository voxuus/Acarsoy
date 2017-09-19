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
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import ua.in.zeusapps.acarsoy.R;
import ua.in.zeusapps.acarsoy.common.Const;
import ua.in.zeusapps.acarsoy.common.ConvertUtils;
import ua.in.zeusapps.acarsoy.common.IAsyncCommand;
import ua.in.zeusapps.acarsoy.services.AcarsoyService;
import ua.in.zeusapps.acarsoy.services.api.PlantResponse;
import ua.in.zeusapps.acarsoy.services.api.TrendlerResponse;

public class ChartActivity extends BaseNavActivity {

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

    private ConvertUtils mConvertUtils;

    private PeriodEnum mPeriod;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chart;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initActionBar();

        initServices();
        mPlantName = getIntent().getStringExtra(Const.EXTRA_PLANT_NAME);
        onClickByYear();
    }

    private void initActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void initServices() {
        mAcarsoyService = new AcarsoyService();
        mConvertUtils = new ConvertUtils(this);
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

    @Override
    public void refresh() {
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
                mTxtPeriod.setText(mBtnByWeek.getText());
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.clear(Calendar.MINUTE);
                cal.clear(Calendar.SECOND);
                cal.clear(Calendar.MILLISECOND);
                cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
                mTxtDate.setText(df.format(cal.getTime()) + " - " + df.format(Calendar.getInstance().getTime()));
                break;
            case Month:
                mBtnByMonth.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                mTxtPeriod.setText(mBtnByMonth.getText());
                cal = Calendar.getInstance();
                cal = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1);
                mTxtDate.setText(df.format(cal.getTime()) + " - " + df.format(Calendar.getInstance().getTime()));
                break;
            case Year:
                mBtnByYear.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                mTxtPeriod.setText(mBtnByYear.getText());
                cal = Calendar.getInstance();
                cal = new GregorianCalendar(cal.get(Calendar.YEAR), 0, 1);
                mTxtDate.setText(df.format(cal.getTime()) + " - " + df.format(Calendar.getInstance().getTime()));
                break;
        }

        loadDataAsync();
    }

    private void loadDataAsync() {

        _chart.clear();

        if (mPlantName == null) {
            mAcarsoyService.getPlantsAsync(new IAsyncCommand<Object, List<PlantResponse>>() {
                @Override
                public void onComplete(List<PlantResponse> data) {
                    for (PlantResponse item : data) {
                        if (item.Type.equals(Const.ENERGY_TYPE_WIND)) {
                            mPlantName = item.Id;
                        }
                    }
                    getTrendlerDataAsync();
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(ChartActivity.this, error, Toast.LENGTH_SHORT).show();
                }

                @Override
                public Object getParameters() {
                    return null;
                }
            });
        } else {
            getTrendlerDataAsync();
        }
    }

    private void getTrendlerDataAsync() {
        mAcarsoyService.getTrendlerAsync(new IAsyncCommand<String, TrendlerResponse>() {
            @Override
            public void onComplete(final TrendlerResponse data) {
                List<Entry> entries = new ArrayList<>();
                float totalPowerDay = 0.0f;
                float totalPowerWeek = 0.0f;
                float totalPowerMonth = 0.0f;
                float totalPowerYear = 0.0f;

                int index = 0;
                for (TrendlerResponse.Turbine turbine : data.Turbines) {

                    totalPowerDay += turbine.PowerToday;
                    totalPowerWeek += turbine.PowerWeek;
                    totalPowerMonth += turbine.PowerMonth;
                    totalPowerYear += turbine.PowerYear;

                    float val = 0;
                    switch (mPeriod) {
                        case Day:
                            val = (float) turbine.PowerToday;
                            break;
                        case Week:
                            val = (float) turbine.PowerWeek;
                            break;
                        case Month:
                            val = (float) turbine.PowerMonth;
                            break;
                        case Year:
                            val = (float) turbine.PowerYear;
                            break;
                    }
                    Entry entry = new Entry(index++, val);
                    entries.add(entry);
                }

                LineDataSet dataSet = new LineDataSet(entries, mPlantName);
                dataSet.setColor(ContextCompat.getColor(ChartActivity.this, android.R.color.holo_green_dark));
                dataSet.setLineWidth(3f);
                LineData line = new LineData(dataSet);

                _chart.setData(line);

                setXAxis(_chart.getXAxis(), new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        return data.Turbines.get((int) value).Name;
                    }
                });
                setYAxis(_chart.getAxisLeft());
                setYAxis(_chart.getAxisRight());

                _chart.notifyDataSetChanged();
                _chart.invalidate();

                mBtnByDay.setText(getString(R.string.bugun) + "\n" + mConvertUtils.getPowerMWatt(totalPowerDay / 1E3));
                mBtnByWeek.setText(getString(R.string.bu_hafta) + "\n" + mConvertUtils.getPowerMWatt(totalPowerWeek / 1E3));
                mBtnByMonth.setText(getString(R.string.bu_ay) + "\n" + mConvertUtils.getPowerMWatt(totalPowerMonth / 1E3));
                mBtnByYear.setText(getString(R.string.bu_yil) + "\n" + mConvertUtils.getPowerMWatt(totalPowerYear / 1E3));
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
    }

    private void setXAxis(XAxis axis, IAxisValueFormatter valueFormatter) {
        axis.setPosition(XAxis.XAxisPosition.BOTTOM);
        axis.setTextSize(10f);
        axis.setDrawAxisLine(true);
        axis.setDrawGridLines(false);
        axis.setValueFormatter(valueFormatter);
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
