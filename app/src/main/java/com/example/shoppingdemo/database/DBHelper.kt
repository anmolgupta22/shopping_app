package com.example.shoppingdemo.database

import android.content.Context
import androidx.room.*
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.shoppingdemo.database.dao.CartDao
import com.example.shoppingdemo.database.dao.FavoriteDao
import com.example.shoppingdemo.database.dao.ShoppingDao
import com.example.shoppingdemo.data.CartItem
import com.example.shoppingdemo.data.FavoriteItem
import com.example.shoppingdemo.data.Product


@Database(
    entities = [Product::class, FavoriteItem::class, CartItem::class],
    version = 1,
    exportSchema = false
)

@TypeConverters(Converters::class)
abstract class DBHelper : RoomDatabase() {

    abstract fun shoppingDao(): ShoppingDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun cartDao(): CartDao

    companion object {

        /**
         * The only instance
         */
        private var appDataBaseInstance: DBHelper? = null

        /**
         * Gets the singleton instance of SampleDatabase.
         *
         * @param context The context.
         * @return The singleton instance of SampleDatabase.
         */
        @Synchronized
        fun getInstance(context: Context): DBHelper {
            if (appDataBaseInstance == null) {
                appDataBaseInstance = Room.databaseBuilder(
                    context.applicationContext,
                    DBHelper::class.java,
                    "shopping_database"
                )
                    .allowMainThreadQueries()
                    .addMigrations(MIGRATION_1_2)
                    .build()
            }
            return appDataBaseInstance!!
        }

        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE tbl_shopping (`id` INTEGER PRIMARY KEY, `status` BOOLEAN, `message` TEXT, `error` TEXT, `categories` TEXT )")
                database.execSQL("INSERT INTO tbl_shopping (`status`, `message`, `error`, `categories`)")

                database.execSQL("CREATE TABLE tbl_favorite (`id` INTEGER PRIMARY KEY, `name` TEXT, `icon` TEXT, `price` Float)")
                database.execSQL("INSERT INTO tbl_favorite (`id`, `name`, `icon`, `price`)")

                database.execSQL("CREATE TABLE tbl_cart (`id` INTEGER PRIMARY KEY, `itemCount` Int, `name` TEXT, `icon` TEXT, `price` Float)")
                database.execSQL("INSERT INTO tbl_cart (`id`, `itemCount`, `name`, `icon`, `price`)")
            }
        }
    }
}