package com.example.planerapplication.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class SplashScreenViewModel extends AndroidViewModel {
    private static final String PREFS_FIRST = "FIRST_OPEN";
    private static final String PREFS_THEME = "THEME";

    SharedPreferences prefsTutorial = getApplication().getSharedPreferences(PREFS_FIRST, Context.MODE_PRIVATE);
    SharedPreferences prefsTheme = getApplication().getSharedPreferences(PREFS_THEME, Context.MODE_PRIVATE);

    public SplashScreenViewModel(@NonNull Application application) {
        super(application);
    }

    public SharedPreferences getPrefsTutorial() {
        return prefsTutorial;
    }

    public void setPrefsTutorial() {
        SharedPreferences.Editor editor = getApplication().getSharedPreferences(PREFS_FIRST, Context.MODE_PRIVATE).edit();
        editor.putString("first", "NO");
        editor.apply();
    }

    public SharedPreferences getPrefsTheme() {
        return prefsTheme;
    }

}
