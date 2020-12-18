package com.example.database.dialogs;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.database.R;
import com.example.database.data.FacultyModel;
import com.example.database.data.GroupModel;
import com.example.database.database.LocalStorage;
import com.example.database.interfaceses.AddFaculty;
import com.example.database.interfaceses.AddGroup;
import com.example.database.interfaceses.PickImage;


public class AddGroupDialog extends AlertDialog {
    private final AddGroup addGroup;
    private final PickImage pickImage;
    private View view;

    public void addImage(Uri uri) {
        Glide.with(getOwnerActivity())
                .load(uri)
                .override(1280, 1280)
                .centerCrop()
                .into((ImageView) findViewById(R.id.imageLogo));
    }

    public AddGroupDialog(Context context, AddGroup addGroup, PickImage pickImage) {
        super(context);
        view = LayoutInflater.from(context).inflate(R.layout.add_group_dialog, null, false);
        setView(view);
        this.addGroup = addGroup;
        this.pickImage = pickImage;
        Button button = view.findViewById(R.id.btAddGroupToDB);
        TextView pickImageButton = view.findViewById(R.id.addAvatarButton);
        EditText editText = view.findViewById(R.id.inputGroupName);
        pickImageButton.setOnClickListener(a -> {
            pickImage.openGallery();
        });
        button.setOnClickListener(a -> {
            addGroup.addGroup(editText.getText().toString(), "");
            this.dismiss();
        });
    }
}


