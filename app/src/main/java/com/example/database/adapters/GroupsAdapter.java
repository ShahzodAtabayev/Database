package com.example.database.adapters;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.PopupMenu;

import com.example.database.R;
import com.example.database.data.GroupModel;
import com.example.database.databinding.GroupItemBinding;
import com.example.database.interfaceses.EditGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;



public class GroupsAdapter extends BaseAdapter {
    private List<GroupModel> list = new ArrayList<>();
    private final ItemClick onClick;
    private ItemClick remove;
    private EditGroup update;

    public GroupsAdapter(ItemClick onClick) {
        this.onClick = onClick;
    }

    public void submitList(List<GroupModel> ls) {
        list = ls;
        notifyDataSetChanged();
    }

    public void remove(ItemClick itemClick) {
        remove = itemClick;
    }

    public void update(EditGroup itemClick) {
        update = itemClick;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public GroupModel getItem(int position) {
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
            GroupItemBinding binding = GroupItemBinding.inflate(LayoutInflater.from(parent.getContext()));
            binding.getRoot().setTag(new ViewHolder(binding));
            view = binding.getRoot();
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.position = position;
        holder.bind();
        return view;
    }

    class ViewHolder {
        GroupItemBinding binding;
        int position = -1;

        ViewHolder(GroupItemBinding binding) {
            this.binding = binding;
            binding.layoutGroup.setOnClickListener(a -> onClick.onClick(getItem(position).getId()));
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
                            update.editGroup(getItem(position));
                        }
                    }
                    return true;
                });
                menu.show();
            });
        }


        @SuppressLint("SetTextI18n")
        void bind() {
            GroupModel f = getItem(position);
            File imgFile = new File(f.getImage());
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                binding.image.setImageBitmap(myBitmap);
            }
            binding.name.setText(f.getName());
            binding.studentCount.setText(f.getStudentCount() + " students");
        }
    }
}
