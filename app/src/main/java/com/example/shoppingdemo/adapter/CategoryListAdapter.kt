package com.example.shoppingdemo.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingdemo.databinding.SelectCategoryItemsBinding

class CategoryListAdapter(var context: Context) :
    RecyclerView.Adapter<CategoryListAdapter.ViewHolder>() {

    private val categoriesList = mutableListOf<String>()

    fun setData(category: MutableList<String>) {
        categoriesList.clear()
        categoriesList.addAll(category)
        Log.d("TAG", "Inside setData $category")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        SelectCategoryItemsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.categoryName.text = categoriesList[position]
        holder.binding.categoryName.setOnClickListener {
            Toast.makeText(
                context,
                "Select Category Feature Coming Soon",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun getItemCount(): Int = categoriesList.size


    class ViewHolder(var binding: SelectCategoryItemsBinding) :
        RecyclerView.ViewHolder(binding.root)
}