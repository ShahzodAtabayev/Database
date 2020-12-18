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
import com.example.database.data.GroupModel;
import com.example.database.database.LocalStorage;
import com.example.database.interfaceses.EditGroup;
import com.example.database.interfaceses.PickImage;


public class EditGroupDialog extends AlertDialog {
    private EditGroup editGroup;
    private long userId = LocalStorage.getUser().getId();
    private final PickImage pickImage;
    Button button;
    TextView addImage;
    EditText editText;
    private long id = 0;

    public void loadData(GroupModel groupModel) {
        editText.setText(groupModel.getName());
        id = groupModel.getId();
    }


    public EditGroupDialog(Context context, EditGroup editGroup, PickImage pickImage) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.add_faculty_dialog, null, false);
        setView(view);
        this.pickImage = pickImage;
        this.editGroup = editGroup;
        button = (Button) view.findViewById(R.id.btUpdateFaculty);
        addImage = (TextView) view.findViewById(R.id.editFacultyAvatarButton);
        editText = view.findViewById(R.id.editFacultyName);
        button.setOnClickListener(a -> {
            GroupModel groupModel = new GroupModel(editText.getText().toString(), "", userId);
            groupModel.setId(id);
            editGroup.editGroup(groupModel);
            this.dismiss();
        });
        addImage.setOnClickListener(a -> pickImage.openGallery());
    }

}
