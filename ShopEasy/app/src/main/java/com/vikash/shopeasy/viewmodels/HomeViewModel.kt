package com.vikash.shopeasy.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class HomeViewModel: ViewModel() {
    val count = mutableStateOf(0)
    fun incrementCount() {
        count.value++
    }
}