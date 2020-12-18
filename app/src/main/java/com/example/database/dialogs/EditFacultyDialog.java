package com.example.database.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.database.R;
import com.example.database.data.FacultyModel;
import com.example.database.database.LocalStorage;
import com.example.database.interfaceses.AddFaculty;
import com.example.database.interfaceses.PickImage;


public class EditFacultyDialog extends AlertDialog {
    private AddFaculty addFaculty;
    private long userId = LocalStorage.getUser().getId();
    private final PickImage pickImage;
    Button button;
    TextView addImage;
    EditText editText;
    private long id = 0;

    public void loadData(FacultyModel facultyModel) {
        editText.setText(facultyModel.getName());
        id = facultyModel.getId();
    }


    public EditFacultyDialog(Context context, AddFaculty addFaculty, PickImage pickImage) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.add_faculty_dialog, null, false);
        setView(view);
        this.pickImage = pickImage;
        this.addFaculty = addFaculty;
        button = (Button) view.findViewById(R.id.btUpdateFaculty);
        addImage = (TextView) view.findViewById(R.id.editFacultyAvatarButton);
        editText = view.findViewById(R.id.editFacultyName);
        button.setOnClickListener(a -> {
            FacultyModel facultyModel = new FacultyModel(editText.getText().toString(), "", userId);
            facultyModel.setId(id);
            addFaculty.addFaculty(facultyModel);
            this.dismiss();
        });
        addImage.setOnClickListener(a -> pickImage.openGallery());
    }

}
