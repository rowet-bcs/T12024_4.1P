package com.example.task41p;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class TaskDetailView extends AppCompatActivity {

    // Declare variables
    String mode;
    int id;
    EditText enterTitle;
    EditText enterDescription;
    TextView enterDueDate;
    Button selectDateButton;
    Button editSaveButton;
    Button deleteTaskButton;
    TextView viewEditTaskText;
    TaskItem selectedTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.task_detail_layout);

        // Link variables to UI elements
        enterTitle = findViewById(R.id.enterTitle);
        enterDescription = findViewById(R.id.enterDescription);
        enterDueDate = findViewById(R.id.enterDueDate);
        editSaveButton = findViewById(R.id.editSaveButton);
        selectDateButton = findViewById(R.id.selectDateButton);
        deleteTaskButton = findViewById(R.id.deleteTaskButton);
        viewEditTaskText = findViewById(R.id.viewEditTaskText);

        // Load task
        loadTask();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void loadTask(){

        // Retrieve selected task and display mode if opened from task list
        id = getIntent().getIntExtra("id", -1);
        mode = getIntent().getStringExtra("mode");

        selectedTask = TaskItem.getTaskById(id);

        // Initialise values of variables if existing task loaded
        if (selectedTask != null){
            enterTitle.setText(selectedTask.getTitle());
            enterDescription.setText(selectedTask.getDescription());
            enterDueDate.setText(selectedTask.getDueDate());
        }

        // Set display mode
        modeSetup();

    }

    public void modeSetup(){
        // Set display elements depending on mode

        switch (mode) {
            case "new":
                enterTitle.setFocusableInTouchMode(true);
                enterDescription.setFocusableInTouchMode(true);
                enterDueDate.setFocusableInTouchMode(true);
                editSaveButton.setText("Create");
                viewEditTaskText.setText("Create New Task");
                deleteTaskButton.setVisibility(View.INVISIBLE);
                selectDateButton.setVisibility(View.VISIBLE);
                break;
            case "edit":
                enterTitle.setFocusableInTouchMode(true);
                enterDescription.setFocusableInTouchMode(true);
                enterDueDate.setFocusableInTouchMode(true);
                editSaveButton.setText("Save");
                viewEditTaskText.setText("Edit Task");
                deleteTaskButton.setVisibility(View.INVISIBLE);
                selectDateButton.setVisibility(View.VISIBLE);
                break;
            case "view":
            default:
                enterTitle.setFocusable(false);
                enterDescription.setFocusable(false);
                enterDueDate.setFocusable(false);
                editSaveButton.setText("Edit");
                viewEditTaskText.setText("Task Details");
                deleteTaskButton.setVisibility(View.VISIBLE);
                selectDateButton.setVisibility(View.INVISIBLE);
                break;
        }
    }

    public void editSaveButtonClick(View view){
        // Perform action based on edit/save button click

        // Retrieve button click action
        String action = editSaveButton.getText().toString();

        switch(action) {
            case "Create":
            case "Save":
                // Check if all entries have been completed
                if (validateEntries()) {
                    // Save/update task
                    saveTask();

                    // Switch to view mode
                    mode = "view";
                    modeSetup();
                }
                break;

            case "Edit":
                // Switch to edit mode
                mode = "edit";
                modeSetup();
                break;
        }
    }

    private boolean validateEntries(){
        // Check if all task entries have been completed before saving

        // Retrieve current values of task elements
        String title = enterTitle.getText().toString();
        String description = enterDescription.getText().toString();
        String dueDate = enterDueDate.getText().toString();

        // Check if entries contain a value
        if (title.isEmpty()){
            Toast.makeText(getApplicationContext(), "Please enter task title", Toast.LENGTH_LONG).show();
            return false;
        }

        if (description.isEmpty()){
            Toast.makeText(getApplicationContext(), "Please enter task description", Toast.LENGTH_LONG).show();
            return false;
        }

        if (dueDate.isEmpty()){
            // Note: no format checking for date as is set by date picker process
            Toast.makeText(getApplicationContext(), "Please enter task due date", Toast.LENGTH_LONG).show();
            return false;
        }

        // All entries have been entered
        return true;
    }

    public void closeTask(View view){
        // Close task and return to previous screen
        finish();
    }

    public void deleteTaskButtonClick(View view){
        // Create popup alert to confirm deletion of task

        // Create AlertDialog Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(TaskDetailView.this);

        // Set alert text
        builder.setMessage("Are you sure you wish to delete this task?");

        // Set Alert Title
        builder.setTitle("Warning");

        // Set value and action of Yes button
        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
            // Continue to delete task
            deleteTask();
        });

        // Set value and action of No button
        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
            // Close popup
            dialog.cancel();
        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();
        // Display alert
        alertDialog.show();
    }

    public void saveTask(){
        // Set db instance
        SQLiteManager db = SQLiteManager.instanceOfDatabase(this);

        // Retrieve value of fields
        String title = enterTitle.getText().toString();
        String description = enterDescription.getText().toString();
        String dueDate = enterDueDate.getText().toString();

        if (selectedTask == null)
        {
            // If new - add to task list and DB
            int id = db.getNextTaskId();
            TaskItem newTask = new TaskItem(id, title, description, dueDate);

            TaskItem.taskList.add(newTask);
            db.addTaskToDatabase(newTask);

            // Set as selected task
            selectedTask = TaskItem.getTaskById(id);

        } else {
            // If existing task, update values
            selectedTask.setTitle(title);
            selectedTask.setDescription(description);
            selectedTask.setDueDate(dueDate);
            db.updateTaskInDB(selectedTask);
        }
    }

    public void deleteTask(){
        // Set db instance
        SQLiteManager db = SQLiteManager.instanceOfDatabase(this);

        // Update deleted flag in task list and database
        selectedTask.setDeleteFlag("Y");
        db.updateTaskInDB(selectedTask);

        // Close task detail display and return to previous view
        finish();

    }

    public void showDatePicker(View view){
        // Set current date for default date view
        Calendar calendar = Calendar.getInstance();
        int todayYear = calendar.get(Calendar.YEAR);
        int todayMonth = calendar.get(Calendar.MONTH);
        int todayDay = calendar.get(Calendar.DAY_OF_MONTH);

        // Create date picker
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // Update display with selected date
                enterDueDate.setText(String.valueOf(year)+ "-"+String.format("%02d", month+1)+"-"+String.format("%02d",day));
            }
        }, todayYear, todayMonth, todayDay );

        // Display date picker
        datePickerDialog.show();
    }
}