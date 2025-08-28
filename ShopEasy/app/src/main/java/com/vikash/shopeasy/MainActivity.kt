package com.vikash.shopeasy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.vikash.shopeasy.dto.Product
import com.vikash.shopeasy.dto.sampleProducts
import com.vikash.shopeasy.ui.CartScreen
import com.vikash.shopeasy.ui.ShopTopBar
import com.vikash.shopeasy.ui.theme.ShopEasyTheme
import com.vikash.shopeasy.viewmodels.HomeViewModel
import androidx.compose.runtime.collectAsState

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
fun HomeScreen(navController: NavHostController, viewModel: HomeViewModel) {
    val count = viewModel.getCartCount()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("ShopEasy") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier,
                actions = {
                    Box(modifier = Modifier.padding(12.dp)) {
                        IconButton(onClick = { navController.navigate("cart") }) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "Cart",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                        val scale = remember { Animatable(1f) }
                        LaunchedEffect(count) {
                            scale.animateTo(
                                targetValue = 1.2f,
                                animationSpec = tween(
                                    durationMillis = 150,
                                    easing = LinearOutSlowInEasing
                                )
                            )
                            scale.animateTo(1f)
                        }
                        if (count > 0) {
                            Box(
                                modifier = Modifier
                                    .scale(scale.value)
                                    .align(Alignment.TopEnd)
                                    .offset(x = (-4).dp, y = 2.dp)
                                    .size(16.dp)
                                    .background(Color.Red, shape = CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$count",
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    modifier = Modifier.offset(y = (-4).dp),
                                )
                            }
                        }
                    }
                }
            )
        },
        content = { padding ->
            GridProductList(
                viewModel.searchedItems.collectAsState().value,
                viewModel,
                padding,
                navController
            )
        }
    )
}

