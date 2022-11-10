package com.example.planerapplication.viewmodel;

import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.planerapplication.R;

import java.util.Calendar;
import java.util.Date;

public class YearViewModel extends ViewModel {
    MutableLiveData<Integer> year = new MutableLiveData<>();
    MutableLiveData<Date> date = new MutableLiveData<>();

    public MutableLiveData<Integer> getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year.setValue(year);
    }

    public MutableLiveData<Date> getDate() {
        return date;
    }

    public void onMonthClick(View view) {
        Calendar c = Calendar.getInstance();
        int month = 0, day = c.get(Calendar.DAY_OF_MONTH);
        if(view.getId() == R.id.btnJan){
            month = 1;
        } else if(view.getId() == R.id.btnFeb){
            month = 2;
        } else if(view.getId() == R.id.btnMart){
            month = 3;
        } else if(view.getId() == R.id.btnApr){
            month = 4;
        } else if(view.getId() == R.id.btnMay){
            month = 5;
        } else if(view.getId() == R.id.btnJune){
            month = 6;
        } else if(view.getId() == R.id.btnJuly){
            month = 7;
        } else if(view.getId() == R.id.btnAvg){
            month = 8;
        } else if(view.getId() == R.id.btnSep){
            month = 9;
        } else if(view.getId() == R.id.btnOct){
            month = 10;
        } else if(view.getId() == R.id.btnNov){
            month = 11;
        } else if(view.getId() == R.id.btnDec){
            month = 12;
        }

        c.clear();
        c.set(year.getValue(),(month-1),day);
        Date temp = new Date();
        temp.setTime(c.getTimeInMillis());

        date.setValue(temp);

    }

    public void onNextClicked(){
        int pom = year.getValue();
        pom++;
        year.setValue(pom);
    }

    public void onPreviousClicked(){
        int pom = year.getValue();
        pom--;
        year.setValue(pom);
    }

}
