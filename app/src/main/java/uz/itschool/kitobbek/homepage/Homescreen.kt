package uz.itschool.kitobbek.homepage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import uz.itschool.kitobbek.BottomNavBar
import uz.itschool.kitobbek.data.remote.model.response.BookResponse
import uz.itschool.kitobbek.data.remote.model.response.CategoryResponse

val NavyDark = Color(0xFF0D1B4B)
val AccentBlue = Color(0xFF1565C0)
val LinkColor = Color(0xFF1565C0)

@Composable
fun HomeScreen(navController: NavController, vm: HomeViewModel = viewModel()) {

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Barchasi") }
    val darsliklar = remember(vm.allBooks) { vm.allBooks.shuffled().take(6) }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopBar(
                onMenuClick = { /*TODO*/ },
                onProfileClick = { navController.navigate("profile") }
            )
        },
        bottomBar = { BottomNavBar(navController = navController) }
    ) { padding ->

        if (vm.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Yuklanmoqda...")
            }
        }

        if (vm.error.isNotEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = vm.error, color = Color.Red)
            }
        }

        if (!vm.isLoading && vm.error.isEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {

                item {
                    CategoryChips(
                        categories = vm.categories,
                        selected = selectedCategory,
                        onSelect = { selectedCategory = it }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                item {
                    SearchBar(query = searchQuery, onChange = { searchQuery = it })
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // MainBook faqat null bo'lmaganda ko'rsatiladi
                vm.mainBook?.let { book ->
                    item {
                        FeaturedBanner(book = book, onReadClick = { /*TODO*/ })
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }

                item {
                    SectionHeader(title = "Romanlar", onSeeAll = { /*TODO*/ })
                    Spacer(modifier = Modifier.height(12.dp))
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(items = vm.allBooks, key = { it.id }) { book ->
                            BookCardItem(book = book, onClick = { /*TODO*/ })
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }

                item {
                    SectionHeader(title = "Darsliklar", onSeeAll = { /*TODO*/ })
                    Spacer(modifier = Modifier.height(12.dp))
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(items = darsliklar, key = { it.id }) { book ->
                            BookCoverItem(book = book, onClick = { /*TODO*/ })
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun TopBar(onMenuClick: () -> Unit, onProfileClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onMenuClick) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = null,
                tint = Color.Black
            )
        }

        Text(
            text = "Bosh Sahifa",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(Color(0xFFE8EAF6))
                .clickable { onProfileClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = NavyDark
            )
        }
    }
}

@Composable
fun CategoryChips(
    categories: List<CategoryResponse>,
    selected: String,
    onSelect: (String) -> Unit
) {
    val allCategories = mutableListOf(CategoryResponse("Barchasi"))
    allCategories.addAll(categories)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for (cat in allCategories) {
            if (cat.typeName == selected) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(NavyDark)
                        .clickable { onSelect(cat.typeName) }
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = cat.typeName,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(Color.Transparent)
                        .clickable { onSelect(cat.typeName) }
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = cat.typeName,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF757575)
                    )
                }
            }
        }
    }
}

@Composable
fun SearchBar(query: String, onChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        placeholder = {
            Text(
                text = "Kitob yoki muallifni qidiring...",
                color = Color(0xFFBDBDBD),
                fontSize = 14.sp
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = Color(0xFFBDBDBD)
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color(0xFFE0E0E0),
            focusedBorderColor = NavyDark,
            unfocusedContainerColor = Color(0xFFFAFAFA),
            focusedContainerColor = Color.White
        )
    )
}

@Composable
fun FeaturedBanner(book: BookResponse, onReadClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(Brush.horizontalGradient(listOf(NavyDark, AccentBlue)))
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 20.dp, top = 20.dp, bottom = 20.dp)
                .width(200.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = book.author + "\n\"" + book.name + "\" asari",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 22.sp,
                maxLines = 3
            )
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF1976D2))
                    .clickable { onReadClick() }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(text = "Hoziroq o'qish", fontSize = 12.sp, color = Color.White)
            }
        }

        // Book cover image on the right
        AsyncImage(
            model = book.image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp)
                .width(100.dp)
                .height(150.dp)
                .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
        )
    }
}

@Composable
fun SectionHeader(title: String, onSeeAll: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Barchasini ko'rish",
            fontSize = 13.sp,
            color = LinkColor,
            modifier = Modifier.clickable { onSeeAll() }
        )
    }
}
