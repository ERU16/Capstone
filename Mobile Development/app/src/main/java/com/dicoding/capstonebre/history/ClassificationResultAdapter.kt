package com.dicoding.capstonebre.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.capstonebre.R
import com.dicoding.capstonebre.database.ClassificationResult


class ClassificationResultAdapter(
    private var results: List<ClassificationResult>
) : RecyclerView.Adapter<ClassificationResultAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val labelTextView: TextView = view.findViewById(R.id.labelTextView)
        private val scoreTextView: TextView = view.findViewById(R.id.scoreTextView)
        private val resultImageView: ImageView = view.findViewById(R.id.resultImageView)

        fun bind(result: ClassificationResult) {
            labelTextView.text = result.labels
            scoreTextView.text = "Score: ${(result.score * 100).toInt()}%"

            Glide.with(itemView.context)
                .load(result.imageUri)
                .into(resultImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_classification_result, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(results[position])
    }

    override fun getItemCount(): Int = results.size

    fun updateResults(newResults: List<ClassificationResult>) {
        results = newResults
        notifyDataSetChanged()
    }
}
