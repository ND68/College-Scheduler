package com.example.collegescheduler.ui.exams;

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

import com.example.collegescheduler.databinding.FragmentExamsBinding;

import java.util.ArrayList;
import java.util.List;

public class ExamsFragment extends Fragment {

    private FragmentExamsBinding binding;
    private ExamsViewModel examsViewModel;
    private ListView listView;
    private EditText editTextName, editTextData, editTextCourse, editTextTime, editTextLocation;
    private Button btnAddTask;
    private ArrayAdapter<String> taskListAdapter;
    private List<String> taskList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ExamsViewModel slideshowViewModel =
                new ViewModelProvider(this).get(ExamsViewModel.class);

        binding = FragmentExamsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textViewE;
        slideshowViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}