package com.example.collegescheduler.ui.todo;


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
        } else {
            if (todoList != null) {
                //returning from backstack, data is fine, do nothing
            } else {
                //newly created make the new arraylist
                todoList = new ArrayList<>();
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
                // Handle the logic to edit or delete the task
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
        todoList.get(position).setCompleted(isChecked);
        // Handle the logic to remove the task or update its status as needed
        if (isChecked) {
            todoList.remove(position);
            todoAdapter.notifyDataSetChanged();
        }
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
                if (task1.isCompleted() && !task2.isCompleted()) {
                    return 1;
                } else if (!task1.isCompleted() && task2.isCompleted()) {
                    return -1;
                }
                return 0;
            }
        });
        todoAdapter.notifyDataSetChanged();
    }

    private void clearEditTexts(EditText... editTexts) {
        for (EditText editText : editTexts) {
            editText.getText().clear();
        }
    }
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable("todoList", todoList);
    }
}