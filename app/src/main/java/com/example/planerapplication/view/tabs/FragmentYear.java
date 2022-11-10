package com.example.planerapplication.view.tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.planerapplication.R;
import com.example.planerapplication.databinding.FragmentYearBinding;
import com.example.planerapplication.viewmodel.DateViewModel;
import com.example.planerapplication.viewmodel.YearViewModel;

import java.util.Calendar;

public class FragmentYear extends Fragment{

    private FragmentYearBinding binding;

    private YearViewModel yearViewModel;

    DateViewModel dateViewModel;

    private  String yearString;
    private int year = Calendar.getInstance().get(Calendar.YEAR);


    public FragmentYear() {
        // Required empty public constructor
    }

    public static FragmentYear newInstance() {

        Bundle args = new Bundle();

        FragmentYear fragment = new FragmentYear();
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_year,container,false);
        yearViewModel = new ViewModelProvider(this).get(YearViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setYearViewModel(yearViewModel);

        yearString = String.valueOf(year);
        binding.txtYear.setText(yearString);

        yearViewModel.setYear(year);


        observeMonthClicked(yearViewModel);
        observeNextPreviousClicked(yearViewModel);

        return binding.getRoot();
    }

    private void observeMonthClicked(YearViewModel viewModel) {
        viewModel.getDate().observe(getViewLifecycleOwner(), date -> {
            dateViewModel = new ViewModelProvider(requireActivity()).get(DateViewModel.class);
            dateViewModel.setSelectItem(date);

            ViewPager2 viewPager = getActivity().findViewById(R.id.view_pager);
            viewPager.setCurrentItem(2);
        });
    }

    private void observeNextPreviousClicked(YearViewModel viewModel) {
        viewModel.getYear().observe(getViewLifecycleOwner(), year -> {
            yearString=String.valueOf(year);
            binding.txtYear.setText(yearString);
        });
    }




}