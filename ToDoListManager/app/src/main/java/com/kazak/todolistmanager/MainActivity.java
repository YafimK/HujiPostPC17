package com.kazak.todolistmanager;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private ListAdapter todoListAdapter;
    private TodoListSQLHelper todoListSQLHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final ListView listView = (ListView) findViewById(R.id.list);
        final MainActivity self = this;
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder todoTaskBuilder = new AlertDialog.Builder(self);
                todoTaskBuilder.setTitle("Delete task item");
//                View v = (View) view.getParent();
                TextView todoTV = (TextView) view.findViewById(R.id.taskDesc);
                String todoTaskItem = todoTV.getText().toString();
                todoTaskBuilder.setMessage("Delete task: " + todoTaskItem);

                final View view1 = view;
                todoTaskBuilder.setPositiveButton("Confirm deletion of the task", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onDoneButtonClick(view1);
                        updateTodoList();
                    }

                });
                todoTaskBuilder.setNegativeButton("Cancel", null);
                todoTaskBuilder.create().show();
                return false;
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder todoTaskBuilder = new AlertDialog.Builder(self);
                todoTaskBuilder.setTitle("Add Todo Task Item");
                todoTaskBuilder.setMessage("describe the Todo task...");
                final EditText todoET = new EditText(self);
                todoTaskBuilder.setView(todoET);
                todoTaskBuilder.setPositiveButton("Add Task", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String todoTaskInput = todoET.getText().toString();
                        todoListSQLHelper = new TodoListSQLHelper(self);
                        SQLiteDatabase sqLiteDatabase = todoListSQLHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.clear();

                        //write the Todo task input into database table
                        values.put(TodoListSQLHelper.TASK_DB_COLUMN, todoTaskInput);
                        sqLiteDatabase.insertWithOnConflict(TodoListSQLHelper.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);

                        //update the Todo task list UI
                        updateTodoList();

                    }

                });
                todoTaskBuilder.setNegativeButton("Cancel", null);

                todoTaskBuilder.create().show();
            }
        });


    }

    private void updateTodoList() {
        ListView listView = (ListView) findViewById(R.id.list);
        todoListSQLHelper = new TodoListSQLHelper(this);
        SQLiteDatabase sqLiteDatabase = todoListSQLHelper.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(TodoListSQLHelper.TABLE_NAME,
                new String[]{TodoListSQLHelper._ID, TodoListSQLHelper.TASK_DB_COLUMN},
                null, null, null, null, null);

        todoListAdapter = new CoolCursorAdapter(
                this,
                R.layout.task,
                cursor,
                new String[]{TodoListSQLHelper.TASK_DB_COLUMN},
                new int[]{R.id.taskDesc},
                0
        );

        listView.setAdapter(todoListAdapter);



    }


    public void onDoneButtonClick(View view) {
        View v = (View) view.getParent();
        TextView todoTV = (TextView) v.findViewById(R.id.taskDesc);
        String todoTaskItem = todoTV.getText().toString();

        String deleteTodoItemSql = "DELETE FROM " + TodoListSQLHelper.TABLE_NAME +
                " WHERE " + TodoListSQLHelper.TASK_DB_COLUMN + " = '" + todoTaskItem + "'";

        todoListSQLHelper = new TodoListSQLHelper(this);
        SQLiteDatabase sqlDB = todoListSQLHelper.getWritableDatabase();
        sqlDB.execSQL(deleteTodoItemSql);
        updateTodoList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
