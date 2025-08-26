package com.vikash.shopeasy.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.vikash.shopeasy.dto.Product

class HomeViewModel: ViewModel() {

    val cartItems = mutableStateListOf<Product>()
    fun addToCart(product: Product) {
        cartItems.add(product)
    }

    fun removeFromCart(product: Product) {
        cartItems.remove(product)
    }

    fun getCartCount(): Int {
        return cartItems.size
    }
}