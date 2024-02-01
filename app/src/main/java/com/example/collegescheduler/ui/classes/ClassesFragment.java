package com.example.collegescheduler.ui.classes;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.collegescheduler.R;
import com.example.collegescheduler.databinding.FragmentClassesBinding;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class ClassesFragment extends Fragment {

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

        Button b = (Button) root.findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editButtonPopup(v);
            }
        });


        return root;
    }

    public void editButtonPopup(View view) {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.edit_popup, null);
        final TextInputEditText etUsername = alertLayout.findViewById(R.id.tiet_username);
        final TextInputEditText etPassword = alertLayout.findViewById(R.id.tiet_password);

        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Login");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false);
        alert.setNegativeButton("Cancel", (dialog, which) -> Toast.makeText(getContext(), "Cancel clicked", Toast.LENGTH_SHORT).show());

        alert.setPositiveButton("Done", (dialog, which) -> {
            String user = etUsername.getText().toString();
            String pass = etPassword.getText().toString();
            Toast.makeText(getContext(), "Username: " + user + " Password: " + pass, Toast.LENGTH_LONG).show();
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}