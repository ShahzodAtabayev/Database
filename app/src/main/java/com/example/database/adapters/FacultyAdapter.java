package com.example.database.adapters;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.PopupMenu;

import com.example.database.data.FacultyModel;
import com.example.database.R;
import com.example.database.databinding.FacultyItemBinding;
import com.example.database.interfaceses.EditFaculty;

import java.io.File;
import java.util.ArrayList;
import java.util.List;



public class FacultyAdapter extends BaseAdapter {
    private ArrayList<FacultyModel> list = new ArrayList<>();
    private final ItemClick itemClick;
    private ItemClick remove;
    private EditFaculty update;

    public FacultyAdapter(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    public void submitList(List<FacultyModel> ls) {
        list = (ArrayList<FacultyModel>) ls;
        notifyDataSetChanged();
    }

    public void remove(ItemClick itemClick) {
        remove = itemClick;
    }

    public void update(EditFaculty itemClick) {
        update = itemClick;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public FacultyModel getItem(int position) {
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
            FacultyItemBinding binding = FacultyItemBinding.inflate(LayoutInflater.from(parent.getContext()));
            binding.getRoot().setTag(new ViewHolder(binding));
            view = binding.getRoot();
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.position = position;
        holder.bind();
        return view;
    }

    class ViewHolder {
        FacultyItemBinding binding;
        int position = -1;

        ViewHolder(FacultyItemBinding binding) {
            this.binding = binding;
            binding.clickLayout.setOnClickListener(a -> itemClick.onClick(getItem(position).getId()));
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
                            update.editFaculty(getItem(position));
                        }
                    }
                    return true;
                });
                menu.show();
            });
        }

        @SuppressLint("SetTextI18n")
        void bind() {
            FacultyModel f = getItem(position);
            File imgFile = new File(f.getImage());
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                binding.image.setImageBitmap(myBitmap);
            } else {
                binding.image.setImageResource(R.drawable.image);
            }
            binding.name.setText(f.getName());
            binding.studentCount.setText(f.getStudentCount() + " students");
        }
    }
}


