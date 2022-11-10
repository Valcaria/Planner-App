package com.example.planerapplication.view;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.example.planerapplication.R;
import com.example.planerapplication.broadcast.DailyNotification;
import com.example.planerapplication.databinding.SettingsActivityBinding;
import com.example.planerapplication.view.common.TutorialActivity;
import com.example.planerapplication.helper.LocaleHelper;
import com.example.planerapplication.viewmodel.SettingsViewModel;

import java.util.Calendar;

public class SettingsActivity extends AppCompatActivity {

    private SettingsActivityBinding binding;
    private SettingsViewModel settingsViewModel;

    private AlarmManager mAlarmManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = SettingsActivityBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        setTitle(R.string.title_activity_settings);

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }


        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = this.getTheme();
        theme.resolveAttribute(com.google.android.material.R.attr.colorPrimaryVariant, typedValue, true);
        @ColorInt int color = typedValue.data;
        getWindow().setNavigationBarColor(color);

    }

    /**
     * Metoda za kreiranje kanala za notifikacije za DNEVNI PODSJETNIK
     */
    public  void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Daily Reminder";
            String description = "Channel For Daily Reminder";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel =
                    new NotificationChannel("plannedDaily",name,importance);
            channel.setDescription(description);

            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Metoda za postavljanje dnevne notifikacije
     * @param selectedTime      parametar tipa String koji predstavlja izabrano vrijeme za slanje notifikacija
     */
    public void setAlarm(@NonNull String selectedTime){
        createNotificationChannel();

        if(selectedTime.equals("cancel")){
            Intent intent = new Intent(this, DailyNotification.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,intent,0);

            if(mAlarmManager== null){
                mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            }
            mAlarmManager.cancel(pendingIntent);
            pendingIntent.cancel();

        } else{
            Calendar notification = settingsViewModel.setTime(selectedTime);

            mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            Intent intent = new Intent(this, DailyNotification.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,intent,0);
            mAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,notification.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent );
        }

    }

    /**
     * Metoda za postavljanje izabrane teme
     * @param theme     parametar tipa string koji predstavlja naziv teme koja treba da se postavi
     */
    public void setTheme(@NonNull String theme){
        settingsViewModel.setPrefsIsThemeSet();
        switch (theme){
            case "theme_one":
                settingsViewModel.setPrefsTheme("Red");
                break;
            case "theme_two":
                settingsViewModel.setPrefsTheme("Purple");
                break;
            case "theme_three":
                settingsViewModel.setPrefsTheme("Yellow");
                break;
        }
        recreate();
    }


    /**
     * Metoda za pokretanje TotorialActrivity
     */
    public void openTutorial(){
        Intent intent = new Intent(SettingsActivity.this, TutorialActivity.class);
        startActivity(intent);
        finish();
    }



    @Override
    public Resources.Theme getTheme() {
        Resources.Theme theme = super.getTheme();
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        switch (settingsViewModel.getPrefsTheme().getString("theme", "")){
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


    public void setLanguage(String language){
        LocaleHelper.setLocale(this, language);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        private ListPreference mListPreferenceReminder,
        mListPerformanceLanguage, mListPreferenceTheme;
        private SwitchPreference mSwitchPreference;


        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }

        @NonNull
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            mListPreferenceReminder = getPreferenceManager().findPreference("reminder");
            mListPerformanceLanguage =  getPreferenceManager().findPreference("language");
            mListPreferenceTheme =  getPreferenceManager().findPreference("theme");
            mSwitchPreference = getPreferenceManager().findPreference("notification");

            Preference tutorial = getPreferenceManager().findPreference("tutorial");

            mListPreferenceReminder.setEnabled(mSwitchPreference.isChecked());

            //PALJENJE?GASENJE DNEVNE NOTIFIKACIJE
            mSwitchPreference.setOnPreferenceChangeListener((preference, newValue) -> {
               if((boolean) newValue){
                   mListPreferenceReminder.setEnabled(true);
                   ((SettingsActivity)getActivity()).setAlarm(mListPreferenceReminder.getValue());
               }else {
                   mListPreferenceReminder.setEnabled(false);
                   ((SettingsActivity)getActivity()).setAlarm("cancel");
               }
                return true;
            });


            //POSTAVLJANJE VREMENA NOTIFIKACIJE
            mListPreferenceReminder.setOnPreferenceChangeListener((preference, newValue) -> {
                ((SettingsActivity)getActivity()).setAlarm(newValue.toString());
                return true;
            });

            //PROMJENA JEZIKA
            mListPerformanceLanguage.setOnPreferenceChangeListener((preference, newValue) -> {
                ((SettingsActivity)getActivity()).setLanguage(newValue.toString());


                mListPerformanceLanguage.setTitle(R.string.language_title);
                mListPreferenceTheme.setTitle(R.string.theme_title);
                mListPreferenceReminder.setTitle(R.string.reminder_title);
                mSwitchPreference.setSwitchTextOn(R.string.is_disabled);
                mSwitchPreference.setSwitchTextOff(R.string.is_set);


                mSwitchPreference.setTitle(R.string.reminder_title);
                tutorial.setTitle(R.string.tutorial_title);

                PreferenceCategory preferenceCategory =  getPreferenceManager().findPreference("settings");
                preferenceCategory.setTitle(R.string.title_activity_settings);

                return true;
            });

            //PROMJENA TEME
            mListPreferenceTheme.setOnPreferenceChangeListener((preference, newValue) -> {

                ((SettingsActivity)getActivity()).setTheme(newValue.toString());

                return true;
            });


            //POKRETANJE TUTORIJALA
            tutorial.setOnPreferenceClickListener(preference -> {
                ((SettingsActivity)getActivity()).openTutorial();
                return true;
            });

            return super.onCreateView(inflater, container, savedInstanceState);
        }

    }


}