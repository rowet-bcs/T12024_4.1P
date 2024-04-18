package com.example.task41p;

import java.util.ArrayList;

public class TaskItem {
    // Custom class to store task details
    public static ArrayList<TaskItem> taskList = new ArrayList<>();
    private int id;
    private String title;
    private String description;
    private String dueDate;
    private String deleteFlag;

    public TaskItem(int id, String title, String description, String dueDate, String deleteFlag){
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.deleteFlag = deleteFlag;
    }

    public TaskItem(int id, String title, String description, String dueDate){
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.deleteFlag = "N"; // Create with default deleteFlag value
    }

    public int getId() {return id;}
    public String getTitle() { return title;}
    public String getDescription() {return description;}
    public String getDueDate() {return dueDate;}
    public String getDeleteFlag() {return deleteFlag;}
    public static TaskItem getTaskById(int id){
        for (TaskItem task : taskList){
            if(task.getId() == id){
                return task;
            }
        }
        return null;
    }

    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setDueDate(String dueDate){ this.dueDate = dueDate; }
    public void setDeleteFlag(String deleteFlag) {this.deleteFlag = deleteFlag;}
}


