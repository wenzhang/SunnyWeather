package com.wenzhang.sunnyweather.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment

inline fun <reified T> Context.startActivity(block: Intent.() -> Unit) {
    val intent = Intent(this, T::class.java)
    intent.block()
    startActivity(intent)
}

inline fun <reified T> Context.startActivity(vararg params: Pair<String, String>) {
    val intent = Intent(this, T::class.java)
    params.forEach {
        intent.putExtra(it.first, it.second)
    }
    startActivity(intent)
}

