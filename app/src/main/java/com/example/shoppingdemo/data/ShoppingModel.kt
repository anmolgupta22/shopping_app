package com.example.shoppingdemo.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_shopping")
data class Product(

    @ColumnInfo
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,

    @ColumnInfo
    var status: Boolean? = null,

    @ColumnInfo
    var message: String? = null,

    @ColumnInfo
    var error: String? = null,

    @ColumnInfo
    var categories: MutableList<Categories> = arrayListOf(),
    )

data class Items(

    var id: Int? = null,
    var name: String? = null,
    var icon: String? = null,
    var price: Float? = null,
)

@Entity(tableName = "tbl_favorite")
data class FavoriteItem(

    @ColumnInfo
    @PrimaryKey
    var id: Int? = null,

    @ColumnInfo
    var name: String? = null,

    @ColumnInfo
    var icon: String? = null,

    @ColumnInfo
    var price: Float? = null,
)

data class Categories(
    var id: Int? = null,
    var name: String? = null,
    var items: ArrayList<Items> = arrayListOf(),
    )

@Entity(tableName = "tbl_cart")
data class CartItem(

    @ColumnInfo
    @PrimaryKey
    var id: Int? = null,

    @ColumnInfo
    var itemCount: Int = 0,

    @ColumnInfo
    var name: String? = null,

    @ColumnInfo
    var icon: String? = null,

    @ColumnInfo
    var price: Float? = null,
)

