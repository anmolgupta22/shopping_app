package com.example.shoppingdemo.database


import com.example.shoppingdemo.database.dao.CartDao
import com.example.shoppingdemo.database.dao.FavoriteDao
import com.example.shoppingdemo.database.dao.ShoppingDao
import com.example.shoppingdemo.data.CartItem
import com.example.shoppingdemo.data.FavoriteItem
import com.example.shoppingdemo.data.Product

class ShoppingRepository(
    private val cartDao: CartDao,
    private val favoriteDao: FavoriteDao,
    private val shoppingDao: ShoppingDao,
) {

    fun insertProductList(product: Product) {
        shoppingDao.insert(product)
    }

    suspend fun fetchProductList(): Product? {
        return shoppingDao.fetchAllProduct()
    }

    suspend fun fetchAllFavorite(): MutableList<FavoriteItem> {
        return favoriteDao.fetchAllFavorite()
    }

    suspend fun fetchTotalItemCart(): Int? {
        return cartDao.fetchTotalItemCart()
    }

    suspend fun fetchAllCart(): MutableList<CartItem> {
        return cartDao.fetchAllCart()
    }

    suspend fun fetchPriceItemCart(): Float {
        return cartDao.fetchPriceItemCart()
    }
}