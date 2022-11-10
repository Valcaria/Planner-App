package com.example.planerapplication.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.planerapplication.R;
import com.example.planerapplication.databinding.ActivityMainBinding;
import com.example.planerapplication.helper.LocaleHelper;
import com.example.planerapplication.helper.StateAdapter;
import com.example.planerapplication.viewmodel.MainViewModel;
import com.example.planerapplication.viewmodel.ProgressViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity{
    public static final int ADD_NOTE_REQUEST = 1;
    private static final int[] TAB_TITLES = new int[]{R.string.tab_day, R.string.tab_week, R.string.tab_month,R.string.tab_year};

    private MainViewModel mainViewModel;
    private StateAdapter stateAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setMainViewModel(mainViewModel);
        setLanguage();

        stateAdapter = new StateAdapter(this);
        ViewPager2 viewPager = binding.viewPager;
        viewPager.setAdapter(stateAdapter);


        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = this.getTheme();
        theme.resolveAttribute(com.google.android.material.R.attr.colorPrimaryVariant, typedValue, true);
        @ColorInt int color = typedValue.data;
        getWindow().setNavigationBarColor(color);

        setSupportActionBar(binding.toolbar);
        TabLayout tabs = binding.tabs;
        new TabLayoutMediator(tabs, viewPager, (tab, position) -> tab.setText(TAB_TITLES[position])).attach();

        observeFabClicked(mainViewModel);
    }

    /**
     * Metoda za promjenu jezika
     */
    private void setLanguage(){
        String language = LocaleHelper.getLanguage(this);
        LocaleHelper.setLocale(this, language);

        setTitle(R.string.app_name);
    }

    private void observeFabClicked(@NonNull MainViewModel viewModel) {
        viewModel.getFabClicked().observe(this, clicked -> {
            if(clicked) {
                Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
                intent.putExtra("ADD_EDIT",ADD_NOTE_REQUEST);
                startActivity(intent);
            }
        });
    }



    /**
     *  Metoda za izbor theme
     * @return  parametar tipa Resources.Theme, izabrana tema
     */
    @Override
    public Resources.Theme getTheme() {
        Resources.Theme theme = super.getTheme();
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        switch (mainViewModel.getPrefsTheme().getString("theme", "")){
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


    /**
     * Metoda za zamjenu teme ukoliko nije ista kao izabrana
     */
    private void switchTheme (){
        if(mainViewModel.getPrefsIsThemeSet().getBoolean("isThemeSet", false)){
            mainViewModel.setPrefsIsThemeSet();
            recreate();
        }
     }


    @Override
    protected void onResume() {
        super.onResume();
        setLanguage();
        switchTheme();
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_settings){
            Intent settings = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(settings);
            return true;
        } else if(item.getItemId() == R.id.action_progress){
            Intent progress = new Intent(MainActivity.this, ProgressChartActivity.class);
            startActivity(progress);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

}