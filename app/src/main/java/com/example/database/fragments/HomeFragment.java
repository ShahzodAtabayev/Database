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
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;

import com.example.database.database.LocalStorage;
import com.example.database.adapters.FacultyAdapter;
import com.example.database.apps.App;
import com.example.database.base.BaseFragment;
import com.example.database.data.FacultyModel;
import com.example.database.databinding.FragmentHomeBinding;
import com.example.database.dialogs.AddFacultyDialog;
import com.example.database.dialogs.EditFacultyDialog;

import java.io.File;
import java.util.List;


public class HomeFragment extends BaseFragment {
    FragmentHomeBinding binding;
    FacultyAdapter adapter;
    long userId = LocalStorage.getUser().getId();
    String imagePath = "";

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new FacultyAdapter(id -> NavHostFragment.findNavController(this).navigate(HomeFragmentDirections.actionHomeFragmentToGroupsFragment(id)));
        binding.list.setAdapter(adapter);
        adapter.remove(id -> runOnWorkerThread(() -> {
            db.removeFaculty(id);
            List<FacultyModel> ls = db.getFacultyByUserId(userId);
            runOnUIThread(() -> adapter.submitList(ls));
        }));
        adapter.update(id -> {
            EditFacultyDialog dialog = new EditFacultyDialog(requireContext(), a -> runOnWorkerThread(() -> {
                a.setImage(imagePath);
                a.setUserId(userId);

                db.updateFaculty(a);
                List<FacultyModel> ls = db.getFacultyByUserId(userId);
                runOnUIThread(() -> {
                    adapter.submitList(ls);
                    imagePath = "";
                });
            }), this::startGallery);
            dialog.loadData(id);
            dialog.show();
        });
        binding.btAddFaculty.setOnClickListener(a -> {
            AddFacultyDialog addFacultyDialog = new AddFacultyDialog(requireContext(), facultyModel -> {
                facultyModel.setImage(imagePath);
                imagePath = "";
                runOnWorkerThread(() -> {
                    db.addFaculty(facultyModel);
                    List<FacultyModel> facultyModels = db.getFacultyByUserId(userId);
                    runOnUIThread(() -> {
                        adapter.submitList(facultyModels);
                    });
                });
            }, this::startGallery);
            addFacultyDialog.show();
        });
        runOnWorkerThread(() -> {
            List<FacultyModel> ls = db.getFacultyByUserId(userId);
            runOnUIThread(() -> adapter.submitList(ls));
        });
    }

    private void startGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1000);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK) {
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
                e.printStackTrace();
            }
        }
    }
}
