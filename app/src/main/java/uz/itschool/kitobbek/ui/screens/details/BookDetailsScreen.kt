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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import uz.itschool.kitobbek.R
import uz.itschool.kitobbek.data.local.Prefs
import uz.itschool.kitobbek.data.remote.model.response.BookResponse
import uz.itschool.kitobbek.ui.screens.pdf.loadProgress

private val NavyDark = Color(0xFF0D1B4B)
private val LightBlue = Color(0xFFB3E5FC)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailsScreen(
    bookId: Int,
    viewModel: BookDetailsViewModel,
    onBackClick: () -> Unit,
    onReadClick: (BookResponse) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val prefs = remember { Prefs(context) }
    var selectedTab by remember { mutableIntStateOf(0) }
    var showAddComment by remember { mutableStateOf(false) }

    LaunchedEffect(bookId) {
        viewModel.loadBookDetails(bookId)
    }
    
    if (showAddComment && uiState.book != null) {
        AddCommentScreen(
            bookName = uiState.book!!.name,
            onBackClick = { showAddComment = false },
            onSendClick = { text, rating ->
                viewModel.sendComment(text, rating)
                showAddComment = false
            }
        )
        return
    }

    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            if (uiState.book != null) {
                val book = uiState.book!!
                val savedPage = remember { loadProgress(context, book.id) }
                val hasStarted = savedPage != -1
                val progress = if (book.countPage > 0 && hasStarted) 
                    savedPage.toFloat() / book.countPage.toFloat() 
                else 0f

                BottomActionBar(
                    progress = progress,
                    isStarted = hasStarted,
                    onClick = { onReadClick(book) }
                )
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
                        val quotesCount = remember(bookId) { prefs.getQuotes(bookId).size }
                        DescriptionTab(
                            text = "Tavsifi",
                            isSelected = selectedTab == 0,
                            onClick = { selectedTab = 0 }
                        )
                        DescriptionTab(
                            text = "Sharhlar(${uiState.comments.size})",
                            isSelected = selectedTab == 1,
                            onClick = { selectedTab = 1 }
                        )
                        DescriptionTab(
                            text = "Iqtiboslar($quotesCount)",
                            isSelected = selectedTab == 2,
                            onClick = { selectedTab = 2 }
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    when (selectedTab) {
                        0 -> {
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
                        }
                        1 -> {
                            // Sharhlar (Comments)
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp)
                            ) {
                                Button(
                                    onClick = { showAddComment = true },
                                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = NavyDark),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("Sharh qoldirish", color = Color.White)
                                }

                                if (uiState.comments.isEmpty()) {
                                    Text(
                                        text = "Hozircha sharhlar yo'q",
                                        color = Color.Gray,
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center
                                    )
                                } else {
                                    uiState.comments.forEach { comment ->
                                        CommentItem(comment.username, comment.text)
                                        Spacer(modifier = Modifier.height(12.dp))
                                    }
                                }
                            }
                        }
                        2 -> {
                            // Iqtiboslar (Quotes)
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp)
                            ) {
                                val quotes = remember(bookId) { prefs.getQuotes(bookId) }
                                if (quotes.isEmpty()) {
                                    Text(
                                        text = "Hozircha iqtiboslar yo'q",
                                        color = Color.Gray,
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center
                                    )
                                } else {
                                    quotes.forEach { quoteData ->
                                        val parts = quoteData.split("|")
                                        if (parts.size >= 2) {
                                            QuoteItem(page = parts[0], text = parts[1])
                                            Spacer(modifier = Modifier.height(12.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun CommentItem(userName: String, text: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = userName, fontWeight = FontWeight.Bold, color = NavyDark, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = text, color = Color.DarkGray, fontSize = 13.sp)
        }
    }
}

@Composable
fun QuoteItem(page: String, text: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F7FA)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = "$page-betdan iqtibos", fontWeight = FontWeight.Bold, color = Color(0xFF006064), fontSize = 12.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "\"$text\"", color = Color.Black, fontSize = 14.sp, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
        }
    }
}

@Composable
fun DescriptionTab(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = text,
            color = if (isSelected) Color(0xFF4DD0E1) else Color.Gray,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            fontSize = 16.sp
        )
        if (isSelected) {
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(2.dp)
                    .background(Color(0xFF4DD0E1))
            )
        }
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
fun BottomActionBar(
    progress: Float,
    isStarted: Boolean,
    onClick: () -> Unit
) {
    val safeProgress = progress.coerceIn(0f, 1f)
    val readPercent = (safeProgress * 100).toInt()

    val rightBg = when {
        readPercent >= 75 -> Color(0xFFC8F5E0)
        readPercent >= 40 -> Color(0xFFFFE5B4)
        else            -> Color(0xFFFFD6D6)
    }
    val rightText = when {
        readPercent >= 75 -> Color(0xFF2E7D32)
        readPercent >= 40 -> Color(0xFFE65100)
        else            -> Color(0xFFC62828)
    }

    val leftWeight = if (isStarted) safeProgress.coerceIn(0.15f, 0.82f) else 1f
    val rightWeight = 1f - (if (isStarted) leftWeight else 0f)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .height(56.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .weight(leftWeight)
                .fillMaxHeight()
                .background(NavyDark),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isStarted) "O'qishni davom ettirish" else "O'qishni boshlash",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        if (isStarted) {
            Box(
                modifier = Modifier
                    .weight(rightWeight)
                    .fillMaxHeight()
                    .background(rightBg),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$readPercent%",
                    color = rightText,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp
                )
            }
        }
    }
}
