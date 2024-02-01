package com.example.collegescheduler.ui.assignments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.collegescheduler.R;
import com.example.collegescheduler.databinding.FragmentAssignmentsBinding;
import com.example.collegescheduler.ui.exams.ExamsViewModel;

import java.util.ArrayList;
import java.util.List;

public class AssignmentsFragment extends Fragment {

    private FragmentAssignmentsBinding binding;
    private AssignmentsViewModel assignmentsViewModel;
    private ListView listView;
    private EditText editTextTask, editTextDate, editTextClass;
    private Button btnAddAssignment;
    private ArrayAdapter<String> assignmentListAdapter;
    private List<String> assignmentList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AssignmentsViewModel galleryViewModel =
                new ViewModelProvider(this).get(AssignmentsViewModel.class);

        binding = FragmentAssignmentsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        initializeViews(root);

        btnAddAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAssignment();
            }
        });

        assignmentListAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, assignmentList);
        listView.setAdapter(assignmentListAdapter);
        final TextView textView = binding.textViewA;
        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }
    private void initializeViews(View root) {
        editTextTask = root.findViewById(R.id.editTextTaskA);
        editTextDate = root.findViewById(R.id.editTextDateA);
        editTextClass = root.findViewById(R.id.editTextClassA);

        btnAddAssignment = root.findViewById(R.id.btnAddAssignments);
        listView = root.findViewById(R.id.listViewA);
    }
    private void addAssignment() {
        String assignmentDetails = getAssignmentDetails();
        if (assignmentDetails != null) {
            assignmentList.add(assignmentDetails);
            assignmentListAdapter.notifyDataSetChanged();
            clearInputFields();
        }
    }
    private String getAssignmentDetails() {
        String name = editTextTask.getText().toString().trim();
        String date = editTextDate.getText().toString().trim();
        String course = editTextClass.getText().toString().trim();


        if (name.isEmpty() || date.isEmpty() || course.isEmpty()) {
            return null;
        }


        return String.format("Name: %s\nDate: %s\nCourse: %s", name, date, course);
    }
    private void clearInputFields() {
        editTextTask.setText("");
        editTextDate.setText("");
        editTextClass.setText("");
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}