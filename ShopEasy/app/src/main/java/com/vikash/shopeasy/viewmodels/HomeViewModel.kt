package com.vikash.shopeasy.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.vikash.shopeasy.dto.Product
import com.vikash.shopeasy.dto.sampleProducts
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel : ViewModel() {

    val cartItems = mutableStateListOf<Product>()
    val products = mutableStateListOf(*sampleProducts.toTypedArray())
    private val _searchQuery = MutableStateFlow("")
    private val _searchedItems =MutableStateFlow(sampleProducts)
    val searchedItems: StateFlow<List<Product>> = _searchedItems

    val searchQuery: StateFlow<String> = _searchQuery
    fun addToCart(product: Product) {
        cartItems.add(product)
    }

    fun removeFromCart(product: Product) {
        cartItems.remove(product)
    }

    fun getCartCount(): Int {
        return cartItems.size
    }

    fun getSearchedItems(query: String) {
        _searchQuery.value = query
        _searchedItems.value = if (query.isEmpty()) {
            sampleProducts
        } else {
            sampleProducts.filter { it.productName.contains(query, ignoreCase = true) }
        }
    }
}