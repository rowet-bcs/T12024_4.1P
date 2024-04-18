// Project completed with assistance from:
// https://stackoverflow.com/questions/660151/how-to-replicate-androideditable-false-in-code
// https://www.geeksforgeeks.org/how-to-create-an-alert-dialog-box-in-android/
// https://www.youtube.com/watch?v=4k1ZMpO9Zn0
// https://www.youtube.com/watch?v=guTycx3L9I4
// https://www.youtube.com/watch?v=TRfDbqmhSDQ


package com.example.task41p;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        initialiseDatabase();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initialiseDatabase(){
        // On first load, create or retrieve existing database
        SQLiteManager db = SQLiteManager.instanceOfDatabase(this);
        db.populateTaskListArray();
    }

    public void createNewTask(View view){
        // Launch create new task activity in new mode
        Intent createNewTask = new Intent(this, TaskDetailView.class);
        createNewTask.putExtra("mode", "new");
        startActivity(createNewTask);
    }

    public void viewList(View view){
        // Launch list view activity
        Intent viewTaskList = new Intent(this, TaskListView.class);
        startActivity(viewTaskList);
    }

    public void close(View view) {
        // Close app
        finish();
    }
}