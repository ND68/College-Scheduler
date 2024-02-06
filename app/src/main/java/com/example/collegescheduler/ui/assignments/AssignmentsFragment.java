package com.example.collegescheduler.ui.assignments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collegescheduler.R;
import com.example.collegescheduler.databinding.FragmentAssignmentsBinding;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AssignmentsFragment extends Fragment implements AssignmentListAdapter.OnAssignmentLongClickListener {

    private FragmentAssignmentsBinding binding;
    private AssignmentsViewModel assignmentsViewModel;
    private RecyclerView recyclerView;
    private EditText editTextTask, editTextDate, editTextClass;
    private Button btnAddAssignment, btnSortByDueDate, btnSortByCourse;
    private List<AssignmentTask> assignmentList = new ArrayList<>();
    private AssignmentListAdapter assignmentListAdapter;

    public class AssignmentTask {
        private String name;
        private String course;
        private String dueDate;

        public AssignmentTask(String name, String dueDate, String course) {
            this.name = name;
            this.course = course;
            this.dueDate = dueDate;
        }

        public String getName() {
            return name;
        }

        public String getCourse() {
            return course;
        }

        public String getDueDate() {
            return dueDate;
        }

        @Override
        public String toString() {
            return String.format("Name: %s\nDate: %s\nCourse: %s", name, dueDate, course);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        assignmentsViewModel = new ViewModelProvider(this).get(AssignmentsViewModel.class);

        binding = FragmentAssignmentsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        initializeViews(root);

        if (savedInstanceState != null) {
            assignmentList = (ArrayList<AssignmentTask>) savedInstanceState.getSerializable("assignmentList");
        } else {
            assignmentList = new ArrayList<>();
        }

        btnAddAssignment.setOnClickListener(v -> addAssignment());
        btnSortByDueDate.setOnClickListener(v -> sortByDueDate());
        btnSortByCourse.setOnClickListener(v -> sortByCourse());

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        assignmentListAdapter = new AssignmentListAdapter(assignmentList);
        assignmentListAdapter.setOnAssignmentLongClickListener(this);
        recyclerView.setAdapter(assignmentListAdapter);

        return root;
    }

    private void initializeViews(View root) {
        editTextTask = root.findViewById(R.id.editTextTaskA);
        editTextDate = root.findViewById(R.id.editTextDateA);
        editTextClass = root.findViewById(R.id.editTextClassA);
        btnAddAssignment = root.findViewById(R.id.btnAddAssignments);
        btnSortByDueDate = root.findViewById(R.id.button);
        btnSortByCourse = root.findViewById(R.id.button3);
        recyclerView = root.findViewById(R.id.recyclerViewA);
    }

    private void addAssignment() {
        AssignmentTask assignmentDetails = getAssignmentDetails();
        if (assignmentDetails != null) {
            assignmentList.add(assignmentDetails);
            assignmentListAdapter.notifyDataSetChanged();
            clearInputFields();
        }
    }

    private AssignmentTask getAssignmentDetails() {
        String name = editTextTask.getText().toString().trim();
        String date = editTextDate.getText().toString().trim();
        String course = editTextClass.getText().toString().trim();

        if (name.isEmpty() || date.isEmpty() || course.isEmpty()) {
            return null;
        }

        return new AssignmentTask(name, date, course);
    }

    private void clearInputFields() {
        editTextTask.setText("");
        editTextDate.setText("");
        editTextClass.setText("");
    }

    @Override
    public void onAssignmentLongClick(View view, int position) {
        AssignmentTask assignmentObject = assignmentList.get(position);
        editButtonPopup(view, assignmentObject, position);
    }

    public void editButtonPopup(View view, AssignmentTask assignmentObject, int position) {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.edit_popup_assignments, null);
        final EditText titleInput = alertLayout.findViewById(R.id.editTextTitleAP);
        final EditText dateInput = alertLayout.findViewById(R.id.editTextDueAP);
        final EditText classInput = alertLayout.findViewById(R.id.editTextClassAP);

        EditText[] inputs = {titleInput, dateInput, classInput};
        String[] assignmentInfo = {assignmentObject.name, assignmentObject.dueDate, assignmentObject.course};

        for (int idx = 0; idx < inputs.length; idx++) {
            inputs[idx].setText(assignmentInfo[idx]);
        }

        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Edit/Delete Assignment");
        alert.setView(alertLayout);
        alert.setCancelable(false);
        alert.setNeutralButton("Cancel", (dialog, which) -> Toast.makeText(getContext(), "Action Canceled", Toast.LENGTH_SHORT).show());
        alert.setNegativeButton("Delete", (dialog, which) -> {
            Toast.makeText(getContext(), "Assignment Deleted", Toast.LENGTH_LONG).show();
            assignmentList.remove(position);
            assignmentListAdapter.notifyDataSetChanged();
        });
        alert.setPositiveButton("Edit", (dialog, which) -> {
            String title = titleInput.getText().toString().trim();
            String date = dateInput.getText().toString().trim();
            String className = classInput.getText().toString().trim();

            if (title.isEmpty() || date.isEmpty() || className.isEmpty()) {
                Toast.makeText(getContext(), "Cannot Have Empty Entries", Toast.LENGTH_LONG).show();
            } else {
                assignmentList.set(position, new AssignmentTask(title, date, className));
                assignmentListAdapter.notifyDataSetChanged();
                Toast.makeText(getContext(), "Assignment Edited", Toast.LENGTH_LONG).show();
            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable("assignmentList", (Serializable) assignmentList);
    }

    private void sortByDueDate() {
        Collections.sort(assignmentList, new Comparator<AssignmentTask>() {
            public int compare(AssignmentTask task1, AssignmentTask task2) {
                return task1.getDueDate().compareTo(task2.getDueDate());
            }
        });
        assignmentListAdapter.notifyDataSetChanged();
    }

    private void sortByCourse() {
        Collections.sort(assignmentList, new Comparator<AssignmentTask>() {
            @Override
            public int compare(AssignmentTask task1, AssignmentTask task2) {
                return task1.getCourse().compareTo(task2.getCourse());
            }
        });
        assignmentListAdapter.notifyDataSetChanged();
    }
}