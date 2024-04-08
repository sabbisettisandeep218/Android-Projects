package com.example.demo_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class ViewPageAdapter(
    private val images: List<Int>
): RecyclerView.Adapter<ViewPageAdapter.ViewPagerHolder>(){
    inner class ViewPagerHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.item_view,parent,false)
        return ViewPagerHolder(view)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: ViewPagerHolder, position: Int) {
        val currentImage=images[position]
        holder.itemView.findViewById<ImageView>(R.id.ImgView).setImageResource((currentImage))
    }
}

