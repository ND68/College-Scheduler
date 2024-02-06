package com.example.collegescheduler.ui.todo;


import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.collegescheduler.R;
import com.example.collegescheduler.ui.classes.ClassesFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TodoFragment extends Fragment {

    private ArrayList<TodoTask> todoList;
    private ArrayList<TodoTask> completedList;
    private TodoListAdapter todoAdapter;
    private ListView listView;

    public static TodoFragment newInstance() {
        return new TodoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_todo, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            //not equal null means there is a past record, so update
            todoList =(ArrayList<TodoTask>) savedInstanceState.getSerializable("todoList");
            completedList =(ArrayList<TodoTask>) savedInstanceState.getSerializable("completedList");
        } else {
            if (todoList != null) {
                //returning from backstack, data is fine, do nothing
            } else {
                //newly created make the new arraylist
                todoList = new ArrayList<>();
                completedList = new ArrayList<>();
            }
        }

        todoAdapter = new TodoListAdapter(getActivity(), todoList);
        listView = getView().findViewById(R.id.listViewT);
        listView.setAdapter(todoAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox checkBox = view.findViewById(R.id.checkBox);
                checkBox.setChecked(!checkBox.isChecked());
                updateTaskStatus(position, checkBox.isChecked());
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "Task name cannot be empty", Toast.LENGTH_SHORT).show();
                TodoTask todoObject = todoList.get(position);
                editButtonPopup(view, todoObject, position);
                return true;
            }
        });

        getView().findViewById(R.id.buttonAddTask).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask();
            }
        });

        getView().findViewById(R.id.buttonSortByDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortByDueDate();
            }
        });

        getView().findViewById(R.id.buttonSortByCourse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortByCourse();
            }
        });

        getView().findViewById(R.id.buttonSortByStatus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortByStatus();
            }
        });
    }

    private void addTask() {
        EditText taskNameEditText = getView().findViewById(R.id.editTextTaskT);
        EditText courseEditText = getView().findViewById(R.id.editTextCourseT);
        EditText dueDateEditText = getView().findViewById(R.id.editTextDateT);

        String taskName = taskNameEditText.getText().toString().trim();
        String course = courseEditText.getText().toString().trim();
        String dueDate = dueDateEditText.getText().toString().trim();

        if (!taskName.isEmpty()) {
            TodoTask newTask = new TodoTask(taskName, course, dueDate, false);
            todoList.add(newTask);
            todoAdapter.notifyDataSetChanged();
            clearEditTexts(taskNameEditText, courseEditText, dueDateEditText);
        } else {
            Toast.makeText(getActivity(), "Task name cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateTaskStatus(int position, boolean isChecked) {
        TodoTask task = todoList.get(position);
        task.setCompleted(isChecked);
        todoAdapter.notifyDataSetChanged();
    }

    private void sortByDueDate() {
        Collections.sort(todoList, new Comparator<TodoTask>() {
            @Override
            public int compare(TodoTask task1, TodoTask task2) {
                return task1.getDueDate().compareTo(task2.getDueDate());
            }
        });
        todoAdapter.notifyDataSetChanged();
    }

    private void sortByCourse() {
        Collections.sort(todoList, new Comparator<TodoTask>() {
            @Override
            public int compare(TodoTask task1, TodoTask task2) {
                return task1.getCourse().compareTo(task2.getCourse());
            }
        });
        todoAdapter.notifyDataSetChanged();
    }


    private void sortByStatus() {
        Collections.sort(todoList, new Comparator<TodoTask>() {
            @Override
            public int compare(TodoTask task1, TodoTask task2) {
                // Incomplete tasks should come before completed tasks
                if (!task1.isCompleted() && task2.isCompleted()) {
                    return -1; // task1 comes before task2
                } else if (task1.isCompleted() && !task2.isCompleted()) {
                    return 1; // task2 comes before task1
                } else {
                    return 0; // no change in order
                }
            }
        });

        // Notify the adapter of the change
        todoAdapter.notifyDataSetChanged();
    }

    private void clearEditTexts(EditText... editTexts) {
        for (EditText editText : editTexts) {
            editText.getText().clear();
        }
    }

    public void editButtonPopup(View view, TodoTask todoObject, int position) {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.edit_popup_todos, null);
        final EditText nameInput = alertLayout.findViewById(R.id.editTextNameTP);
        final EditText courseInput = alertLayout.findViewById(R.id.editTextCourseTP);
        final EditText dateInput = alertLayout.findViewById(R.id.editTextDateTP);

        EditText[] inputs = {nameInput, courseInput, dateInput};
        String[] todoInfo = {todoObject.getName(), todoObject.getCourse(), todoObject.getDueDate()};

        for (int idx = 0; idx < inputs.length; idx++) {
            inputs[idx].setText(todoInfo[idx]);
        }

        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Edit/ Delete Task");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false);
        alert.setNeutralButton("Cancel", (dialog, which) -> Toast.makeText(getContext(), "Action Canceled", Toast.LENGTH_SHORT).show());
        alert.setNegativeButton("Delete", (dialog, which) -> {

            Toast.makeText(getContext(), "Task Deleted", Toast.LENGTH_LONG).show();
            todoList.remove(position);
            todoAdapter.notifyDataSetChanged();
        });
        alert.setPositiveButton("Edit", (dialog, which) -> {
            String name = nameInput.getText().toString().trim();
            String course = courseInput.getText().toString().trim();
            String date = dateInput.getText().toString().trim();

            if (name.isEmpty() || course.isEmpty() || date.isEmpty()) {
                Toast.makeText(getContext(), "Cannot Have Empty Entries", Toast.LENGTH_LONG).show();
            } else {
                todoList.set(position, new TodoTask(name, course, date, false));
                todoAdapter.notifyDataSetChanged();
                Toast.makeText(getContext(), "Class Edited", Toast.LENGTH_LONG).show();
            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable("todoList", todoList);
        savedInstanceState.putSerializable("completedList", completedList);
    }
}