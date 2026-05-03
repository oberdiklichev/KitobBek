package uz.itschool.kitobbek.ui.screens.details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import uz.itschool.kitobbek.R

private val NavyDark = Color(0xFF0D1B4B)
private val LightBlue = Color(0xFFB3E5FC)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailsScreen(
    bookId: Int,
    viewModel: BookDetailsViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(bookId) {
        viewModel.loadBookDetails(bookId)
    }

    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            if (uiState.book != null) {
                BottomActionBar()
            }
        }
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = NavyDark)
            }
        } else if (uiState.book != null) {
            val book = uiState.book!!
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = innerPadding.calculateBottomPadding())
            ) {
                // Header with Navy Background
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                        .background(NavyDark)
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        // Custom Top Bar
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .statusBarsPadding()
                                .padding(horizontal = 8.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            IconButton(onClick = onBackClick) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White)
                            }
                            Text(
                                text = "Batafsil",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            IconButton(onClick = { viewModel.toggleSaveBook() }) {
                                Icon(
                                    painter = painterResource(id = if (uiState.isSaved) R.drawable.bookmark_fill_ico else R.drawable.bookmark_ico),
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        
                        // Tabs (E-Kitob, Audio kitob)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp, vertical = 16.dp)
                                .height(44.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White.copy(alpha = 0.2f))
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.White.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "E-Kitob", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                            if (!book.audio.isNullOrEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "Audio kitob", color = Color.White.copy(alpha = 0.7f))
                                }
                            }
                        }
                    }
                }

                // Content overlapping the navy background
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-100).dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Book Cover
                    Card(
                        modifier = Modifier
                            .width(180.dp)
                            .height(260.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        AsyncImage(
                            model = book.image,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = book.name,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = NavyDark,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                    Text(
                        text = book.author,
                        fontSize = 16.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFD600), modifier = Modifier.size(18.dp))
                        Text(text = " " + book.reyting.toDouble().toString(), fontWeight = FontWeight.Bold, color = NavyDark)
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Description Tabs
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        DescriptionTab(text = "Tavsifi", isSelected = true)
                        DescriptionTab(text = "Sharhlar(${uiState.comments.size})", isSelected = false)
                        DescriptionTab(text = "Iqtiboslar(23)", isSelected = false)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Book Meta Info
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MetaInfoItem(iconRes = R.drawable.file_ico, text = "${book.countPage} bet")
                        if (!book.audio.isNullOrEmpty()) {
                            MetaInfoItem(iconRes = R.drawable.headphone_ico, text = "12 soat")
                        }
                        MetaInfoItem(iconRes = R.drawable.internet_ico, text = book.lang)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = book.description,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        lineHeight = 22.sp,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun DescriptionTab(text: String, isSelected: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = text,
            color = if (isSelected) Color(0xFF4DD0E1) else Color.Gray,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            fontSize = 16.sp
        )
    }
}

@Composable
fun MetaInfoItem(iconRes: Int, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = Color.LightGray,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = text, fontSize = 12.sp, color = Color.LightGray)
    }
}

@Composable
fun BottomActionBar() {
    Button(
        onClick = { /* TODO: Start reading */ },
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(containerColor = NavyDark),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = "O'qishni boshlash",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }
}
