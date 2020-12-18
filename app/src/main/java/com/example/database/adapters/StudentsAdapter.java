package com.example.database.adapters;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.PopupMenu;

import com.example.database.R;
import com.example.database.data.StudentModel;
import com.example.database.databinding.StudentItemBinding;
import com.example.database.interfaceses.EditStudent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class StudentsAdapter extends BaseAdapter {
    private List<StudentModel> list = new ArrayList<>();
    private ClickCheckbox onClick;
    private ItemClick remove;
    private EditStudent update;

    public StudentsAdapter(ClickCheckbox onClick) {
        this.onClick = onClick;
    }

    public void submitList(List<StudentModel> ls) {
        list = ls;
        notifyDataSetChanged();
    }

    public void remove(ItemClick itemClick) {
        remove = itemClick;
    }

    public void update(EditStudent itemClick) {
        update = itemClick;
    }

    @Override

    public int getCount() {
        return list.size();
    }

    @Override
    public StudentModel getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            StudentItemBinding binding = StudentItemBinding.inflate(LayoutInflater.from(parent.getContext()));
            binding.getRoot().setTag(new ViewHolder(binding));
            view = binding.getRoot();
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.position = position;
        holder.bind();
        return view;
    }

    class ViewHolder {
        StudentItemBinding binding;
        int position = -1;

        ViewHolder(StudentItemBinding binding) {
            this.binding = binding;
            binding.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> onClick.onClick(isChecked, position));
            binding.morePopup.setOnClickListener(a -> {
                PopupMenu menu = new PopupMenu(binding.getRoot().getContext(), a);
                menu.inflate(R.menu.popup_menu);
                menu.setOnMenuItemClickListener(item -> {
                    if (item.getItemId() == R.id.menuRemove) {
                        if (remove != null) {
                            remove.onClick(getItemId(position));
                        }
                    } else {
                        if (update != null) {
                            update.editStudent(getItem(position));
                        }
                    }
                    return true;
                });
                menu.show();
            });
        }

        @SuppressLint("SetTextI18n")
        void bind() {
            StudentModel f = getItem(position);
            binding.textName.setText(f.getFirstName() + "  " + f.getLastName());
            binding.textId.setText("ID: " + f.getId());
            File imgFile = new File(f.getImage());
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                binding.imageAvatar.setImageBitmap(myBitmap);
            }
        }
    }
/*
*  val menu = PopupMenu(context, view1)
                    menu.inflate(R.menu.popup_menu)
                    menu.setOnMenuItemClickListener {
                        when (it.itemId) {
                            R.id.menuButtonDone -> {
                                listenerDone?.invoke(data[adapterPosition])
                                listenerDelete?.invoke(adapterPosition)
                            }
                            R.id.menuButtonCanceled -> {
                                listenerCanceled?.invoke(data[adapterPosition])
                                listenerDelete?.invoke(adapterPosition)
                            }
                        }
                        true
                    }
                    menu.show()
* */

}
