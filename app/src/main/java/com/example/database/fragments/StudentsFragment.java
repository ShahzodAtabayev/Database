package com.example.database.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.database.R;
import com.example.database.adapters.ClickCheckbox;
import com.example.database.adapters.StudentsAdapter;
import com.example.database.base.BaseFragment;
import com.example.database.data.FacultyModel;
import com.example.database.data.HistoryModel;
import com.example.database.data.StudentModel;
import com.example.database.database.LocalStorage;
import com.example.database.databinding.FragmentStudentsBinding;
import com.example.database.dialogs.AddStudentDialog;
import com.example.database.dialogs.EditStudentDialog;
import com.example.database.interfaceses.AddStudent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class StudentsFragment extends BaseFragment implements ClickCheckbox, AddStudent {
    private long groupId = 0;
    private String date = "";
    private String imagePath = "";
    private final List<HistoryModel> historyModels = new ArrayList<>();

    FragmentStudentsBinding binding;
    private StudentsAdapter adapter;
    private final List<StudentModel> list = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStudentsBinding.inflate(inflater);
        setHasOptionsMenu(true);
        assert getArguments() != null;
        groupId = StudentsFragmentArgs.fromBundle(getArguments()).getGroupId();
        date = StudentsFragmentArgs.fromBundle(getArguments()).getDate();
        adapter = new StudentsAdapter(this);
        binding.listStudents.setAdapter(adapter);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter.remove(id -> {
            runOnWorkerThread(() -> {
                db.removeStudent(id);
                List<StudentModel> ls = db.getStudentsByGroupId(groupId);
                runOnUIThread(() -> adapter.submitList(ls));
            });
        });
        adapter.update(id -> {
            EditStudentDialog dialog = new EditStudentDialog(requireContext(), a -> runOnWorkerThread(() -> {
                a.setImage(imagePath);
                db.updateStudent(a);
                List<StudentModel> ls = db.getStudentsByGroupId(groupId);
                imagePath = "";
                runOnUIThread(() -> adapter.submitList(ls));
            }), this::startGallery);
            dialog.loadData(id);
            dialog.show();
            Log.d("WWW", "update");
        });
        runOnWorkerThread(() -> {
            List<StudentModel> ls = db.getStudentsByGroupId(groupId);
            runOnUIThread(() -> {
                adapter.submitList(ls);
                for (int i = 0; i < ls.size(); i++) {
                    historyModels.add(new HistoryModel(groupId, ls.get(i).getId(), date, "false"));
                }
            });
        });
        binding.btnReadyCheckout.setOnClickListener(a -> {
            runOnWorkerThread(() -> {

                runOnUIThread(() -> {
                    NavHostFragment.findNavController(this).navigate(StudentsFragmentDirections.actionStudentsFragmentToHomeFragment());
                });
            });
//            db.addHistories(historyModels);
        });
        binding.searchView.setOnSearchClickListener(v -> Log.d("TTT", "Search on click"));
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("TTT", "onQueryTextSubmit" + query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu, menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addStudent: {
                AddStudentDialog studentDialog = new AddStudentDialog(requireContext(), this, this::startGallery);
                studentDialog.show();
            }
            default: {
            }
        }
        return NavigationUI.onNavDestinationSelected(item, NavHostFragment.findNavController(this)) || super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(boolean value, int position) {
        Log.d("KKK", value + "");
        historyModels.get(position).setIsChecked(value + "");
    }

    @Override
    public void addStudent(String firstName, String lastName, String image) {
        Log.d("TTT", groupId + " groupId");
        StudentModel studentModel = new StudentModel(firstName, lastName, groupId, image);
        studentModel.setImage(imagePath);
        imagePath = "";
        runOnWorkerThread(() -> {
            db.addStudent(studentModel);
            List<StudentModel> ls = db.getStudentsByGroupId(groupId);
            runOnUIThread(() -> {
                adapter.submitList(ls);
                historyModels.clear();
                for (int i = 0; i < ls.size(); i++) {
                    historyModels.add(new HistoryModel(groupId, ls.get(i).getId(), date, false + ""));
                }
            });
        });
    }

    private void startGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 200);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            binding.tempImage.setImageURI(data.getData());
            BitmapDrawable bitmapDrawable = (BitmapDrawable) binding.tempImage.getDrawable();
            Bitmap bitmap = resizedBitmap(bitmapDrawable.getBitmap(), 512, 512);
            int image_count = LocalStorage.read("image_count", 0);
            try {
                @SuppressLint("SdCardPath") File directory = new File("/data/data/com.example.database/", "files");
                if (!directory.exists()) directory.mkdir();
                @SuppressLint("SdCardPath") File file = new File("/data/data/com.example.database/files/", "image" + image_count + ".png");
                LocalStorage.write("image_count", ++image_count);
                if (!file.exists()) {
                    file.createNewFile();
                }
                saveBitmap(bitmap, file);
                imagePath = file.getAbsolutePath();
            } catch (Exception e) {

            }
        }
    }
}