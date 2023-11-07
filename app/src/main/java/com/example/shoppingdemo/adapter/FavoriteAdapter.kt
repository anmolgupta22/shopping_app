package com.example.shoppingdemo.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.shoppingdemo.R
import com.example.shoppingdemo.database.DBHelper
import com.example.shoppingdemo.databinding.FavoriteItemListBinding
import com.example.shoppingdemo.model.CartItem
import com.example.shoppingdemo.model.FavoriteItem
import kotlinx.coroutines.*

class FavoriteAdapter(var context: Context) :
    RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    private val items = mutableListOf<FavoriteItem>()

    fun setData(item: MutableList<FavoriteItem>) {
        items.clear()
        items.addAll(item)
        Log.d("TAG", "Inside setData $item")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = FavoriteViewHolder(
        FavoriteItemListBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.onBind(items[position], context)

    }

    class FavoriteViewHolder(private val binding: FavoriteItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun onBind(item: FavoriteItem, context: Context) {
            binding.favName.text = item.name
            binding.favPrice.text = item.price.toString()

            Glide.with(context).load(item.icon).apply(
                RequestOptions.placeholderOf(R.drawable.ic_three_line)
                    .error(R.drawable.ic_three_line)
            ).into(binding.favImage)

            CoroutineScope(Dispatchers.IO).launch {
                val cartItem: CartItem? = DBHelper.getInstance(context).cartDao().fetchCart(item.id)
                CoroutineScope(Dispatchers.Main).launch {
                    if (cartItem != null) {
                        binding.unit.text = "${cartItem.itemCount} unit"
                    } else {
                        binding.unit.text = "0 unit"
                    }
                }
            }

            binding.addToCart.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    val fetchCartItem = DBHelper.getInstance(context).cartDao().fetchCart(item.id)
                    CoroutineScope(Dispatchers.Main).launch {
                        if (fetchCartItem != null) {
                            val one = 1
                            val count = fetchCartItem.itemCount + one
                            val newCartItem =
                                CartItem(item.id, count, item.name, item.icon, item.price)
                            DBHelper.getInstance(context).cartDao().insert(newCartItem)
                            binding.unit.text = "$count unit"
                        } else {
                            val newCartItem = CartItem(item.id, 1, item.name, item.icon, item.price)
                            DBHelper.getInstance(context).cartDao().insert(newCartItem)
                            binding.unit.text = "1 unit"
                        }
                    }
                }

                Toast.makeText(
                    context,
                    "Added item in Cart",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}