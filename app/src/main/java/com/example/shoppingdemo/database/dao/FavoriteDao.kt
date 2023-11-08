package com.example.shoppingdemo.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.shoppingdemo.data.FavoriteItem


@Dao
interface FavoriteDao : BaseDao<FavoriteItem> {

    @Query("Select * from tbl_favorite")
    suspend fun fetchAllFavorite(): MutableList<FavoriteItem>

    @Query("Select * from tbl_favorite where id = :id")
    suspend fun fetchFavorite(id: Int?): FavoriteItem?

    @Query("Delete from tbl_favorite where id = :id")
    suspend fun deleteFavorite(id: Int)

}