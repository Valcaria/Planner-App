package com.example.planerapplication.view.tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.planerapplication.R;
import com.example.planerapplication.adapter.WeekRecyclerAdapter;
import com.example.planerapplication.databinding.FragmentWeekBinding;
import com.example.planerapplication.viewmodel.DateViewModel;
import com.example.planerapplication.singleton.FindIDSingletonClass;
import com.example.planerapplication.viewmodel.TodayViewModel;
import com.example.planerapplication.viewmodel.WeekViewModel;

import java.util.Date;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FragmentWeek extends Fragment implements WeekRecyclerAdapter.OnItemClickedListener{
    private final boolean[] openClosed = new boolean[]{false,false,false,false,false,false,false};

    private FragmentWeekBinding binding;
    private DateViewModel viewModel;
    private WeekViewModel weekViewModel;

    private WeekRecyclerAdapter recyclerAdapter;
    Date[] dates;

    private final boolean more = true;
    public FragmentWeek() {
        // Required empty public constructor
    }

    public static FragmentWeek newInstance() {

        Bundle args = new Bundle();

        FragmentWeek fragment = new FragmentWeek();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_week,container,false);
        viewModel = new ViewModelProvider(requireActivity()).get(DateViewModel.class);
        weekViewModel = new ViewModelProvider(this).get(WeekViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setWeekViewModel(weekViewModel);


        addingWeeklyTasks(0);
        observeNextPreviousClicked(weekViewModel);

        return binding.getRoot();
    }



    /**
     * Metoda za dodavanje sedmicnih obvazeva i zadataka
     * @param week      parametar tipa int, da li je sedmica trenutna (0), prethodna (-1) ili naredna (+1)
     */
    public void addingWeeklyTasks(int week){
        dates = weekViewModel.getWeekDates(week, dates);

        binding.recViewWeekTasks.removeAllViews();

        weekViewModel.getWeeklyTasks(dates[0].getTime(),dates[6].getTime()).observe(getViewLifecycleOwner(), tasksList -> {
            recyclerAdapter = new WeekRecyclerAdapter(tasksList,this, dates,viewModel,openClosed);
            binding.recViewWeekTasks.setLayoutManager(new LinearLayoutManager(this.getContext()));
            binding.recViewWeekTasks.setAdapter(recyclerAdapter);
        });


        weekViewModel.getWeekNumber().observe(getViewLifecycleOwner(), weekNumber -> {
            binding.txtWeek.setText( getString(R.string.tab_week)+ " "+weekNumber);
        });
    }

    private void observeNextPreviousClicked(WeekViewModel viewModel) {
        viewModel.getNextPreviousWeek().observe(getViewLifecycleOwner(), week -> {
           addingWeeklyTasks(week);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setLanguage();
    }

    /**
     * Metoda za postavljanje teksta u izabrani jezik
     */
    private void setLanguage(){
        weekViewModel.getWeekNumber().observe(getViewLifecycleOwner(), weekNumber -> {
            binding.txtWeek.setText( getString(R.string.tab_week)+ " "+weekNumber);
        });
    }

    @Override
    public void OnDayInWeekClicked() {

        ViewPager2 viewPager = getActivity().findViewById(R.id.view_pager);
        viewPager.setCurrentItem(0);
    }

    @Override
    public void OnMoreCLicked() {
        FindIDSingletonClass txtMoreID = FindIDSingletonClass.getInstance();
        if(openClosed[txtMoreID.getData()]){
            openClosed[txtMoreID.getData()] = false;
        } else {
            openClosed[txtMoreID.getData()] = true;
        }
        addingWeeklyTasks(0);
    }
}