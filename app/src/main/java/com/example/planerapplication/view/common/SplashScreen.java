package com.example.planerapplication.view.common;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.planerapplication.R;
import com.example.planerapplication.databinding.SplashScreenBinding;
import com.example.planerapplication.helper.LocaleHelper;
import com.example.planerapplication.view.MainActivity;
import com.example.planerapplication.viewmodel.SplashScreenViewModel;

public class SplashScreen extends AppCompatActivity {

    private static final int SPLASH_TIMER = 3000;

    SplashScreenBinding binding;

    private SplashScreenViewModel splashScreenViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN );
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = this.getTheme();
        theme.resolveAttribute(com.google.android.material.R.attr.colorPrimaryVariant, typedValue, true);
        @ColorInt int color = typedValue.data;
        getWindow().setNavigationBarColor(color);

        /*binding = SplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());*/

        binding = DataBindingUtil.setContentView(this, R.layout.splash_screen);

        String language = LocaleHelper.getLanguage(this);
        LocaleHelper.setLocale(this,language);


        //Animation
        Animation sideAnim = AnimationUtils.loadAnimation(this, R.anim.side_animation);
        Animation bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        binding.splashBackground.setAnimation(sideAnim);
        binding.author.setAnimation(bottomAnim);

        if(!splashScreenViewModel.getPrefsTutorial().getString("first", "").equals("NO")){
            splashScreenViewModel.setPrefsTutorial();

            new Handler().postDelayed(() -> {
                Intent intent = new Intent(SplashScreen.this, TutorialActivity.class);
                startActivity(intent);
                finish();
            },SPLASH_TIMER);
        } else{
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            },SPLASH_TIMER);
        }
    }

    /**
     *  Metoda za izbor theme
     * @return  parametar tipa Resources.Theme, izabrana tema
     */
    @Override
    public Resources.Theme getTheme() {
        Resources.Theme theme = super.getTheme();
        splashScreenViewModel = new ViewModelProvider(this).get(SplashScreenViewModel.class);

        switch (splashScreenViewModel.getPrefsTheme().getString("theme", "")){
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