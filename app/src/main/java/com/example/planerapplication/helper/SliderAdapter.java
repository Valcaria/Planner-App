package com.example.planerapplication.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.example.planerapplication.R;

public class SliderAdapter extends PagerAdapter{

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context) {
        this.context = context;
    }

    int[] images = {
            0,
            R.drawable.list_tutorial ,
            R.drawable.new_tutorial,
            R.drawable.swipe_tutorial,
            R.drawable.details_tutorial,
            R.drawable.week_month_tutorial,
            R.drawable.progress_tutorial,
            0,
            0
    };

    int[] description = {
            R.string.welcome,
            R.string.first_page,
            R.string.second_page,
            R.string.third_page,
            R.string.forth_page,
            R.string.fifth_page,
            R.string.sixth_page,
            R.string.seventh_page,
            R.string.end_of_tutorial
    };



    @Override
    public int getCount() {
        return 9;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v =layoutInflater.inflate(R.layout.slider_layout,container,false);

        ImageView imageView =v.findViewById(R.id.imageSlider);
        TextView txtDescription = v.findViewById(R.id.txtDescription);


        if(position == 0){
            TextView txtWelcome = v.findViewById(R.id.txtWelcome);

            imageView.setVisibility(View.GONE);
            txtDescription.setVisibility(View.GONE);
            txtWelcome.setVisibility(View.VISIBLE);
        } if(position == 8 || position == 7){
            TextView txtLetsStart = v.findViewById(R.id.txtStart);

            imageView.setVisibility(View.GONE);
            txtDescription.setVisibility(View.GONE);
            txtLetsStart.setVisibility(View.VISIBLE);

            txtLetsStart.setText(description[position]);
        }
        else{
            if(images[position]!= 0){
                imageView.setImageResource(images[position]);
            } else{
                imageView.setVisibility(View.GONE);
            }

            txtDescription.setText(description[position]);

        }

        container.addView(v);


        return v;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }
}
