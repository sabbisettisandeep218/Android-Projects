package com.example.rv_app

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RvAdapter(
    private val rvItems:MutableList<Rvdata>,
    private val dbhelper:DatabaseHelper
) : RecyclerView.Adapter<RvAdapter.RvViewHolder>() {

    class RvViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RvViewHolder {
        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.items,parent,false)
        return RvViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return rvItems.size
    }


    override fun onBindViewHolder(holder: RvViewHolder, position: Int) {
        val currentItem=rvItems[position]
        val rvDesc=holder.itemView.findViewById<TextView>(R.id.rvtext)
        rvDesc.text=currentItem.desc
        val delbtn=holder.itemView.findViewById<ImageButton>(R.id.delbtn)
        delbtn.isClickable=currentItem.delIsChecked


        rvDesc.setOnClickListener {
            val context = holder.itemView.context
            val editText = EditText(context)
            editText.setText(currentItem.desc)

            AlertDialog.Builder(context).setTitle("Edit the Task").setView(editText)
                .setPositiveButton("Save") { dialog, _ ->   //dialog box
                    val newDesc = editText.text.toString()
                    if (newDesc.isNotEmpty()) {
                        dbhelper.updateData(currentItem.desc,newDesc)
                        currentItem.desc = newDesc
                        rvDesc.text = newDesc
                    }
                    dialog.dismiss()    //close dialog
                }.setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }.show()
        }

        delbtn.setOnClickListener{
            val currentPosition=holder.adapterPosition
            if(currentPosition!=RecyclerView.NO_POSITION){
                dbhelper.deleteData(rvItems[currentPosition].desc)
                rvItems.removeAt(currentPosition)
                notifyItemRemoved(currentPosition)
            }

        }

    }


}



