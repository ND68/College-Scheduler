package com.example.collegescheduler.ui.classes;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.collegescheduler.R;
import com.example.collegescheduler.databinding.FragmentClassesBinding;

import java.util.ArrayList;

public class ClassesFragment extends Fragment implements AdapterView.OnItemLongClickListener {

    private FragmentClassesBinding binding;
    ListView listView;
    ArrayList<Class> classes;
    ArrayAdapter<Class> adapter;

    public class Class {
        String section;
        String dateStart;
        String dateEnd;
        String time;
        String days;
        String location;
        String instructor;

        public Class(String section, String dateStart, String dateEnd, String time, String days, String location, String instructor) {
            this.section = section;
            this.dateStart = dateStart;
            this.dateEnd = dateEnd;
            this.time = time;
            this.days = days;
            this.location = location;
            this.instructor = instructor;
        }
        @Override
        public String toString() {
            return String.format("Section: %s\nDate Range: %s - %s\nTime: %s\nDays: %s\nLocation: %s\nInstructor: %s",
                    section, dateStart, dateEnd, time, days, location, instructor);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ClassesViewModel homeViewModel =
                new ViewModelProvider(this).get(ClassesViewModel.class);

        binding = FragmentClassesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        listView = root.findViewById(R.id.listViewClasses);
        classes = new ArrayList<>();
        classes.add(new Class("CS2340", "1/8/24", "4/28/24", "12:00",
                "Wednesday", "Skiles 241", "Professor Pedro"));
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, classes);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(this);

        return root;
    }

    public void editButtonPopup(View view, Class classObject, int position) {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.edit_popup, null);
        final EditText sectionInput = alertLayout.findViewById(R.id.editTextSectionCP);
        final EditText dateStartInput = alertLayout.findViewById(R.id.editTextDateStartCP);
        final EditText dateEndInput = alertLayout.findViewById(R.id.editTextDateEndCP);
        final EditText timeInput = alertLayout.findViewById(R.id.editTextTimeCP);
        final EditText daysInput = alertLayout.findViewById(R.id.editTextDaysCP);
        final EditText locationInput = alertLayout.findViewById(R.id.editTextLocationCP);
        final EditText instructorInput = alertLayout.findViewById(R.id.editTextInstructorCP);

        EditText[] inputs = {sectionInput, dateStartInput, dateEndInput, timeInput, daysInput, locationInput, instructorInput};
        String[] classInfo = {classObject.section, classObject.dateStart, classObject.dateEnd, classObject.time, classObject.days,
                        classObject.location, classObject.instructor};

        for (int idx = 0; idx < inputs.length; idx++) {
            inputs[idx].setText(classInfo[idx]);
        }

        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Edit/ Delete Class");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false);
        alert.setNeutralButton("Cancel", (dialog, which) -> Toast.makeText(getContext(), "Action Canceled", Toast.LENGTH_SHORT).show());
        alert.setNegativeButton("Delete", (dialog, which) -> {

            Toast.makeText(getContext(), "Class Deleted", Toast.LENGTH_LONG).show();
            classes.remove(position);
            adapter.notifyDataSetChanged();
        });
        alert.setPositiveButton("Edit", (dialog, which) -> {
            String section = sectionInput.getText().toString().trim();
            String dateStart = dateStartInput.getText().toString().trim();
            String dateEnd = dateEndInput.getText().toString().trim();
            String time = timeInput.getText().toString().trim();
            String days = daysInput.getText().toString().trim();
            String location = locationInput.getText().toString().trim();
            String instructor = instructorInput.getText().toString().trim();

            if (section.isEmpty() || dateStart.isEmpty() || dateEnd.isEmpty() || time.isEmpty() ||
                    days.isEmpty() || location.isEmpty() || instructor.isEmpty()) {
                Toast.makeText(getContext(), "Cannot Have Empty Entries", Toast.LENGTH_LONG).show();
            } else {
                classes.set(position, new Class(section, dateStart, dateEnd, time, days, location, instructor));
                adapter.notifyDataSetChanged();
                Toast.makeText(getContext(), "Class Edited", Toast.LENGTH_LONG).show();
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
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Class classObject = classes.get(position);
        editButtonPopup(view, classObject, position);

        return false;
    }
}