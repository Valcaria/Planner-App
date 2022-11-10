package com.example.planerapplication.view.common;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.planerapplication.databinding.ActivityTutorialBinding;
import com.example.planerapplication.view.MainActivity;
import com.example.planerapplication.R;
import com.example.planerapplication.helper.SliderAdapter;
import com.example.planerapplication.viewmodel.TutorialViewModel;


public class TutorialActivity extends AppCompatActivity {

    private ActivityTutorialBinding binding;

    private TutorialViewModel tutorialViewModel;


    private int mCurrentPos;

    private TextView[] mDots;
    private Animation mAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityTutorialBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SliderAdapter sliderAdapter = new SliderAdapter(this);
        binding.tutorial.setAdapter(sliderAdapter);

        addDots(0);
        binding.tutorial.addOnPageChangeListener(changeListener);


        binding.btnStart.setOnClickListener(this::skip);
        binding.skipBtn.setOnClickListener(this::skip);

        binding.nextPage.setOnClickListener(this::next);
    }

    public void skip(View view) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void next(View view) {
        binding.tutorial.setCurrentItem(mCurrentPos + 1);
    }


    /**
     * Metoda za kreiranje tackica na aplikaciji kao indikacija na kojij stranici tutorijala se korisnik nalazi
     * @param position      Pozicija
     */
    private void addDots(int position) {

        mDots = new TextView[9];
        binding.dots.removeAllViews();

        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);

            binding.dots.addView(mDots[i]);
        }

        if (mDots.length > 0) {
            mDots[position].setTextColor(getResources().getColor(R.color.primaryDarkColorTheme1));
        }

    }

    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDots(position);
            mCurrentPos = position;
            if(position<8){
                binding.btnStart.setVisibility(View.INVISIBLE);
            } else{
                mAnimation = AnimationUtils.loadAnimation(TutorialActivity.this, R.anim.bottom_animation);
                binding.btnStart.setAnimation(mAnimation);
                binding.btnStart.setVisibility(View.VISIBLE);
            }

        }
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    /**
     *  Metoda za izbor theme
     * @return  parametar tipa Resources.Theme, izabrana tema
     */
    @Override
    public Resources.Theme getTheme() {
        Resources.Theme theme = super.getTheme();
        tutorialViewModel = new ViewModelProvider(this).get(TutorialViewModel.class);

        switch (tutorialViewModel.getPrefsTheme().getString("theme", "")){
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