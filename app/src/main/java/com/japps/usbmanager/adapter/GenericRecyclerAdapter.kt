package com.japps.usbmanager.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView

abstract class GenericRecyclerAdapter<T>(
    private var itemList: List<T>,
    private val resLayout: Int,
    private val hasRowClick: Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(resLayout, parent, false)
        return object : RecyclerView.ViewHolder(view) {}
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        onBind(holder, position, itemList[position])
        if (hasRowClick) {
            holder.itemView.setOnClickListener {
                onRowClick(holder.adapterPosition, itemList[holder.adapterPosition])
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun updateData(data:List<T>){
        itemList = data
    }

    fun getData():List<T>{
        return itemList
    }

    protected abstract fun onBind(holder: RecyclerView.ViewHolder, position: Int, item: T)

    protected abstract fun onRowClick(position: Int, item: T)
}
