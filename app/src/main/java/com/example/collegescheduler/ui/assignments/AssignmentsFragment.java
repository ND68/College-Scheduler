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
import java.util.List;

public class AssignmentsFragment extends Fragment implements AssignmentListAdapter.OnAssignmentLongClickListener {

    private FragmentAssignmentsBinding binding;
    private AssignmentsViewModel assignmentsViewModel;
    private RecyclerView recyclerView;
    private EditText editTextTask, editTextDate, editTextClass;
    private Button btnAddAssignment;
    private List<Assignment> assignmentList = new ArrayList<>();
    private AssignmentListAdapter assignmentListAdapter;

    public class Assignment {
        String name;
        String date;
        String course;

        public Assignment(String name, String date, String course) {
            this.name = name;
            this.date = date;
            this.course = course;
        }

        @Override
        public String toString() {
            return String.format("Name: %s\nDate: %s\nCourse: %s", name, date, course);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        assignmentsViewModel =
                new ViewModelProvider(this).get(AssignmentsViewModel.class);

        binding = FragmentAssignmentsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        initializeViews(root);
        if (savedInstanceState != null) {
            // not equal null means there is a past record, so update
            assignmentList = (ArrayList<Assignment>) savedInstanceState.getSerializable("assignmentList");
        } else {
            if (assignmentList != null) {
                // returning from backstack, data is fine, do nothing
            } else {
                // newly created make the new ArrayList
                assignmentList = new ArrayList<>();
            }
        }

        btnAddAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAssignment();
            }
        });

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
        recyclerView = root.findViewById(R.id.recyclerViewA);
    }

    private void addAssignment() {
        Assignment assignmentDetails = getAssignmentDetails();
        if (assignmentDetails != null) {
            assignmentList.add(assignmentDetails);
            assignmentListAdapter.notifyDataSetChanged();
            clearInputFields();
        }
    }

    private Assignment getAssignmentDetails() {
        String name = editTextTask.getText().toString().trim();
        String date = editTextDate.getText().toString().trim();
        String course = editTextClass.getText().toString().trim();

        if (name.isEmpty() || date.isEmpty() || course.isEmpty()) {
            return null;
        }

        return new Assignment(name, date, course);
    }

    private void clearInputFields() {
        editTextTask.setText("");
        editTextDate.setText("");
        editTextClass.setText("");
    }

    @Override
    public void onAssignmentLongClick(View view, int position) {
        Assignment assignmentObject = assignmentList.get(position);
        editButtonPopup(view, assignmentObject, position);
    }

    public void editButtonPopup(View view, Assignment assignmentObject, int position) {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.edit_popup_assignments, null);
        final EditText titleInput = alertLayout.findViewById(R.id.editTextTitleAP);
        final EditText dateInput = alertLayout.findViewById(R.id.editTextDueAP);
        final EditText classInput = alertLayout.findViewById(R.id.editTextClassAP);

        EditText[] inputs = {titleInput, dateInput, classInput};
        String[] assignmentInfo = {assignmentObject.name, assignmentObject.date, assignmentObject.course};

        for (int idx = 0; idx < inputs.length; idx++) {
            inputs[idx].setText(assignmentInfo[idx]);
        }

        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Edit/Delete Assignment");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of the back button and outside touch
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
                assignmentList.set(position, new Assignment(title, date, className));
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
}