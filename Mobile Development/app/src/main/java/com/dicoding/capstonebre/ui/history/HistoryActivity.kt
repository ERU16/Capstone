package com.dicoding.capstonebre.ui.history

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.capstonebre.R
import com.dicoding.capstonebre.history.ClassificationResultAdapter
import com.dicoding.capstonebre.history.ClassificationResultViewModel

class HistoryActivity : AppCompatActivity() {
    private lateinit var adapter: ClassificationResultAdapter
    private val viewModel: ClassificationResultViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        adapter = ClassificationResultAdapter(emptyList())
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        viewModel.allResults.observe(this) { results ->
            if (results.isNullOrEmpty()) {
                showEmptyState(true)
            } else {
                showEmptyState(false)
                adapter.updateResults(results)
            }
        }
    }

    private fun showEmptyState(isEmpty: Boolean) {
        val emptyTextView = findViewById<TextView>(R.id.emptyTextView)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        if (isEmpty) {
            emptyTextView.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            emptyTextView.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }
}
