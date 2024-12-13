package com.dicoding.capstonebre.api

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.capstonebre.R
import com.dicoding.capstonebre.response.DataItem

class AnimalAdapter(private val onItemClick: (String) -> Unit) :
    ListAdapter<DataItem, AnimalAdapter.AnimalViewHolder>(AnimalDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_animal, parent, false)
        return AnimalViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        val animal = getItem(position)
        holder.bind(animal)
        holder.itemView.setOnClickListener {
            animal?.name?.let { name ->
                val cleanedName = name.replace("[#*]".toRegex(), "")
                onItemClick(cleanedName)
            }
        }
    }

    class AnimalViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val tvAnimalName: TextView = view.findViewById(R.id.tvAnimalName)
        private val tvAnimalImage: ImageView = view.findViewById(R.id.tvAnimalImage)

        fun bind(animal: DataItem?) {
            val cleanedName = animal?.name?.replace("[#*]".toRegex(), "") ?: "Unknown"

            tvAnimalName.text = cleanedName

            animal?.imageUrl?.let { imageUrl ->
                Glide.with(view.context)
                    .load(imageUrl)
                    .placeholder(R.drawable.baseline_image_24)
                    .error(R.drawable.baseline_image_24)
                    .into(tvAnimalImage)
            } ?: tvAnimalImage.setImageResource(R.drawable.baseline_image_24)
        }
    }

    class AnimalDiffCallback : DiffUtil.ItemCallback<DataItem>() {
        override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem == newItem
        }
    }
}
