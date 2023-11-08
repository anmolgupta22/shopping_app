package com.example.shoppingdemo.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.shoppingdemo.R
import com.example.shoppingdemo.database.DBHelper
import com.example.shoppingdemo.databinding.CartItemsBinding
import com.example.shoppingdemo.data.CartItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CartAdapter(var context: Context, private var totalPriceCount: TotalPriceCount) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private val cartItems = mutableListOf<CartItem>()

    fun setData(item: MutableList<CartItem>) {
        cartItems.clear()
        cartItems.addAll(item)
        Log.d("TAG", "Inside setData $item")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CartViewHolder(
        CartItemsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), totalPriceCount
    )

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.onBind(cartItems[position], context)

        holder.binding.removeItem.setOnClickListener {
            val item = cartItems[position]
            CoroutineScope(Dispatchers.IO).launch {
                var count = 0
                val cardItem: CartItem? = DBHelper.getInstance(context).cartDao().fetchCart(item.id)
                CoroutineScope(Dispatchers.Main).launch {
                    if (cardItem != null) {
                        val one = 1
                        count = cardItem.itemCount.minus(one)
                        val newCartItem = CartItem(
                            item.id,
                            count,
                            item.name,
                            item.icon,
                            item.price
                        )
                        DBHelper.getInstance(context).cartDao().insert(newCartItem)
                    }
                    if (count == 0) {
                        CoroutineScope(Dispatchers.IO).launch {
                            DBHelper.getInstance(context).cartDao().deleteCart(item.id)
                            val updateCartItem: MutableList<CartItem> =
                                DBHelper.getInstance(context).cartDao().fetchAllCart()
                            CoroutineScope(Dispatchers.Main).launch {
                                cartItems.remove(cartItems[position])
                                notifyItemRemoved(position)
                                notifyItemRangeChanged(position, itemCount)
                                setData(updateCartItem)
                            }
                        }
                    }
                    CoroutineScope(Dispatchers.Main).launch {
                        holder.binding.itemCount.text = count.toString()
                        val total = item.price?.times(count)
                        holder.binding.totalItemPrice.text = "₹" + total.toString()
                        totalPriceCount.onClicked()
                    }
                }
            }
        }
    }


    override fun getItemCount(): Int = cartItems.size

    class CartViewHolder(
        val binding: CartItemsBinding,
        private var totalPriceCount: TotalPriceCount,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun onBind(
            item: CartItem,
            context: Context,
        ) {
            binding.cartName.text = item.name
            binding.cartPrice.text = "₹" + item.price.toString()
            binding.itemCount.text = item.itemCount.toString()
            val totalValue = item.price?.times(item.itemCount)
            binding.totalItemPrice.text = "₹" + totalValue.toString()

            Glide.with(context).load(item.icon).apply(
                RequestOptions.placeholderOf(R.drawable.ic_three_line)
                    .error(R.drawable.ic_three_line)
            ).into(binding.cartImage)
            totalPriceCount.onClicked()


            binding.addMore.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    val cardItem: CartItem? =
                        DBHelper.getInstance(context).cartDao().fetchCart(item.id)
                    CoroutineScope(Dispatchers.Main).launch {
                        val one = 1
                        val count = cardItem?.itemCount?.plus(one)
                        val newCartItem =
                            count?.let { it1 ->
                                CartItem(
                                    item.id,
                                    it1,
                                    item.name,
                                    item.icon,
                                    item.price
                                )
                            }
                        newCartItem?.let { it1 ->
                            DBHelper.getInstance(context).cartDao().insert(it1)
                        }
                        binding.itemCount.text = count.toString()
                        val total = count?.let { it1 -> item.price?.times(it1) }
                        binding.totalItemPrice.text = "₹" + total.toString()
                        totalPriceCount.onClicked()
                    }
                }
            }
        }
    }

    interface TotalPriceCount {
        fun onClicked()
    }
}