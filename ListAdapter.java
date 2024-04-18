package com.example.task41p;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<TaskItem> {
    // Custom list adapter class to display task list
    public ListAdapter(@NonNull Context context, ArrayList<TaskItem> taskItemArrayList){
        super(context, R.layout.task_item_layout,taskItemArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent){
        // Retrieve task for given position
        TaskItem taskItem = getItem(position);

        if (view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.task_item_layout, parent, false);
        }

        // Populate display with task due date and title
        TextView dateDue = view.findViewById(R.id.dueDateText);
        TextView title = view.findViewById(R.id.taskNameText);

        dateDue.setText(taskItem.getDueDate());
        title.setText(taskItem.getTitle());

        return view;
    }
}
