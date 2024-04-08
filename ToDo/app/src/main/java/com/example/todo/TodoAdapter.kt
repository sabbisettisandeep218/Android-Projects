package com.example.todo

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.database.TodoDatabaseHelper


@Suppress("NAME_SHADOWING", "DEPRECATION")
class TodoAdapter(
    private val todoList: MutableList<Todo>,
    private val dbHelper: TodoDatabaseHelper,
) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    inner class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface OnCheckedChangeListener {
        fun onCheckedChange(todo: Todo, isChecked: Boolean)
    }

    private var checkedChangeListener: OnCheckedChangeListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoAdapter.TodoViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        return TodoViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return todoList.size  //Size of ToDoList
    }


    @SuppressLint("CutPasteId")
    override fun onBindViewHolder(holder: TodoAdapter.TodoViewHolder, position: Int) {

        val currentItem = todoList[position]
        holder.itemView.apply {

            val titleTextView = findViewById<TextView>(R.id.todoTitle)
            titleTextView.text = currentItem.title

            val checkBoxid = findViewById<CheckBox>(R.id.todoCb1)
            checkBoxid.isChecked = currentItem.isChecked
            checkBoxid.setOnCheckedChangeListener { _, isChecked ->
                checkedChangeListener?.onCheckedChange(currentItem, isChecked)
            }

            val title = findViewById<TextView>(R.id.todoTitle)
            title.setOnClickListener {
                val context = holder.itemView.context
                val editText = EditText(context)
                editText.setText(currentItem.title)

                AlertDialog.Builder(context).setTitle("Edit Todo").setView(editText)
                    .setPositiveButton("Save") { dialog, _ ->
                        val newTitle = editText.text.toString()
                        if (newTitle.isNotEmpty()) {
                            dbHelper.updateTaskTitle(currentItem.title, newTitle)
                            currentItem.title = newTitle
                            title.text = newTitle
                        }
                        dialog.dismiss()
                    }.setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }.show()
            }


            val delButton = findViewById<ImageButton>(R.id.DelBtn)
            delButton.setOnClickListener {
                val position = holder.adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    dbHelper.deleteTask(todoList[position].title)
                    todoList.removeAt(position)
                    notifyItemRemoved(position)
                }
            }


        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(todoList: MutableList<Todo>) {
        this.todoList.clear()
        this.todoList.addAll(todoList)
        notifyDataSetChanged()
    }


}