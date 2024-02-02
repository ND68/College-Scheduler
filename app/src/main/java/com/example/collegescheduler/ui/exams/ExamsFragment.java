package com.example.collegescheduler.ui.exams;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.collegescheduler.R;
import com.example.collegescheduler.databinding.FragmentExamsBinding;

import java.util.ArrayList;
import java.util.List;

public class ExamsFragment extends Fragment {

    private FragmentExamsBinding binding;
    private ExamsViewModel examsViewModel;
    private ListView listView;
    private EditText editTextName, editTextDate, editTextCourse, editTextTime, editTextLocation;
    private Button btnAddExam;
    private ArrayAdapter<String> examListAdapter;
    private List<String> examList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        examsViewModel = new ViewModelProvider(this).get(ExamsViewModel.class);

        binding = FragmentExamsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initializeViews(root);

        btnAddExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExam();
            }
        });

        examListAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, examList);
        listView.setAdapter(examListAdapter);

        return root;
    }

    private void initializeViews(View root) {
        editTextName = root.findViewById(R.id.editTextNameE);
        editTextDate = root.findViewById(R.id.editTextDateE);
        editTextCourse = root.findViewById(R.id.editTextCourseE);
        editTextTime = root.findViewById(R.id.editTextTimeE);
        editTextLocation = root.findViewById(R.id.editTextLocationE);
        btnAddExam = root.findViewById(R.id.btnAddExam);
        listView = root.findViewById(R.id.listViewE);
    }

    private void addExam() {
        String examDetails = getExamDetails();
        if (examDetails != null) {
            examList.add(examDetails);
            examListAdapter.notifyDataSetChanged();
            clearInputFields();
        }
    }

    private String getExamDetails() {
        String name = editTextName.getText().toString().trim();
        String date = editTextDate.getText().toString().trim();
        String course = editTextCourse.getText().toString().trim();
        String time = editTextTime.getText().toString().trim();
        String location = editTextLocation.getText().toString().trim();

        if (name.isEmpty() || date.isEmpty() || course.isEmpty() || time.isEmpty() || location.isEmpty()) {
            return null;
        }


        return String.format("Name: %s\nDate: %s\nCourse: %s\nTime: %s\nLocation: %s", name, date, course, time, location);
    }

    private void clearInputFields() {
        editTextName.setText("");
        editTextDate.setText("");
        editTextCourse.setText("");
        editTextTime.setText("");
        editTextLocation.setText("");
    }

    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}