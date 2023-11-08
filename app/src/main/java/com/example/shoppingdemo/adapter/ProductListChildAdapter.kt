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
import com.example.shoppingdemo.databinding.CategoryItemsBinding
import com.example.shoppingdemo.utils.hide
import com.example.shoppingdemo.data.CartItem
import com.example.shoppingdemo.data.FavoriteItem
import com.example.shoppingdemo.data.Items
import com.example.shoppingdemo.utils.show
import kotlinx.coroutines.*

class ProductListChildAdapter(
    var context: Context,
    private var categoryItems: List<Items>,
    private var cartCount: CartCount,
) :
    RecyclerView.Adapter<ProductListChildAdapter.ProductChildViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductChildViewHolder =
        ProductChildViewHolder(
            CategoryItemsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), cartCount
        )


    override fun onBindViewHolder(holder: ProductChildViewHolder, position: Int) {
        holder.onBind(categoryItems[position], context)
    }

    override fun getItemCount(): Int = categoryItems.size

    class ProductChildViewHolder(
        private val binding: CategoryItemsBinding,
        private var cartCount: CartCount,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun onBind(item: Items, context: Context) {
            binding.itemName.text = item.name
            binding.prize.text = "₹" + item.price.toString()
            val id: Int? = item.id
            CoroutineScope(Dispatchers.IO).launch {
                val favItem = DBHelper.getInstance(context).favoriteDao().fetchFavorite(id)
                CoroutineScope(Dispatchers.Main).launch {
                    if (favItem != null) {
                        Log.d("TAG", "onBind: favorite item $favItem ${item.id}")
                        binding.selectFavorite.hide()
                        binding.UnselectFavorite.show()
                    }
                }
            }

            Glide.with(context).load(item.icon).apply(
                RequestOptions.placeholderOf(R.drawable.ic_three_line)
                    .error(R.drawable.ic_three_line)
            ).into(binding.image)

            binding.selectFavorite.setOnClickListener {
                binding.selectFavorite.hide()
                binding.UnselectFavorite.show()
                val favoriteItem = FavoriteItem(
                    item.id,
                    item.name, item.icon, item.price
                )
                CoroutineScope(Dispatchers.IO).launch {
                    DBHelper.getInstance(context).favoriteDao().insert(favoriteItem)
                }
            }

            binding.UnselectFavorite.setOnClickListener {
                binding.selectFavorite.show()
                binding.UnselectFavorite.hide()

                item.id?.let { it1 ->
                    CoroutineScope(Dispatchers.IO).launch {
                        DBHelper.getInstance(context).favoriteDao().deleteFavorite(it1)
                    }
                }
            }
            binding.addCart.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    val totalItem: Int =
                        DBHelper.getInstance(context).cartDao().fetchTotalItemCart() ?: 0
                    CoroutineScope(Dispatchers.Main).launch {
                        cartCount.clicked(totalItem + 1)
                        Log.d("TAG", "onBind: check clicked totalItem $totalItem")
                    }
                    val cartItemDb: CartItem? =
                        DBHelper.getInstance(context).cartDao().fetchCart(item.id)
                    CoroutineScope(Dispatchers.Main).launch {
                        if (cartItemDb != null) {
                            val itemCount = cartItemDb.itemCount
                            val cartItem =
                                CartItem(
                                    item.id,
                                    itemCount.plus(1),
                                    item.name,
                                    item.icon,
                                    item.price
                                )
                            DBHelper.getInstance(context).cartDao().insert(cartItem)
                        } else {
                            val cartItem =
                                CartItem(item.id, 1, item.name, item.icon, item.price)
                            DBHelper.getInstance(context).cartDao().insert(cartItem)
                        }
                    }
                }
            }
        }
    }

    interface CartCount {
        fun clicked(size: Int)
    }

}