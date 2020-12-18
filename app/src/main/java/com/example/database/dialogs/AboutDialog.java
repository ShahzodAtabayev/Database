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
import com.example.database.interfaceses.AddGroup;
import com.example.database.interfaceses.PickImage;


public class AboutDialog extends AlertDialog {

    public AboutDialog(Context context) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.about_dialog, null, false);
        setView(view);
        Button button = view.findViewById(R.id.btUnderstand);
        button.setOnClickListener(a -> {
            dismiss();
        });
    }
}


