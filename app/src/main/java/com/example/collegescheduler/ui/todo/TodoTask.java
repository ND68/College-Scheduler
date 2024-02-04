package com.example.collegescheduler.ui.todo;

public class TodoTask {
    private String name;
    private String course;
    private String dueDate;
    private boolean completed;

    public TodoTask(String name, String course, String dueDate, boolean completed) {
        this.name = name;
        this.course = course;
        this.dueDate = dueDate;
        this.completed = completed;
    }

    public String getName() {
        return name;
    }

    public String getCourse() {
        return course;
    }

    public String getDueDate() {
        return dueDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}