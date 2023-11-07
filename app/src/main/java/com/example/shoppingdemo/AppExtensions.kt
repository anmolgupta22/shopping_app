package com.example.shoppingdemo

import android.view.View

fun View.show() {
    let { visibility = View.VISIBLE }
}

fun View.hide() {
    let { visibility = View.INVISIBLE }
}

fun View.gone() {
    let { visibility = View.GONE }
}
