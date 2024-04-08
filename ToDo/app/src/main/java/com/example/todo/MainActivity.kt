package com.example.todo

import android.annotation.SuppressLint
import android.database.Cursor
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.database.TodoContract
import com.example.todo.database.TodoDatabaseHelper
import android.database.Cursor as Cursor1

class MainActivity : AppCompatActivity() {
    private lateinit var dbHelper: TodoDatabaseHelper
    private lateinit var adapter: TodoAdapter
    private val todoList = mutableListOf<Todo>()
    private var isFiltered = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dbHelper = TodoDatabaseHelper(this)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        adapter = TodoAdapter(todoList, dbHelper)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        //Load the data from database
        loadDataFromDatabase()

        val button = findViewById<Button>(R.id.buttonTodo)
        button.setOnClickListener {
            val titleDesc = findViewById<EditText>(R.id.todoET).text.toString()
            if (titleDesc.isNotEmpty()) {
                val todo = Todo(titleDesc, false)
                val newRowId = dbHelper.insertTask(todo)
                if (newRowId.toInt()!=-1) {
                    todoList.add(todo)
                    adapter.notifyItemInserted(todoList.size - 1)
                    findViewById<EditText>(R.id.todoET).text.clear()
                }
            } else {
                Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show()
            }
        }

        val searchView = findViewById<SearchView>(R.id.searchView)
        // Setup search functionality
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    try {
                        search(it)
                    } catch (e: Exception) {
                        e.printStackTrace() //prints the detailed description of the error
                        Toast.makeText(this@MainActivity, "Error occurred", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                return true
            }
        })

        val filterButton = findViewById<ImageButton>(R.id.todofilter)
        filterButton.setOnClickListener {
            try {
                toggleFilter()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun toggleFilter() {
        if (isFiltered) {
            loadDataFromDatabase() // Load all tasks
        } else {
            val completedTasks = todoList.filter { it.isChecked }
            adapter.submitList(completedTasks.toMutableList())
        }
        isFiltered = !isFiltered
    }

    override fun onResume() {
        super.onResume()
        loadDataFromDatabase()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadDataFromDatabase() {
        todoList.clear()
        todoList.addAll(dbHelper.getAllTasks())
        adapter.notifyDataSetChanged()
    }

    @Suppress("NAME_SHADOWING")
    @SuppressLint("Range", "Recycle")
    private fun search(query: String) {
        /*val cursor = dbHelper.readableDatabase.query(
            TodoContract.TodoEntry.TABLE_NAME,
            arrayOf(TodoContract.TodoEntry.COLUMN_TITLE, TodoContract.TodoEntry.COLUMN_IS_CHECKED),
            "${TodoContract.TodoEntry.COLUMN_TITLE} LIKE ?",
            arrayOf("%$query%"),
            null,
            null,
            null
        )*/
        val data:String="Select * from ${TodoContract.TodoEntry.TABLE_NAME}"
        val cursor:Cursor=dbHelper.writableDatabase.rawQuery(data,null)

        while (cursor.moveToNext()){
            val title=cursor.getString(cursor.getColumnIndex(TodoContract.TodoEntry.COLUMN_TITLE))
            val isChecked=cursor.getString(cursor.getColumnIndex(TodoContract.TodoEntry.COLUMN_IS_CHECKED))
        }

        /*val searchResults = mutableListOf<Todo>()

        cursor.use { cursor ->
            while (cursor.moveToNext()) {
                val title =
                    cursor.getString(cursor.getColumnIndex(TodoContract.TodoEntry.COLUMN_TITLE))
                val isChecked =
                    cursor.getInt(cursor.getColumnIndex(TodoContract.TodoEntry.COLUMN_IS_CHECKED)) != 0
                searchResults.add(Todo(title, isChecked))
            }
        }

        adapter.submitList(searchResults)*/
    }


}