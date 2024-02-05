package com.example.collegescheduler.ui.exams;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collegescheduler.R;

import java.util.List;

public class ExamListAdapter extends RecyclerView.Adapter<ExamListAdapter.ExamViewHolder> {

    private List<ExamsFragment.Exam> examList;

    public ExamListAdapter(List<ExamsFragment.Exam> examList) {
        this.examList = examList;
    }

    @NonNull
    @Override
    public ExamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exam, parent, false);
        return new ExamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamViewHolder holder, int position) {
        holder.bind(examList.get(position));
    }

    @Override
    public int getItemCount() {
        return examList.size();
    }

    public static class ExamViewHolder extends RecyclerView.ViewHolder {
        TextView textViewExam;

        public ExamViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewExam = itemView.findViewById(R.id.textViewExam);
        }

        public void bind(ExamsFragment.Exam examDetails) {
            textViewExam.setText(examDetails.toString());
        }
    }
}
