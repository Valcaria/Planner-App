package com.example.planerapplication.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import java.util.Calendar;
import java.util.Date;

public class SettingsViewModel extends AndroidViewModel {
    private static final String PREFS_THEME = "THEME";
    private static final String PREFS_SET = "IS_THEME_SET";

    SharedPreferences prefsIsThemeSet = getApplication().getSharedPreferences(PREFS_SET, Context.MODE_PRIVATE);
    SharedPreferences prefsTheme = getApplication().getSharedPreferences(PREFS_THEME, Context.MODE_PRIVATE);

    public SettingsViewModel(@NonNull Application application) {
        super(application);
    }

    public SharedPreferences getPrefsIsThemeSet() {
        return prefsIsThemeSet;
    }

    public void setPrefsIsThemeSet() {
        SharedPreferences.Editor isThemeSet = getApplication().getSharedPreferences(PREFS_SET, Context.MODE_PRIVATE).edit();
        isThemeSet.putBoolean("isThemeSet", true);
        isThemeSet.apply();
    }

    public SharedPreferences getPrefsTheme() {
        return prefsTheme;
    }

    public void setPrefsTheme(String theme) {
        SharedPreferences.Editor editor = getApplication().getSharedPreferences(PREFS_THEME, Context.MODE_PRIVATE).edit();
        editor.putString("theme", theme);
        editor.apply();
    }

    public Calendar setTime(String selectedTime) {
        Date date = new Date();

        Calendar notification = Calendar.getInstance();
        notification.setTime(date);
        switch (selectedTime) {
            case "reminder_one":
                notification.set(Calendar.HOUR_OF_DAY, 19);
                notification.set(Calendar.MINUTE, 0);
                notification.set(Calendar.SECOND, 0);
                break;
            case "reminder_two":
                notification.set(Calendar.HOUR_OF_DAY, 19);
                notification.set(Calendar.MINUTE, 30);
                notification.set(Calendar.SECOND, 0);
                break;
            case "reminder_three":
                notification.set(Calendar.HOUR_OF_DAY, 20);
                notification.set(Calendar.MINUTE, 0);
                notification.set(Calendar.SECOND, 0);
                break;
            case "reminder_four":
                notification.set(Calendar.HOUR_OF_DAY, 20);
                notification.set(Calendar.MINUTE, 30);
                notification.set(Calendar.SECOND, 0);
                break;
            case "reminder_five":
                notification.set(Calendar.HOUR_OF_DAY, 21);
                notification.set(Calendar.MINUTE, 0);
                notification.set(Calendar.SECOND, 0);
                break;
            case "reminder_six":
                notification.set(Calendar.HOUR_OF_DAY, 21);
                notification.set(Calendar.MINUTE, 30);
                notification.set(Calendar.SECOND, 0);
                break;
            case "reminder_seven":
                notification.set(Calendar.HOUR_OF_DAY, 22);
                notification.set(Calendar.MINUTE, 0);
                notification.set(Calendar.SECOND, 0);
                break;
            case "reminder_eight":
                notification.set(Calendar.HOUR_OF_DAY, 22);
                notification.set(Calendar.MINUTE, 30);
                notification.set(Calendar.SECOND, 0);
                break;
        }

        return notification;
    }
}
