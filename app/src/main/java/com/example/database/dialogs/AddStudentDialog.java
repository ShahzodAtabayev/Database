package com.example.database.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.database.R;
import com.example.database.interfaceses.AddStudent;
import com.example.database.interfaceses.PickImage;


public class AddStudentDialog extends AlertDialog {
    private final AddStudent addStudent;
    private final PickImage pickImage;
    private View view;

    public void addImage(Uri uri) {
        Glide.with(getOwnerActivity())
                .load(uri)
                .override(1280, 1280)
                .centerCrop()
                .into((ImageView) findViewById(R.id.imageLogo));
    }

    public AddStudentDialog(Context context, AddStudent addStudent, PickImage pickImage) {
        super(context);
        view = LayoutInflater.from(context).inflate(R.layout.add_student_dialog, null, false);
        setView(view);
        this.addStudent = addStudent;
        this.pickImage = pickImage;
        Button button = view.findViewById(R.id.btEditStudentToDB);
        TextView pickImageButton = view.findViewById(R.id.editStudentAvatarButton);
        EditText editText1 = view.findViewById(R.id.editFirstName);
        EditText editText2 = view.findViewById(R.id.editLastName);
        pickImageButton.setOnClickListener(a -> {
            pickImage.openGallery();
        });
        button.setOnClickListener(a -> {
            addStudent.addStudent(editText1.getText().toString(), editText2.getText().toString(), "");
            this.dismiss();
        });
    }
}


