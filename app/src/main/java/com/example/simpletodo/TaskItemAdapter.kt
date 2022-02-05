package com.example.simpletodo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/*
* A bridge that tells the recyclerView how to display the data
 */
class TaskItemAdapter(
    val listOfItems: List<String>,
    val longClickListener: OnLongClickListener,
    val onClickListener: onSingleClickListener) :
    RecyclerView.Adapter<TaskItemAdapter.ViewHolder>(){

    //long click to remove item
    interface OnLongClickListener{
        fun onItemLongClicked(position: Int)
    }

    //one click to edit item
    interface onSingleClickListener{
        fun onItemClicked(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: String = listOfItems.get(position)
        holder.textView.text = item
    }

    override fun getItemCount(): Int {
        return listOfItems.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Store reference to ements in our layout view
        var textView: TextView

        //init for long click
        init {
            textView = itemView.findViewById(android.R.id.text1)

            itemView.setOnLongClickListener {
                //Log.i("YTL", "Long clicked on item$adapterPosition")
                longClickListener.onItemLongClicked(adapterPosition)
                true
            }
        }

        //init for one click
        init {
            textView = itemView.findViewById(android.R.id.text1)
            itemView.setOnClickListener {
                onClickListener.onItemClicked(adapterPosition)
                true
            }
        }
    }

}