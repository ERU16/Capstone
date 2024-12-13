package com.dicoding.capstonebre.question

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.capstonebre.R
import com.dicoding.capstonebre.response.QuestionData

class QuestionAdapter(
    private val questionData: QuestionData,
    private val verifyAnswer: (String) -> Unit
) : RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    inner class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val questionTextView: TextView = itemView.findViewById(R.id.tvQuestion)
        val optionsContainer: LinearLayout = itemView.findViewById(R.id.llOptions)

        fun bind() {
            questionTextView.text = questionData.quizQuestion

            optionsContainer.removeAllViews()
            questionData.quizOptions?.forEach { option ->
                val button = Button(itemView.context).apply {
                    text = option
                    setOnClickListener {
                        verifyAnswer(option ?: "")
                    }
                }
                optionsContainer.addView(button)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_question, parent, false)
        return QuestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return 1
    }
}