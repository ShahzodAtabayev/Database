package com.example.database.adapters;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.database.data.StudentHistoryModel;
import com.example.database.data.StudentModel;
import com.example.database.databinding.HistoriesItemBinding;
import com.example.database.databinding.StudentItemBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class HistoriesAdapter extends BaseAdapter {
    private ArrayList<StudentHistoryModel> list = new ArrayList<>();

    public void submitList(List<StudentHistoryModel> ls) {
        list = (ArrayList<StudentHistoryModel>) ls;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public StudentHistoryModel getItem(int position) {
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
            HistoriesItemBinding binding = HistoriesItemBinding.inflate(LayoutInflater.from(parent.getContext()));
            binding.getRoot().setTag(new ViewHolder(binding));
            view = binding.getRoot();
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.position = position;
        holder.bind();
        return view;
    }

    class ViewHolder {
        HistoriesItemBinding binding;
        int position = -1;

        ViewHolder(HistoriesItemBinding binding) {
            this.binding = binding;
        }

        @SuppressLint("SetTextI18n")
        void bind() {
            StudentHistoryModel f = getItem(position);
            binding.textName.setText(f.getFirstName() + "  " + f.getLastName());
            binding.textId.setText("ID: " + f.getId());
            File imgFile = new File(f.getImage());
            if (f.isChecked().equals("true")) {
                binding.plus.setVisibility(View.VISIBLE);
                binding.minus.setVisibility(View.GONE);
            } else {
                binding.minus.setVisibility(View.VISIBLE);
                binding.plus.setVisibility(View.GONE);
            }
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                binding.imageAvatar.setImageBitmap(myBitmap);
            }
        }
    }
}
