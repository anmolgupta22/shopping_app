package com.example.shoppingdemo.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.shoppingdemo.model.Items
import com.example.shoppingdemo.model.Product

@Dao
interface ShoppingDao : BaseDao<Product> {

    @Query("Select * from tbl_shopping")
    suspend fun fetchAllProduct(): Product?

}