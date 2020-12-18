package com.example.database.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.database.R;
import com.example.database.databinding.FragmentCalendarBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class CalendarFragment extends Fragment {
    private FragmentCalendarBinding binding;
    private long groupId = 0;
    private String selectedDate = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCalendarBinding.inflate(inflater);
        Date date = new Date(binding.calendar.getDate());
        selectedDate = dateToString(date, "yyyyMMdd");
        groupId = CalendarFragmentArgs.fromBundle(getArguments()).getGroupId();
        binding.btnReady.setOnClickListener(a -> {
            NavHostFragment.findNavController(this).navigate(CalendarFragmentDirections.actionCalendarFragmentToStudentsFragment(groupId, selectedDate));
        });
        binding.calendar.setWeekNumberColor(R.color.black);
        binding.calendar.setMinDate(Calendar.getInstance().getTimeInMillis());
        CalendarView.OnDateChangeListener myCalendarListener = (view, year, month, day) -> {
            month = month + 1;
            selectedDate = year + "" + month + "" + day;
            Log.d("SSSS", selectedDate);
        };
        binding.calendar.setOnDateChangeListener(myCalendarListener);
        return binding.getRoot();
    }

    String dateToString(Date date, String format) {
        Locale locale = Locale.getDefault();
        SimpleDateFormat formatter = new SimpleDateFormat(format, locale);
        return formatter.format(date);
    }

}
