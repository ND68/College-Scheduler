package com.example.collegescheduler.ui.todo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.collegescheduler.R;

import java.util.ArrayList;

public class TodoListAdapter extends ArrayAdapter<TodoTask> {

    private Context context;
    private ArrayList<TodoTask> todoList;

    public TodoListAdapter(Context context, ArrayList<TodoTask> todoList) {
        super(context, 0, todoList);
        this.context = context;
        this.todoList = todoList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_todo, parent, false);
        }

        TodoTask currentTask = getItem(position);

        TextView taskNameTextView = convertView.findViewById(R.id.taskNameTextView);
        TextView courseTextView = convertView.findViewById(R.id.courseTextView);
        TextView dueDateTextView = convertView.findViewById(R.id.dueDateTextView);
        CheckBox checkBox = convertView.findViewById(R.id.checkBox);

        taskNameTextView.setText(currentTask.getName());
        courseTextView.setText(currentTask.getCourse());
        dueDateTextView.setText(currentTask.getDueDate());
        checkBox.setChecked(currentTask.isCompleted());

        return convertView;
    }
}