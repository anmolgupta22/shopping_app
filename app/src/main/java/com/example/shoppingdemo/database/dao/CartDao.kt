package com.example.shoppingdemo.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.shoppingdemo.data.CartItem

@Dao
interface CartDao : BaseDao<CartItem> {

    @Query("Select * from tbl_cart")
    suspend fun fetchAllCart(): MutableList<CartItem>

    @Query("Select * from tbl_cart where id = :id")
    suspend fun fetchCart(id: Int?): CartItem?

    @Query("Select SUM(itemCount) AS total from tbl_cart")
    suspend fun fetchTotalItemCart(): Int?

    @Query("Select SUM(price * itemCount) AS total from tbl_cart")
    suspend fun fetchPriceItemCart(): Float

    @Query("Delete from tbl_cart where id = :id")
    suspend fun deleteCart(id: Int?)


}