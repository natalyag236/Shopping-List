package com.example.project


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ItemAdapter(private val items: List<Item>, private val itemClickListener: ItemFragmentFragment) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: Item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = items[position]

        holder.bind(currentItem)

        Glide.with(holder.itemView.context)
            .load("https://api.redcircleapi.com/request" + currentItem.imageUrl)
            .centerInside()
            .into(holder.mMoviePoster)

        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mPrice: TextView = itemView.findViewById(R.id.price)
        private val mDescription: TextView = itemView.findViewById(R.id.description)
        private val mItemName: TextView = itemView.findViewById(R.id.itemName)
        val mMoviePoster: ImageView = itemView.findViewById(R.id.imageView)

        fun bind(item: Item) {
            mPrice.text = item.price.toString()
            mDescription.text = item.description
            // Set other item details...
        }
    }
}
