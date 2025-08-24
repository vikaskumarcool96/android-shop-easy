package com.vikash.shopeasy.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: Int,
    val productName: String,
    val price: String,
    val description: String,
    val productImage: String
) : Parcelable

val sampleProducts = listOf(
    Product(
        1,
        "Shoes",
        "₹1200",
        "Comfortable running shoes with modern design, perfect for daily wear.",
        "https://picsum.photos/600/400?random=1"
    ),
    Product(
        2,
        "T-Shirt",
        "₹499",
        "Stylish cotton t-shirt, lightweight and perfect for summer fashion.",
        "https://picsum.photos/600/400?random=2"
    ),
    Product(
        3,
        "Jeans",
        "₹999",
        "Slim fit jeans made with high quality denim, durable and trendy.",
        "https://picsum.photos/600/400?random=3"
    ),
    Product(
        4,
        "Jacket",
        "₹1999",
        "Warm and stylish jacket, ideal for winter season with a modern look.",
        "https://picsum.photos/600/400?random=4"
    )
)

