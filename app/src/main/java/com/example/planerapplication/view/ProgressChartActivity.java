package com.example.planerapplication.view;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.planerapplication.R;
import com.example.planerapplication.databinding.ActivityProgressChartBinding;
import com.example.planerapplication.viewmodel.ProgressViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProgressChartActivity extends AppCompatActivity {
    private static final String PREFS_THEME = "THEME";

    ActivityProgressChartBinding binding;
    private ProgressViewModel progressViewModel;


    private PieChart mPieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //binding = ActivityProgressChartBinding.inflate(getLayoutInflater());
        binding = DataBindingUtil.setContentView(this, R.layout.activity_progress_chart);
        progressViewModel.setTodayDate();
        binding.setLifecycleOwner(this);
        binding.setViewModel(progressViewModel);
        setContentView(binding.getRoot());


        setSupportActionBar(binding.toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = this.getTheme();
        theme.resolveAttribute(com.google.android.material.R.attr.colorPrimaryVariant, typedValue, true);
        @ColorInt int color = typedValue.data;
        getWindow().setNavigationBarColor(color);

        progressViewModel.getToday().observe(this, today -> {
            setAllPies(today);
        });

        observeTimePickerDialogData(progressViewModel);
    }


    private void observeTimePickerDialogData(ProgressViewModel viewModel) {
        viewModel.getTimePickerDialogData().observe(this, display -> {
            if(display) setTimePickerDialog(); // Display TimePickerDialog
        });
    }

    private void setTimePickerDialog() {
        int mYear, mMonth, mDay;
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth= c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            Calendar c1 = Calendar.getInstance();
            c1.clear();
            c1.set(year,(month),dayOfMonth);

            Date myDate = new Date();
            myDate.setTime(c1.getTimeInMillis());

            progressViewModel.setToday(myDate);

            setAllPies(myDate);
        },mYear,mMonth,mDay);

        datePickerDialog.show();
    }

    /**
     * Metoda za uredjivanje izgleda PieChart-a
     */
    private void setupPieChart(){
        mPieChart.setDrawHoleEnabled(false);
        mPieChart.setUsePercentValues(true);
        mPieChart.setEntryLabelTextSize(15f);
        mPieChart.setEntryLabelColor(Color.BLACK);
        mPieChart.setCenterTextSize(20f);
        mPieChart.getDescription().setEnabled(false);

        Legend l = mPieChart.getLegend();
        l.setEnabled(false);
    }

    /**
     * Metoda za popunjaavanje PieChart-a sa podacima
     * @param all , niz float, ciji prvi element niza je done,
     *            drugi element niza je notDone,
     *            treci element niza je cancle
     */
    private void loadPieChartData(float[] all){
        float done = all[0], notDone = all[1], canceled = all[2];
        ArrayList<PieEntry> entries = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();
        if(done>0){
            entries.add(new PieEntry(done));
            colors.add(getResources().getColor(R.color.done));//DONE
        }
        if (notDone>0){
            entries.add(new PieEntry(notDone));
            colors.add(getResources().getColor(R.color.active));//NOT DONE, ACTIVE
        }
        if(canceled>0){
            entries.add(new PieEntry(canceled));
            colors.add(getResources().getColor(R.color.canceled));//CANCELED
        }

        if(done == 0 && notDone == 0 && canceled == 0){
            entries.add(new PieEntry(1f));
            colors.add(Color.GRAY);//CANCELED
        }

        // ... Ostatak Koda

        PieDataSet dataSet = new PieDataSet(entries, "MY GRAPHS");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(mPieChart));
        data.setValueTextSize(15f);
        data.setValueTextColor(Color.BLACK);

        mPieChart.setData(data);
        mPieChart.invalidate();
    }



    /**
     * Metoda za popunjaavanje PieChart-a sa podacima
     * @param date parametar tipa Date, izabrani datum
     */
    private void setAllPies(Date date){
        progressViewModel.getWeekDates(date);
        DateFormat formatMonth = new SimpleDateFormat("MM"), formatYear = new SimpleDateFormat("yyyy");

        progressViewModel.getDailyTasks(date.getTime(), "Task").observe(this, tasksList -> {
            mPieChart = binding.todayPieChart;
            setupPieChart();
            loadPieChartData(progressViewModel.countStatus(tasksList));
        });

        progressViewModel.getFirstWeekDay().observe(this,
                firstDay -> progressViewModel.getLastWeekDay().observe(this,
                        lastDay -> progressViewModel.getWeeklyTasks(firstDay,lastDay,"Task").observe(this, tasksList -> {
                            mPieChart = binding.weekPieChart;
            setupPieChart();
            loadPieChartData(progressViewModel.countStatus(tasksList));
        })));


        progressViewModel.getMonthlyTasks(formatMonth.format(date),formatYear.format(date)).observe(this, tasksList -> {
            mPieChart = binding.monthPieChart;
            setupPieChart();
            loadPieChartData(progressViewModel.countStatus(tasksList));
        });

        progressViewModel.getYearTasks(formatYear.format(date)).observe(this, tasksList -> {
            mPieChart = binding.yearPieChart;
            setupPieChart();
            loadPieChartData(progressViewModel.countStatus(tasksList));
        });
    }

    /**
     * Metoda za postavljanje izabrane teme
     * @return  parametar tipa Resources.Theme, izabrana tema
     */
    @Override
    public Resources.Theme getTheme() {
        Resources.Theme theme = super.getTheme();
        progressViewModel = new ViewModelProvider(this).get(ProgressViewModel.class);

        SharedPreferences prefs = getSharedPreferences(PREFS_THEME, MODE_PRIVATE);
        String themeName = prefs.getString("theme", "");

        switch (progressViewModel.getPrefsTheme().getString("theme", "")){
            case "Red":
                theme.applyStyle(R.style.Theme_PlanerApplication,true);
                break;
            case "Purple":
                theme.applyStyle(R.style.Theme2,true);
                break;
            case "Yellow":
                theme.applyStyle(R.style.Theme3,true);
                break;
        }
        return super.getTheme();
    }
}