package com.example.droidconnyc22.viewmodel

import android.content.Context
import androidx.annotation.StringRes

class StringResource(@StringRes private val resId: Int, private val formatArgs: Array<String> = emptyArray()) {
    fun value(context: Context): String = context.getString(resId, *formatArgs)
}