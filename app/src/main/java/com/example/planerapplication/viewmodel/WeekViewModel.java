package com.example.planerapplication.viewmodel;

import android.app.Application;

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
public class WeekViewModel extends AndroidViewModel {
    @Inject
    PlannerRepository plannerRepository;
    MutableLiveData<Integer> weekNumber = new MutableLiveData<>();
    MutableLiveData<Integer> nextPreviousWeek = new MutableLiveData<>();
    @Inject
    public WeekViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Integer> getWeekNumber() {
        return weekNumber;
    }


    public MutableLiveData<Integer> getNextPreviousWeek() {
        return nextPreviousWeek;
    }


    public LiveData<List<Tasks>> getWeeklyTasks(long firstDate, long lastDate){
        return plannerRepository.getWeeklyTasks(firstDate,lastDate);
    }

    /**
     * Metoda za pornalazenje niza datuma za odredjenu sedmicu
     * @param week  parametar tipa int, da li je sedmica trenutna (0), prethodna (-1) ili naredna (+1)
     * @return      niz tipa long, niz datuma u sedmici kao milisekunde
     */
    public Date[] getWeekDates(int week, Date[] dates){
        Date[] datesInTheWeek;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();

        //Za prvo poktretanje
        if(dates != null){
            calendar.setTime(dates[0]);
        }

        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        weekNumber.setValue(calendar.get(Calendar.WEEK_OF_YEAR) + week);
        calendar.set(Calendar.WEEK_OF_YEAR, weekNumber.getValue());
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());


        datesInTheWeek = new Date[7];
        String pom;

        for (int i = 0; i < 7; i++)
        {
            pom = format.format(calendar.getTime());
            pom += " 00:00:00";


            Date date = null;
            try {
                date = iso8601Format.parse(pom);
            } catch (ParseException e) {
                datesInTheWeek  = null;
            }

            if (date != null) {
                if (datesInTheWeek != null) {
                    datesInTheWeek[i] = date;
                }
            }
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return datesInTheWeek;
    }


    public void onNextClicked(){
        nextPreviousWeek.setValue(1);
    }

    public void onPreviousClicked(){
        nextPreviousWeek.setValue(-1);
    }
}
