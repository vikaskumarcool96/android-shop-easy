package com.vikash.shopeasy.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.vikash.shopeasy.viewmodels.HomeViewModel

@Composable
fun CartScreen(viewModel: HomeViewModel, onBack: () -> Unit) {
    // Implementation of CartScreen UI goes here
    Scaffold(
        topBar = {ShopTopBar("Cart(${viewModel.getCartCount()})", onBackClick = onBack)},
        bottomBar = {
            BottomBar(viewModel)
        },
        content = {padding ->
            CartList(viewModel, padding)
        }
    )
}
@Composable
fun BottomBar(viewModel: HomeViewModel) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
        ){
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(8.dp)
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "total: ₹${viewModel.cartItems.sumOf { it.price.replace("₹", "").toInt() }}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00796B)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF00897B)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    "Checkout",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun CartList(viewModel: HomeViewModel, padding: PaddingValues) {
    // Implementation of CartList UI goes here
    LazyColumn(
        contentPadding = PaddingValues(
            top = padding.calculateTopPadding() + 16.dp,
            bottom = padding.calculateBottomPadding(),
            start = 16.dp,
            end = 16.dp
        )

    ) {

    items(
        count = viewModel.getCartCount(),
        key = {index -> viewModel.cartItems[index].id },
        contentType = {index -> viewModel.cartItems[index]::class.java},
        itemContent = { index ->
            val product = viewModel.cartItems[index]
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 0.dp, end = 8.dp, top = 0.dp, bottom = 0.dp),
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(Color.White)
                ) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        AsyncImage(
                            model = product.productImage,
                            contentDescription = product.productName,
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Text(
                                text = product.productName,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF323232),
                                lineHeight = 16.sp
                            )
                            Text(
                                text = "Price: ${product.price}",
                                fontSize = 12.sp,
                                lineHeight = 12.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color(0xFF00796B)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    viewModel.removeFromCart(product)
                                },
                                modifier = Modifier
                                    .fillMaxWidth(0.4f)
                                    .height(25.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(Color(0xFFD32F2F)),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(text = "Remove", fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        }
    )
    }
}