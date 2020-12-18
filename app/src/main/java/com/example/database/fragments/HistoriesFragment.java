package com.example.database.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.database.adapters.FacultyAdapter;
import com.example.database.adapters.GroupsAdapter;
import com.example.database.adapters.HistoriesAdapter;
import com.example.database.base.BaseFragment;
import com.example.database.data.FacultyModel;
import com.example.database.data.GroupModel;
import com.example.database.data.StudentHistoryModel;
import com.example.database.database.LocalStorage;
import com.example.database.databinding.FragmentHistoriesBinding;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class HistoriesFragment extends BaseFragment {
    private FragmentHistoriesBinding binding;
    private HistoriesAdapter historiesAdapter;
    private FacultyAdapter facultyAdapter;
    private GroupsAdapter groupsAdapter;
    private String selectedDate = "";
    private long groupId = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHistoriesBinding.inflate(inflater);
        binding.calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate = year + month + dayOfMonth + "";
        });
        historiesAdapter = new HistoriesAdapter();
        facultyAdapter = new FacultyAdapter(id -> {
            binding.listFaculties.setVisibility(View.GONE);
            runOnWorkerThread(() -> {
                List<GroupModel> ls = db.getGroupsByFacultyId(id);
                runOnUIThread(() -> {
                    groupsAdapter.submitList(ls);
                    binding.listGroups.setVisibility(View.VISIBLE);
                });
            });
        });
        groupsAdapter = new GroupsAdapter(id -> {
            binding.listGroups.setVisibility(View.GONE);
            Calendar cal = Calendar.getInstance();
            @SuppressLint("SimpleDateFormat") DatePickerDialog date = DatePickerDialog.newInstance((view, year, monthOfYear, dayOfMonth) -> {
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, monthOfYear);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                selectedDate = new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
                makeToast(selectedDate);
                runOnWorkerThread(() -> {
                    List<StudentHistoryModel> ls = db.getHistories(selectedDate, groupId);
                    runOnUIThread(() -> {
                        historiesAdapter.submitList(ls);
                        binding.calendarView.setVisibility(View.GONE);
                        binding.btnReady.setVisibility(View.GONE);
                        binding.listHistories.setVisibility(View.VISIBLE);
                    });
                });
            });
            date.show(this.getParentFragmentManager(), "");
//            binding.calendarView.setVisibility(View.VISIBLE);
//            binding.btnReady.setVisibility(View.VISIBLE);
            groupId = id;
        });
        binding.btnReady.setOnClickListener(a -> {

        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.listFaculties.setAdapter(facultyAdapter);
        binding.listGroups.setAdapter(groupsAdapter);
        binding.listHistories.setAdapter(historiesAdapter);
        runOnWorkerThread(() -> {
            List<FacultyModel> ls = db.getFacultyByUserId(LocalStorage.getUser().getId());
            runOnUIThread(() -> {
                facultyAdapter.submitList(ls);
                binding.listFaculties.setVisibility(View.VISIBLE);
            });
        });
    }
}