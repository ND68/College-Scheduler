package com.example.collegescheduler.ui.assignments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collegescheduler.R;

import java.util.List;

public class AssignmentListAdapter extends RecyclerView.Adapter<AssignmentListAdapter.AssignmentViewHolder> {

    private List<AssignmentsFragment.AssignmentTask> assignmentList;
    private OnAssignmentLongClickListener assignmentLongClickListener;

    public AssignmentListAdapter(List<AssignmentsFragment.AssignmentTask> assignmentList) {
        this.assignmentList = assignmentList;
    }

    public interface OnAssignmentLongClickListener {
        void onAssignmentLongClick(View view, int position);
    }

    public void setOnAssignmentLongClickListener(OnAssignmentLongClickListener listener) {
        this.assignmentLongClickListener = listener;
    }

    @NonNull
    @Override
    public AssignmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_assignment, parent, false);
        return new AssignmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssignmentViewHolder holder, int position) {
        holder.bind(assignmentList.get(position));
    }

    @Override
    public int getItemCount() {
        return assignmentList.size();
    }

    public class AssignmentViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        TextView textViewAssignment;

        public AssignmentViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAssignment = itemView.findViewById(R.id.textViewAssignment);
            itemView.setOnLongClickListener(this);
        }

        public void bind(AssignmentsFragment.AssignmentTask assignment) {
            textViewAssignment.setText(assignment.toString());
        }

        @Override
        public boolean onLongClick(View v) {
            if (assignmentLongClickListener != null) {
                assignmentLongClickListener.onAssignmentLongClick(v, getAdapterPosition());
            }
            return true;
        }
    }
}