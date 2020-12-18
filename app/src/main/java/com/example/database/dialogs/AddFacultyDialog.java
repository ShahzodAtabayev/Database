package com.example.database.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.database.data.FacultyModel;
import com.example.database.database.LocalStorage;
import com.example.database.R;
import com.example.database.interfaceses.AddFaculty;
import com.example.database.interfaceses.PickImage;


public class AddFacultyDialog extends AlertDialog {
    private AddFaculty addFaculty;
    private long userId = LocalStorage.getUser().getId();
    private final PickImage pickImage;

    public AddFacultyDialog(Context context, AddFaculty addFaculty, PickImage pickImage) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.add_faculty_dialog, null, false);
        setView(view);
        this.pickImage = pickImage;
        this.addFaculty = addFaculty;
        Button button = (Button) view.findViewById(R.id.btUpdateFaculty);
        TextView addImage = (TextView) view.findViewById(R.id.editFacultyAvatarButton);
        EditText editText = view.findViewById(R.id.editFacultyName);
        button.setOnClickListener(a -> {
            addFaculty.addFaculty(new FacultyModel(editText.getText().toString(), "bkb", userId));
            this.dismiss();
        });
        addImage.setOnClickListener(a -> pickImage.openGallery());
    }

}
