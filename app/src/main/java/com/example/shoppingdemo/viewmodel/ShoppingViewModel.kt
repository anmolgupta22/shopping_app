package com.example.shoppingdemo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingdemo.data.CartItem
import com.example.shoppingdemo.data.FavoriteItem
import com.example.shoppingdemo.data.Product
import com.example.shoppingdemo.database.ShoppingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ShoppingViewModel(private val repository: ShoppingRepository) : ViewModel() {

    fun insertProductList(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertProductList(product)
        }
    }

    suspend fun fetchTotalItemCart(): Int? {
        return withContext(Dispatchers.IO) {
            repository.fetchTotalItemCart()
        }
    }

    suspend fun fetchProductList(): Product? {
        return withContext(Dispatchers.IO) {
            repository.fetchProductList()
        }
    }

    suspend fun fetchAllFavorite(): MutableList<FavoriteItem> {
        return withContext(Dispatchers.IO) {
            repository.fetchAllFavorite()
        }
    }

    suspend fun fetchAllCart(): MutableList<CartItem> {
        return withContext(Dispatchers.IO) {
            repository.fetchAllCart()
        }
    }


    suspend fun fetchPriceItemCart(): Float {
        return withContext(Dispatchers.IO) {
            repository.fetchPriceItemCart()
        }
    }

}