package com.example.planerapplication.helper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.planerapplication.R;
import com.example.planerapplication.view.tabs.FragmentMonth;
import com.example.planerapplication.view.tabs.FragmentToday;
import com.example.planerapplication.view.tabs.FragmentWeek;
import com.example.planerapplication.view.tabs.FragmentYear;

public class StateAdapter extends FragmentStateAdapter {
    private static final int[] TAB_TITLES = new int[]{R.string.tab_day, R.string.tab_week, R.string.tab_month,R.string.tab_year};

    public StateAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return FragmentToday.newInstance();
            case 1:
                return FragmentWeek.newInstance();
            case 2:
                return FragmentMonth.newInstance();
            case 3:
                return FragmentYear.newInstance();
        }
        return FragmentToday.newInstance();
        /*Fragment fragment = this.mFragmentList.get(position);
        return fragment;*/
    }

    @Override
    public int getItemCount() {
        return TAB_TITLES.length;
    }

    public void refreshFragment(){
        notifyItemChanged(0);
    }
}
