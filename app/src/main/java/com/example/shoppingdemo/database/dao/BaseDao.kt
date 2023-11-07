package com.example.shoppingdemo.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Update
import com.example.shoppingdemo.model.CartItem


@Dao
interface BaseDao<T> {

    @Insert(onConflict = REPLACE)
    fun insertAll(shopping: List<T?>?)

    @Insert(onConflict = REPLACE)
    fun insert(shopping: T?): Long

    @Update(onConflict = REPLACE)
    fun update(shopping: T?): Int

    @Delete
    fun delete(shopping: T?): Int
}