package com.example.collegescheduler.ui.exams;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collegescheduler.R;
import com.example.collegescheduler.databinding.FragmentExamsBinding;
import com.example.collegescheduler.ui.classes.ClassesFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ExamsFragment extends Fragment {

    private FragmentExamsBinding binding;
    private ExamsViewModel examsViewModel;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private EditText editTextName, editTextDate, editTextCourse, editTextTime, editTextLocation;
    private Button btnAddExam;
    private ExamListAdapter examListAdapter;
    private List<Exam> examList;

    public class Exam {
        String name;
        String date;
        String course;
        String time;
        String location;

        public Exam(String name, String date, String course, String time, String location) {
            this.name = name;
            this.date = date;
            this.course = course;
            this.time = time;
            this.location = location;
        }

        @Override
        public String toString() {
            return String.format("Name: %s\nDate: %s\nCourse: %s\nTime: %s\nLocation: %s",
                    name, date, course, time, location);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        examsViewModel = new ViewModelProvider(this).get(ExamsViewModel.class);

        binding = FragmentExamsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        if (savedInstanceState != null) {
            //not equal null means there is a past record, so update
            examList = (ArrayList<Exam>) savedInstanceState.getSerializable("examList");
        } else {
            if (examList != null) {
                //returning from backstack, data is fine, do nothing
            } else {
                //newly created make the new arraylist
                examList = new ArrayList<>();
            }
        }
        initializeViews(root);
        btnAddExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExam();
            }
        });

        examListAdapter = new ExamListAdapter(examList);
        recyclerView.setAdapter(examListAdapter);

        return root;
    }

    private void initializeViews(View root) {
        editTextName = root.findViewById(R.id.editTextNameE);
        editTextDate = root.findViewById(R.id.editTextDateE);
        editTextCourse = root.findViewById(R.id.editTextCourseE);
        editTextTime = root.findViewById(R.id.editTextTimeE);
        editTextLocation = root.findViewById(R.id.editTextLocationE);
        btnAddExam = root.findViewById(R.id.btnAddExam);
        recyclerView = root.findViewById(R.id.recyclerViewE);
        layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);
    }

    private void addExam() {
        Exam examDetails = getExamDetails();
        if (examDetails != null) {
            examList.add(examDetails);
            examListAdapter.notifyDataSetChanged();
            clearInputFields();
        }
    }

    private Exam getExamDetails() {
        String name = editTextName.getText().toString().trim();
        String date = editTextDate.getText().toString().trim();
        String course = editTextCourse.getText().toString().trim();
        String time = editTextTime.getText().toString().trim();
        String location = editTextLocation.getText().toString().trim();

        if (name.isEmpty() || date.isEmpty() || course.isEmpty() || time.isEmpty() || location.isEmpty()) {
            return null;
        }

        return new Exam(name, date, course, time, location);
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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable("examList", (Serializable) examList);
    }
}