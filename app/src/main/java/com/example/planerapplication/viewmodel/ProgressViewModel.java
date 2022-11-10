package com.example.planerapplication.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.planerapplication.model.PlannerRepository;
import com.example.planerapplication.model.PlannerRepositoryImpl;
import com.example.planerapplication.model.room.entity.Tasks;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ProgressViewModel extends AndroidViewModel {
    private static final String PREFS_THEME = "THEME";

    @Inject
    PlannerRepository plannerRepository;

    SharedPreferences prefsTheme = getApplication().getSharedPreferences(PREFS_THEME, Context.MODE_PRIVATE);

    MutableLiveData<Boolean> timePickerDialogData = new MutableLiveData<>();
    MutableLiveData<Date> today = new MutableLiveData<>();
    MutableLiveData<Long> firstWeekDay = new MutableLiveData<>();
    MutableLiveData<Long> lastWeekDay = new MutableLiveData<>();

    @Inject
    public ProgressViewModel(@NonNull Application application) {
        super(application);
    }

    public SharedPreferences getPrefsTheme() {
        return prefsTheme;
    }

    public MutableLiveData<Date> getToday() {
        return today;
    }

    public void setToday(Date today) {
        this.today.setValue(today);
    }

    public MutableLiveData<Long> getFirstWeekDay() {
        return firstWeekDay;
    }


    public MutableLiveData<Long> getLastWeekDay() {
        return lastWeekDay;
    }

    public LiveData<Boolean> getTimePickerDialogData() {
        return timePickerDialogData;
    }


    public LiveData<List<Tasks>> getDailyTasks(long date, String type){
        int typeID = 0;
        if(type.equals("Task")){
            typeID = 1;
        } else if(type.equals("Event")){
            typeID = 2;
        }
        return plannerRepository.getDailyTasksByType(date,typeID);
    }

    public LiveData<List<Tasks>> getWeeklyTasks(long firstDate, long lastDate, String type){
        int typeID = 0;
        if(type.equals("Task")){
            typeID = 1;
        } else if(type.equals("Event")){
            typeID = 2;
        }
        return plannerRepository.getWeeklyTasksByType(firstDate,lastDate,typeID);
    }

    public LiveData<List<Tasks>> getMonthlyTasks(String monty,String year){
        return plannerRepository.getMonthlyTasks(monty, year);
    }

    public LiveData<List<Tasks>> getYearTasks(String year){
        return plannerRepository.getYearTasks(year);
    }

    public void onDisplayTimePickerDialogClick() {
        timePickerDialogData.setValue(true);
    }

    /**
     * Metoda za postavljanje danasnjeg datuma
     */
    public void setTodayDate() {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        String todayDate = format.format(calendar.getTime());
        todayDate += " 00:00:00";
        SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dates;

        try {
            dates = iso8601Format.parse(todayDate);
        } catch (ParseException e) {
            dates  = null;
        }

        if (dates != null) {
            today.setValue(dates);
        }
    }

    /**
     * Metoda za kreiranje postotka za PieChart
     * @param list  parametar tipa List<Tasks>, lista Tasks elemenata
     */
    public float[] countStatus(List<Tasks> list){
        int size, done = 0, notDone = 0,canceled = 0;
        float[] all = new float[]{0.0f,0.0f,0.0f};
        Tasks tasks;

        if(list!=null)
        {
            if(list.size()>0){
                size = list.size();
                for (int i = 0; i < list.size(); i++) {
                    tasks = list.get(i);
                    switch (tasks.getStatusID()){
                        case 1:
                            notDone++;
                            break;
                        case 2:
                            done++;
                            break;
                        case 3:
                            canceled++;
                            break;
                    }
                }
                all = new float[]{
                        Math.round((done*100f)/size),
                        Math.round((notDone*100f)/size),
                        Math.round((canceled*100f)/size)
                };

            }
        }
        return all;
    }

    /**
     * Metoda koja kreira sve datume u zadatoj sedmici
     * @param date  parametar tipa Date, izabrani datum
     */
    public void getWeekDates(Date date){
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTime(date);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        String pom;

        for (int i = 0; i < 2; i++)
        {
            pom = format.format(calendar.getTime());
            pom += " 00:00:00";


            Date pomDate;
            try {
                pomDate = iso8601Format.parse(pom);
            } catch (ParseException e) {
                pomDate  = null;
            }

            if (i == 0) {
                if (pomDate != null) {
                    firstWeekDay.setValue(pomDate.getTime());
                }
                calendar.add(Calendar.DAY_OF_MONTH, 6);
            } else{
                if (pomDate != null) {
                    lastWeekDay.setValue(pomDate.getTime());
                }
            }
        }
    }

}
