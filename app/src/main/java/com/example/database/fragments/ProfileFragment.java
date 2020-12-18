package com.example.database.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.database.R;
import com.example.database.base.BaseFragment;
import com.example.database.data.UserModel;
import com.example.database.database.LocalStorage;
import com.example.database.databinding.FragmentProfileBinding;
import com.example.database.dialogs.AboutDialog;
import com.example.database.dialogs.ChangeLanguageDialog;
import com.zeugmasolutions.localehelper.LocaleHelper;

import java.io.File;
import java.util.Locale;


public class ProfileFragment extends BaseFragment {
    private UserModel userModel;


    FragmentProfileBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater);
        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userModel = LocalStorage.getUser();
        File imgFile = new File(userModel.getImage());
        if (imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            binding.circleImage.setImageBitmap(myBitmap);
        }
        binding.buttonAboutApp.setOnClickListener(a -> {
            AboutDialog aboutDialog = new AboutDialog(requireContext());
            aboutDialog.show();
        });
        binding.fullName.setText(userModel.getName());
        binding.textId.setText("ID: " + userModel.getId());
        binding.editImage.setOnClickListener(a -> {
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
        binding.buttonChangeLanguage.setOnClickListener(a -> {
        });
    }

    @Override
    public void onResume() {
        super.onResume();

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
                binding.circleImage.setImageBitmap(bitmap);
                userModel.setImage(file.getAbsolutePath());
                db.updateUser(userModel);
                LocalStorage.setUser(userModel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}