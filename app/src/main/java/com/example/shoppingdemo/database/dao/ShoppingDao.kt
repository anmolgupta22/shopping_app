package com.example.shoppingdemo.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.shoppingdemo.data.Product

@Dao
interface ShoppingDao : BaseDao<Product> {

    @Query("Select * from tbl_shopping")
    suspend fun fetchAllProduct(): Product?

}