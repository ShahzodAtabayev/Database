package com.example.database.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Executors;
import com.example.database.base.BaseFragment;
import com.example.database.R;
import com.example.database.adapters.GroupsAdapter;
import com.example.database.data.GroupModel;
import com.example.database.database.LocalStorage;
import com.example.database.databinding.FragmentGroupsBinding;
import com.example.database.dialogs.AddGroupDialog;
import com.example.database.dialogs.EditGroupDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static id.zelory.compressor.UtilKt.saveBitmap;


public class GroupsFragment extends BaseFragment {
    private FragmentGroupsBinding binding;
    private long facultyId = 0;
    private GroupsAdapter groupsAdapter;
    String imagePath = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGroupsBinding.inflate(inflater);
        assert getArguments() != null;
        facultyId = GroupsFragmentArgs.fromBundle(getArguments()).getFacultyId();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        groupsAdapter = new GroupsAdapter(id -> NavHostFragment.findNavController(this).navigate(GroupsFragmentDirections.actionGroupsFragmentToCalendarFragment(id)));
        binding.listGroups.setAdapter(groupsAdapter);
        groupsAdapter.remove(id -> runOnWorkerThread(() -> {
            db.removeGroup(id);
            List<GroupModel> ls = db.getGroupsByFacultyId(facultyId);
            runOnUIThread(() -> groupsAdapter.submitList(ls));
        }));
        groupsAdapter.update(data -> {
            EditGroupDialog editGroupDialog = new EditGroupDialog(requireContext(), a -> runOnWorkerThread(() -> {
                a.setImage(imagePath);
                imagePath = "";
                a.setFacultyId(facultyId);
                db.updateGroup(a);
                List<GroupModel> ls = db.getGroupsByFacultyId(facultyId);
                runOnUIThread(() -> groupsAdapter.submitList(ls));
            }), this::startGallery);
            editGroupDialog.loadData(data);
            editGroupDialog.show();
            Log.d("WWW", "update");
        });
        runOnWorkerThread(() -> {
            List<GroupModel> ls = db.getGroupsByFacultyId(facultyId);
            runOnUIThread(() -> {
                groupsAdapter.submitList(ls);
            });
        });
        binding.btAddGroup.setOnClickListener(a -> {
            AddGroupDialog addGroupDialog = new AddGroupDialog(requireContext(), (name, image) -> {
                Log.d("TTT", facultyId + " facultyId");
                GroupModel groupModel = new GroupModel(name, image, facultyId);
                groupModel.setImage(imagePath);
                imagePath = "";
                runOnWorkerThread(() -> {
                    db.addGroup(groupModel);
                    List<GroupModel> ls = db.getGroupsByFacultyId(facultyId);
                    runOnUIThread(() -> groupsAdapter.submitList(ls));
                });
            }, () -> {
                int permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE);

                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                    startGallery();
                } else {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            2000);
                }
            });
            addGroupDialog.show();
        });
    }

    private void startGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            binding.tempImage.setImageURI(data.getData());
            BitmapDrawable bitmapDrawable = (BitmapDrawable) binding.tempImage.getDrawable();
            Bitmap bitmap = resizedBitmap(bitmapDrawable.getBitmap(), 512, 512);
            int image_count = LocalStorage.read("image_count", 0);
            try {
                @SuppressLint("SdCardPath") File directory = new File("/data/data/com.example.database/", "files");
                if (!directory.exists()) {
                    directory.mkdir();
                }
                @SuppressLint("SdCardPath") File file = new File("/data/data/com.example.database/files/", "image" + image_count + ".png");
                LocalStorage.write("image_count", ++image_count);
                if (!file.exists()) {
                    file.createNewFile();
                }
                saveBitmap(bitmap, file);
                imagePath = file.getAbsolutePath();
            } catch (Exception ignored) {

            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        groupsAdapter.submitList(db.getGroupsByFacultyId(facultyId));
    }


}