package com.example.todo.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.todo.Todo
import com.example.todo.database.TodoContract.SQL_CREATE_ENTRIES

class TodoDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "todo.db"
        const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${TodoContract.TodoEntry.TABLE_NAME}")
        onCreate(db)
    }

    fun insertTask(todo: Todo): Long {
        val db = writableDatabase   //property inherited by SQLiteOpenHelper class
        val values = ContentValues().apply {        //used to store column values as key-value Pair
            put(TodoContract.TodoEntry.COLUMN_TITLE, todo.title)
            put(TodoContract.TodoEntry.COLUMN_IS_CHECKED, todo.isChecked)
            //put(TodoContract.TodoEntry.COLUMN_DEL_IS_CHECKED, todo.DELIsChecked)
        }
        return db.insert(TodoContract.TodoEntry.TABLE_NAME, null, values)
    }

    fun getAllTasks(): List<Todo> {
        val db = readableDatabase
        val projection = arrayOf(
            TodoContract.TodoEntry.COLUMN_TITLE,
            TodoContract.TodoEntry.COLUMN_IS_CHECKED,
            //TodoContract.TodoEntry.COLUMN_DEL_IS_CHECKED
        )
        val cursor: Cursor = db.query(
            TodoContract.TodoEntry.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null
        )
        val todoList = mutableListOf<Todo>()
        with(cursor) {
            while (moveToNext()) {
                val title = getString(getColumnIndexOrThrow(TodoContract.TodoEntry.COLUMN_TITLE))
                val isChecked =
                    getInt(getColumnIndexOrThrow(TodoContract.TodoEntry.COLUMN_IS_CHECKED)) != 0
                //val DELIsChecked = getInt(getColumnIndexOrThrow(TodoContract.TodoEntry.COLUMN_DEL_IS_CHECKED)) != 0
                todoList.add(Todo(title, isChecked))
            }
        }
        cursor.close()
        return todoList
    }


    fun updateTaskTitle(oldTitle: String, newTitle: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(TodoContract.TodoEntry.COLUMN_TITLE, newTitle)
        }
        val selection = "${TodoContract.TodoEntry.COLUMN_TITLE} LIKE ?"
        val selectionArgs = arrayOf(oldTitle)
        return db.update(TodoContract.TodoEntry.TABLE_NAME, values, selection, selectionArgs)
    }

    /*fun updateTaskCompletion(title: String, isChecked: Boolean) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(TodoContract.TodoEntry.COLUMN_IS_CHECKED, isChecked)
        }
        val selection = "${TodoContract.TodoEntry.COLUMN_TITLE} LIKE ?"
        val selectionArgs = arrayOf(title)
        db.update(TodoContract.TodoEntry.TABLE_NAME, values, selection, selectionArgs)
    }*/


    fun deleteTask(title: String) {
        val db = writableDatabase
        val selection = "${TodoContract.TodoEntry.COLUMN_TITLE} LIKE ?"
        val selectionArgs = arrayOf(title)
        db.delete(TodoContract.TodoEntry.TABLE_NAME, selection, selectionArgs)
    }


}