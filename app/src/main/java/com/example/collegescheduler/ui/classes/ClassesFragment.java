package com.example.collegescheduler.ui.classes;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.collegescheduler.R;
import com.example.collegescheduler.databinding.FragmentClassesBinding;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class ClassesFragment extends Fragment implements AdapterView.OnItemLongClickListener {

    private FragmentClassesBinding binding;
    ListView listView;
    ArrayList<String> items;
    ArrayAdapter<String> adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ClassesViewModel homeViewModel =
                new ViewModelProvider(this).get(ClassesViewModel.class);

        binding = FragmentClassesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        listView = root.findViewById(R.id.listViewClasses);
        items = new ArrayList<>();
        items.add("CS 2340\n1/8/24 - 4/28/24");
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(this);

        return root;
    }

    public void editButtonPopup(View view, String details, int position) {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.edit_popup, null);
        final EditText sectionInput = alertLayout.findViewById(R.id.editTextSectionCP);
        final EditText dateStartInput = alertLayout.findViewById(R.id.editTextDateStartCP);
        final EditText dateEndInput = alertLayout.findViewById(R.id.editTextDateEndCP);
        final EditText timeInput = alertLayout.findViewById(R.id.editTextTimeCP);
        final EditText daysInput = alertLayout.findViewById(R.id.editTextDaysCP);
        final EditText locationInput = alertLayout.findViewById(R.id.editTextLocationCP);
        final EditText instructorInput = alertLayout.findViewById(R.id.editTextInstructorCP);

        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Edit Class");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false);
        alert.setNegativeButton("Cancel", (dialog, which) -> Toast.makeText(getContext(), "Cancel clicked", Toast.LENGTH_SHORT).show());

        alert.setPositiveButton("Edit", (dialog, which) -> {
            String user = sectionInput.getText().toString();
            String pass = instructorInput.getText().toString();
            Toast.makeText(getContext(), "Class Edited", Toast.LENGTH_LONG).show();
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
        String classDetails = items.get(position);
        editButtonPopup(view, classDetails, position);

        return false;
    }
}