@Composable
fun GridProductList(
    productList: List<Product>,
    viewModel: HomeViewModel,
    padding: PaddingValues,
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = padding.calculateTopPadding() + 16.dp,
                bottom = padding.calculateBottomPadding(),
                start = 16.dp,
                end = 16.dp
            )
    ) {
        SearchTextField(viewModel)
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2)
        ) {
            items(
                count = productList.size,
                key = { index -> productList[index].id },
                contentType = { index -> productList[index]::class },
                itemContent = { index ->
                    val product = productList[index]
                    Box(
                        modifier = Modifier.fillMaxWidth(0.5f),
                        contentAlignment = Alignment.Center
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            shape = RoundedCornerShape(8.dp),
                            elevation = CardDefaults.cardElevation(4.dp),
                            colors = CardDefaults.cardColors(contentColor = Color.White),
                            onClick = {
                                navController.currentBackStackEntry?.savedStateHandle?.set(
                                    "product",
                                    product
                                )
                                navController.navigate("detail")
                            }
                        ) {
                            Column {
                                AsyncImage(
                                    model = product.productImage,
                                    contentDescription = "product Image",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(1.5f)
                                        .clip(RoundedCornerShape(8.dp, 8.dp, 0.dp, 0.dp))
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
                                    Spacer(modifier = Modifier.height(8.dp))


                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        AddToCartButton(
                                            product = product,
                                            viewModel = viewModel,
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(25.dp)
                                                .padding(0.dp),
                                            iconSize = 14.dp,
                                            textSize = 10.sp,
                                            text = "Cart"
                                        )
                                        WishlistButton(
                                            product = product,
                                            viewModel = viewModel,
                                            modifier = Modifier
                                                .padding(0.dp)
                                                .weight(1f)
                                                .height(25.dp),
                                            iconSize = 14.dp,
                                            textSize = 10.sp,
                                            textWhenNotWished = "Wish",
                                            textWhenWished = "Wished"
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun SearchTextField(viewModel: HomeViewModel) {
    // Implementation of SearchTextField UI goes here

    OutlinedTextField(
        value = viewModel.searchQuery.collectAsState().value,
        onValueChange = { newTextValue -> viewModel.getSearchedItems(newTextValue) },
        placeholder = { Text("Search products...") },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color.Gray
            )
        },
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFF5F5F5),
            unfocusedContainerColor = Color(0xFFF5F5F5),

            )
    )
}

@Composable
fun AddToCartButton(
    product: Product?,
    viewModel: HomeViewModel,
    shape: RoundedCornerShape = RoundedCornerShape(16.dp),
    modifier: Modifier,
    iconSize: Dp,
    textSize: TextUnit,
    text: String = "Add to Cart"
) {
    val isInCart = viewModel.cartItems.contains(product)

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed = interactionSource.collectIsPressedAsState()
    val targetColor = if (isInCart) Color.Gray else MaterialTheme.colorScheme.primary
    val animatedColor by animateColorAsState(
        targetValue = targetColor,
        animationSpec = tween(300)
    )
    val backgroundColor = if (isPressed.value) {
        animatedColor.copy(alpha = 0.75f)
    } else {
        animatedColor
    }
    Button(
        onClick = {
            if (!isInCart) {
                product?.let { viewModel.addToCart(product) }
            } else {
                product?.let { viewModel.removeFromCart(product) }
            }
        },
        modifier = modifier,
        shape = shape,
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        interactionSource = interactionSource,
        contentPadding = PaddingValues(0.dp)

    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "",
                tint = Color.White,
                modifier = Modifier.size(iconSize)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = if (isInCart) {
                    "Added"
                } else {
                    text
                },
                fontSize = textSize,
                fontWeight = FontWeight.Normal,
                color = Color.White
            )
        }
    }
}

@Composable
fun WishlistButton(
    product: Product?,
    viewModel: HomeViewModel,
    shape: RoundedCornerShape = RoundedCornerShape(16.dp),
    modifier: Modifier,
    iconSize: Dp,
    textSize: TextUnit,
    textWhenNotWished: String = "Add To Wishlist",
    textWhenWished: String = "Remove from Wishlist"
) {
    OutlinedButton(
        onClick = {viewModel.addOrRemoveFromWishlist(product)},
        modifier = modifier,
        shape = shape,
        contentPadding = PaddingValues(0.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
    ) {
        val icon = if (viewModel.wishlistItems.contains(product)) {
            Icons.Default.Favorite
        } else {
            Icons.Default.FavoriteBorder
        }
        Icon(
            imageVector = icon,
            contentDescription = "wish",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(iconSize)
        )
        val text = if (viewModel.wishlistItems.contains(product)) {
            textWhenWished
        } else {
            textWhenNotWished
        }
        Text(
            text = text,
            fontSize = textSize,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun ShopEasyApp() {
    val navController = rememberNavController()
    val viewModel: HomeViewModel = viewModel()

    NavHost(navController = navController, startDestination = "Home") {
        composable("home") {
            HomeScreen(navController, viewModel)
        }
        composable("detail") { backstackEntry ->
            val productFromNav =
                navController.previousBackStackEntry?.savedStateHandle?.get<Product>("product");
            val product = remember { mutableStateOf(productFromNav) }
            DetailScreen(product.value, { navController.popBackStack() }, viewModel)
        }
        composable("cart") {
            CartScreen(viewModel, { navController.popBackStack() })

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(product: Product?, onBackClick: () -> Unit, viewModel: HomeViewModel) {
    Scaffold(
        topBar = {
            ShopTopBar(product?.productName ?: "", onBackClick)
        },
        content = { padding ->
            Column(
                modifier = Modifier.padding(
                    top = padding.calculateTopPadding() + 16.dp,
                    bottom = padding.calculateBottomPadding(),
                    start = 16.dp,
                    end = 16.dp
                ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
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
                    Column(modifier = Modifier.padding(16.dp)) {
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
                Column {
                    AddToCartButton(
                        product = product,
                        viewModel = viewModel,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .height(55.dp),
                        shape = RoundedCornerShape(50.dp),
                        iconSize = 24.dp,
                        textSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    WishlistButton(
                        product = product,
                        viewModel = viewModel,
                        modifier = Modifier
                            .fillMaxWidth().padding(16.dp).height(55.dp),
                        shape = RoundedCornerShape(50.dp),
                        iconSize = 24.dp,
                        textSize = 18.sp,
                    )
                }
            }

        }
    )
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ShopEasyApp()
}