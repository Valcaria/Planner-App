package com.example.planerapplication.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class TutorialViewModel extends AndroidViewModel {
    private static final String PREFS_THEME = "THEME";

    SharedPreferences prefsTheme = getApplication().getSharedPreferences(PREFS_THEME, Context.MODE_PRIVATE);

    public TutorialViewModel(@NonNull Application application) {
        super(application);
    }

    public SharedPreferences getPrefsTheme() {
        return prefsTheme;
    }
}
