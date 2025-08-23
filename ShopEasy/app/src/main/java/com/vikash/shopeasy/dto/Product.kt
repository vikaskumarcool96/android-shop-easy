package com.vikash.shopeasy.dto

data class Product(
    val id: Int,
    val productName: String,
    val price: String
)

val sampleProducts = listOf(
    Product(1, "Shoes", "₹1200"),
    Product(2, "T-Shirt", "₹499"),
    Product(3, "Jeans", "₹999"),
    Product(4, "Jacket", "₹1999")
)
