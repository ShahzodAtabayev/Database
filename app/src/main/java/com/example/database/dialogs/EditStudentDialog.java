package com.example.database.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.database.R;
import com.example.database.data.StudentModel;
import com.example.database.interfaceses.EditStudent;
import com.example.database.interfaceses.PickImage;


public class EditStudentDialog extends AlertDialog {
    private final EditStudent editStudent;
    private final PickImage pickImage;
    private View view;
    private long id = 0;
    private long groupId = 0;

    public void loadData(StudentModel studentModel) {
        EditText editText = view.findViewById(R.id.editFirstName);
        EditText editText2 = view.findViewById(R.id.editLastName);
        editText.setText(studentModel.getFirstName());
        editText2.setText(studentModel.getLastName());
        id = studentModel.getId();
        groupId = studentModel.getGroupId();
    }

    public EditStudentDialog(Context context, EditStudent editStudent, PickImage pickImage) {
        super(context);
        view = LayoutInflater.from(context).inflate(R.layout.add_student_dialog, null, false);
        setView(view);
        this.editStudent = editStudent;
        this.pickImage = pickImage;
        Button button = view.findViewById(R.id.btEditStudentToDB);
        TextView pickImageButton = view.findViewById(R.id.editStudentAvatarButton);
        EditText editText1 = view.findViewById(R.id.editFirstName);
        EditText editText2 = view.findViewById(R.id.editLastName);
        pickImageButton.setOnClickListener(a -> pickImage.openGallery());
        button.setOnClickListener(a -> {
            StudentModel studentModel = new StudentModel(editText1.getText().toString(), editText2.getText().toString(), groupId, "");
            studentModel.setId(id);
            editStudent.editStudent(studentModel);
            this.dismiss();
        });
    }
}


