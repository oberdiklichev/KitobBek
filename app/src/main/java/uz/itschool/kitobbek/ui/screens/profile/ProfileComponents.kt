package uz.itschool.kitobbek.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import uz.itschool.kitobbek.data.remote.model.response.BookResponse

val PrimaryDarkBlue = Color(0xFF1F2B6C)
val SoftGray = Color(0xFFF4F4F6)
val SecondaryText = Color(0xFFC4C4C4)
val SeeAllText = Color(0xFF3AB4CC)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTopBar(
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Shaxsiy kabinet",
                color = PrimaryDarkBlue,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = PrimaryDarkBlue)
            }
        },
        actions = {
            IconButton(onClick = onSettingsClick) {
                Icon(Icons.Default.Settings, contentDescription = "Settings", tint = PrimaryDarkBlue)
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

@Composable
fun ProfileHeader(
    userName: String,
    email: String,
    avatarUrl: String? = null
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(SoftGray),
            contentAlignment = Alignment.Center
        ) {
            if (avatarUrl != null) {
                AsyncImage(
                    model = avatarUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(60.dp),
                    tint = PrimaryDarkBlue
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = userName,
            color = PrimaryDarkBlue,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = email,
            color = SecondaryText,
            fontSize = 14.sp
        )
    }
}

@Composable
fun StatsCard(
    readingCount: Int,
    readCount: Int,
    savedCount: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = SoftGray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatItem(count = readingCount, label = "o'qilayotgan\nkitoblar")
            VerticalDivider(modifier = Modifier.height(40.dp), color = Color.LightGray)
            StatItem(count = readCount, label = "o'qilgan\nkitoblar")
            VerticalDivider(modifier = Modifier.height(40.dp), color = Color.LightGray)
            StatItem(count = savedCount, label = "saqlangan\nkitoblar")
        }
    }
}

@Composable
fun StatItem(count: Int, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = count.toString(),
            color = PrimaryDarkBlue,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            color = SecondaryText,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            lineHeight = 14.sp
        )
    }
}

@Composable
fun SectionHeader(
    title: String,
    onSeeAllClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = PrimaryDarkBlue,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Barchasini ko'rish",
            color = SeeAllText,
            fontSize = 14.sp,
            modifier = Modifier.clickable { onSeeAllClick() }
        )
    }
}

@Composable
fun ReadingBookCard(
    book: BookResponse,
    onBookClick: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .width(320.dp)
            .padding(start = 16.dp, end = 8.dp, bottom = 16.dp)
            .clickable { onBookClick(book.id) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SoftGray)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(width = 80.dp, height = 110.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray)
            ) {
                AsyncImage(
                    model = book.image,
                    contentDescription = book.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                RatingOverlay(rating = book.reyting.toDouble())
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = book.name,
                    color = PrimaryDarkBlue,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = book.author,
                    color = SecondaryText,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "$12.00",
                    color = SeeAllText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
fun SmallBookCard(
    book: BookResponse,
    onBookClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .width(135.dp)
            .padding(start = 16.dp, end = 8.dp)
            .clickable { onBookClick(book.id) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.LightGray)
        ) {
            AsyncImage(
                model = book.image,
                contentDescription = book.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            RatingOverlay(rating = book.reyting.toDouble())
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = book.name,
            color = PrimaryDarkBlue,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = book.author,
            color = SecondaryText,
            fontSize = 13.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = "$12.00",
            color = SeeAllText,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

@Composable
fun GridBookCard(
    book: BookResponse,
    onBookClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onBookClick(book.id) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.LightGray)
        ) {
            AsyncImage(
                model = book.image,
                contentDescription = book.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            RatingOverlay(rating = book.reyting.toDouble())
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = book.name,
            color = PrimaryDarkBlue,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = book.author,
            color = SecondaryText,
            fontSize = 13.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = "$12.00",
            color = SeeAllText,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

@Composable
fun RatingOverlay(rating: Double) {
    Row(
        modifier = Modifier
            .padding(top = 6.dp, end = 6.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(Color.White.copy(alpha = 0.85f))
            .padding(horizontal = 4.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Icon(
            Icons.Default.Star,
            contentDescription = null,
            tint = SeeAllText,
            modifier = Modifier.size(12.dp)
        )
        Spacer(modifier = Modifier.width(2.dp))
        Text(
            text = rating.toString(),
            color = PrimaryDarkBlue,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun LoadingView() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = PrimaryDarkBlue)
    }
}

@Composable
fun ErrorView(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = message, color = Color.Red, textAlign = TextAlign.Center)
    }
}

@Composable
fun EmptyView() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Ma'lumot topilmadi", color = SecondaryText)
    }
}
