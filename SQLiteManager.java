package com.example.task41p;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;

public class SQLiteManager extends SQLiteOpenHelper{
    // Custom class to set up SQLite database for persistent task storage
    private static SQLiteManager db;
    // Set database details
    private static final String DATABASE_NAME = "TaskDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "Tasks";

    // Set database fields
    private static final String ID_FIELD = "id";
    private static final String TITLE_FIELD = "title";
    private static final String DESC_FIELD = "description";
    private static final String DUE_DATE_FIELD = "dueDate";
    private static final String DELETED_FLAG_FIELD = "deletedFlag";

    public SQLiteManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static SQLiteManager instanceOfDatabase(Context context){
        // Return database instance, creating a new database on first call
        if(db == null){
            db = new SQLiteManager(context);
        }
        return db;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create database table
        StringBuilder sql;
        sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(TABLE_NAME)
                .append(" (")
                .append(ID_FIELD)
                .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(TITLE_FIELD)
                .append(" TEXT, ")
                .append(DESC_FIELD)
                .append(" TEXT, ")
                .append(DUE_DATE_FIELD)
                .append(" TEXT, ")
                .append(DELETED_FLAG_FIELD)
                .append(" TEXT)");

        db.execSQL(sql.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Standard function to recreate table on upgrade
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addTaskToDatabase(TaskItem task){
        // Retrieve task details and save as a new record to database
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE_FIELD, task.getTitle());
        contentValues.put(DESC_FIELD, task.getDescription());
        contentValues.put(DUE_DATE_FIELD, task.getDueDate());
        contentValues.put(DELETED_FLAG_FIELD, task.getDeleteFlag());

        db.insert(TABLE_NAME, null, contentValues);
    }

    public void populateTaskListArray(){
        // Populate task list from database
        SQLiteDatabase db = this.getReadableDatabase();

        // Empty task list before repopulating
        TaskItem.taskList.clear();

        // Retrieve current tasks from database, sorted by due date
        try (Cursor result = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + DELETED_FLAG_FIELD + " = 'N' order by " + DUE_DATE_FIELD, null)){
            if(result.getCount() != 0){
                while (result.moveToNext()){
                    int id = result.getInt(0);
                    String title = result.getString(1);
                    String desc = result.getString(2);
                    String dueDate = result.getString(3);
                    String deleteFlag = result.getString(4);

                    // Create new task from database values and add to task list
                    TaskItem task = new TaskItem(id,title,desc,dueDate,deleteFlag);
                    TaskItem.taskList.add(task);
                }
            }
        }
    }

    public int getNextTaskId(){
        // Query database to set the next Id number
        SQLiteDatabase db = this.getReadableDatabase();

        try (Cursor result = db.rawQuery("SELECT * FROM " + TABLE_NAME, null)){
            return result.getCount();
        }
    }

    public void updateTaskInDB(TaskItem task){
        // Update existing task in database
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE_FIELD, task.getTitle());
        contentValues.put(DESC_FIELD, task.getDescription());
        contentValues.put(DUE_DATE_FIELD, task.getDueDate());
        contentValues.put(DELETED_FLAG_FIELD, task.getDeleteFlag());

        db.update(TABLE_NAME, contentValues, ID_FIELD + " =?", new String[]{String.valueOf(task.getId())});
    }
}
