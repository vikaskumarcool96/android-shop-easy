package com.vikash.shopeasy

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.vikash.shopeasy.dto.Product
import com.vikash.shopeasy.dto.sampleProducts
import com.vikash.shopeasy.ui.theme.ShopEasyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShopEasyTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    ShopEasyApp()
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("ShopEasy") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier,
            )
        },
        content = { padding ->
            GridProductList(sampleProducts, padding, navController)
        }
    )
}

@Composable
fun GridProductList(productList: List<Product>, padding: PaddingValues, navController: NavHostController) {
    LazyVerticalStaggeredGrid(
        contentPadding = PaddingValues(
            top = padding.calculateTopPadding() + 16.dp,
            bottom = padding.calculateBottomPadding(),
            start = 16.dp,
            end = 16.dp
        ),
        columns = StaggeredGridCells.Fixed(2)
    ) {
        items(
            count = productList.size,
            key = { index -> productList[index].id},
            contentType = { index -> productList[index]::class},
            itemContent = { index ->
                val product = productList[index]
                Box(
                    modifier = Modifier.fillMaxWidth(0.5f),
                    contentAlignment = Alignment.Center
                ) {
                    val context = LocalContext.current
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(contentColor = Color.White),
                        onClick = {
                            navController.currentBackStackEntry?.savedStateHandle?.set("product", product)
                            navController.navigate("detail")
                        }
                    ){
                        Column{
                            AsyncImage(
                                model = product.productImage,
                                contentDescription = "product Image",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1.5f)
                                    .clip( RoundedCornerShape(8.dp, 8.dp, 0.dp, 0.dp ))
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                                Text(
                                    text = product.productName,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF323232)
                                )
                                Text(
                                    text = product.price,
                                    fontSize = 14.sp,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFF00796B)
                                )

                                Text(
                                    text = product.description,
                                    fontSize = 12.sp,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray,
                                    minLines = 2,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}
@Composable
fun ProductList(productList: List<Product>, padding: PaddingValues, navController: NavHostController) {
    LazyColumn(
        contentPadding = PaddingValues(
            top = padding.calculateTopPadding() + 16.dp,
            bottom = padding.calculateBottomPadding(),
            start = 16.dp,
            end = 16.dp
        )
    ) {
        items(
            count = productList.size,
            key = { index -> productList[index].id },
            contentType = { index -> productList[index]::class },
            itemContent = { index ->
                val product = productList[index]
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(vertical = 8.dp),

                        shape = RoundedCornerShape(16.dp),
                        onClick = {navController.navigate("detail/${product.productName}/${product.price}")},
                        elevation = CardDefaults.cardElevation(6.dp)

                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(
                                        MaterialTheme.colorScheme.primary,
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingCart,
                                    contentDescription = "",
                                    tint = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = product.productName,
                                    style = MaterialTheme.typography.titleMedium,
                                )
                                Text(
                                    text = product.price,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun ShopEasyApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "Home") {
        composable("home") {
            HomeScreen(navController)
        }
        composable("detail") { backstackEntry ->
            val productFromNav  = navController.previousBackStackEntry?.savedStateHandle?.get<Product>("product");
            val product = remember { mutableStateOf(productFromNav) }
            DetailScreen(product.value, {navController.popBackStack()})
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(product: Product?, onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { product?.productName?.let { Text(it) } }, colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ), navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
                })
        },
        content = { padding ->
            Column(
                modifier = Modifier.padding(top = padding.calculateTopPadding() + 16.dp,
                    bottom = padding.calculateBottomPadding(),
                    start = 16.dp,
                    end = 16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .drawWithContent {
                            val gradient = Brush.linearGradient(
                                colors = listOf(Color.White, Color.LightGray),
                                start = Offset(x = size.width / 2f, y = 0f),
                                end = Offset(x = size.width / 2, y = size.height / 2)
                            )
                            drawRect(gradient)
                        }
                )
                    AsyncImage(
                        model = product?.productImage,
                        contentDescription = "Product Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1.5f)
                            .shadow(8.dp, RoundedCornerShape(16.dp))
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )

                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column (modifier = Modifier.padding(16.dp)) {
                        product?.productName?.let {
                            Text(
                                text = it,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        product?.price?.let {
                            Text(
                                text = it,
                                fontSize = 14.sp,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF00796B)
                            )
                        }
                        Spacer(Modifier.padding(8.dp))

                        product?.description?.let {
                            Text(
                                text = it,
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }

                Button(
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(55.dp),
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00695C)
                    )
                ) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = "")
                        Text(
                            text = "Add to Cart",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                }
            }

        }
    )
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ShopEasyApp()
}