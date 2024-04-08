package com.example.todo.database

import android.provider.BaseColumns

object TodoContract {
    class TodoEntry : BaseColumns {
        companion object {
            const val TABLE_NAME = "ToDo_List"
            const val COLUMN_TITLE = "title"
            const val COLUMN_IS_CHECKED = "isChecked"
            //const val COLUMN_DEL_IS_CHECKED = "DELIsChecked"
        }
    }

    const val SQL_CREATE_ENTRIES =
        "CREATE TABLE ${TodoEntry.TABLE_NAME} (" +
                //"${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${TodoEntry.COLUMN_TITLE} TEXT," +
                "${TodoEntry.COLUMN_IS_CHECKED} Boolean)"
}