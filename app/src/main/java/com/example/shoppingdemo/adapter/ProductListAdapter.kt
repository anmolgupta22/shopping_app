package com.example.shoppingdemo.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingdemo.databinding.ProductItemsBinding
import com.example.shoppingdemo.model.Categories

class ProductListAdapter(var context: Context, private var cartItemsCount: CartItemsCount) :
    RecyclerView.Adapter<ProductListAdapter.ProductViewHolder>() {

    private val categories = mutableListOf<Categories>()

    fun setData(category: MutableList<Categories>) {
        categories.clear()
        categories.addAll(category)
        Log.d("TAG", "Inside setData $category")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder(
        ProductItemsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), cartItemsCount
    )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.onBind(categories[position], context)
    }

    override fun getItemCount(): Int = categories.size

    class ProductViewHolder(
        private val binding: ProductItemsBinding,
        private var cartItemsCount: CartItemsCount,
    ) :
        RecyclerView.ViewHolder(binding.root),
        ProductListChildAdapter.CartCount {

        fun onBind(category: Categories, context: Context) {
            binding.productName.text = category.name

            val linearLayoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            val eventListChildAdapter = ProductListChildAdapter(context, category.items, this)
            binding.rvCategoryItem.apply {
                layoutManager = linearLayoutManager
                adapter = eventListChildAdapter
                itemAnimator = null
                setHasFixedSize(true)
            }
        }

        override fun clicked(size: Int) {
            Log.d("TAG", "onItemClicked: check cart size $size")
            cartItemsCount.onClicked(size)
        }
    }

    interface CartItemsCount {
        fun onClicked(size: Int)
    }


}