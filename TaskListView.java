package com.example.task41p;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TaskListView extends AppCompatActivity {
    // Declare variables
    ListView taskListView;
    ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.task_list_layout);

        // Link variables to UI elements
        taskListView = findViewById(R.id.listview);

        // Load values and
        loadFromDB();
        setTaskAdapter();
        setOnClickListener();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setTaskAdapter() {
        // Set list adapter to display elements of taskList
        listAdapter = new ListAdapter(TaskListView.this, TaskItem.taskList);
        taskListView.setAdapter(listAdapter);
    }

    private void setOnClickListener() {
        // Define on click action for task list
        taskListView.setClickable(true);

        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Launch task detail activity in view mode
                Intent intent = new Intent(TaskListView.this, TaskDetailView.class);
                TaskItem task = TaskItem.taskList.get(position);
                intent.putExtra("mode","view");
                intent.putExtra("id", task.getId());
                startActivity(intent);
            }
        });
    }

    public void home(View view){
        // Close activity and return to home screen
        finish();
    }

    public void createNewTask(View view) {
        // Launch create new task activity in new mode
        Intent createNewTask = new Intent(this, TaskDetailView.class);
        createNewTask.putExtra("mode", "new");
        startActivity(createNewTask);
    }

    private void loadFromDB(){
        // Refresh data from database
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        sqLiteManager.populateTaskListArray();
    }

    @Override
    protected void onResume(){
        super.onResume();
        // Refresh from database and update task list
        loadFromDB();
        setTaskAdapter();
    }
}