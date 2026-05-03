package uz.itschool.kitobbek.ui.screens.articles

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import uz.itschool.kitobbek.ui.components.TopBar

data class Article(
    val id: Int,
    val title: String,
    val image: String,
    val time: String,
    val date: String,
    val content: String
)

val mockArticles = listOf(
    Article(
        1,
        "Rossiya va Xitoy yaqinlashuvi: kim nimadan manfaatdor?",
        "https://storage.kun.uz/source/8/wI4_U2T8Z4lV0y-U-0_N8N8N8N8N8N8.jpg",
        "19:32",
        "25.03.2023",
        "Rossiya va Xitoy o'rtasidagi munosabatlarning yangi formati urushdan oldingiga qaraganda ham oddiy ham murakkab. Bir tomondan, Rossiyaning Pekinga qaramligi iqtisodiy va siyosiy jihatdan oshib bormoqda. Boshqa tomondan, Xitoyning Moskvaga bog'liqligi kuchaymoqda.\n\nRossiyaning Ukraina hududiga bostirib kirishi ortidan rasmiy Moskva va G'arb davlatlari o'rtasidagi geosiyosiy keskinlik nihoyatda taranglashib ketdi. Rasmiy Moskvaga nisbatan siyosiy va iqtisodiy bosim kuchaydi. Ushbu vaziyat fonida Rossiya va Xitoy yetakchilari \"har tomonlama sheriklik va strategik hamkorlikni\" chuqurlashtirish to'g'risidagi bayonotni imzoladi. Bu nimani anglatadi?"
    ),
    Article(
        2,
        "Tashkil topish, yuksalish va xotima. O'zbek xonliklari chegaralarining yangi xaritalari",
        "https://storage.kun.uz/source/9/jW_YyV_X_U_X_U_X_U_X_U.jpg",
        "20:50",
        "27.03.2023",
        "O'zbek xonliklari tarixi o'zbek xalqi davlatchilik tarixining muhim bosqichi hisoblanadi. Bugungi kunda ushbu davrni xolis o'rganish va keng jamoatchilikka yetkazish dolzarb vazifalardan biridir."
    ),
    Article(
        3,
        "Putinning xalqaro qidiruvga berilish sababli. Putin haqidagi asosiy savollarga javoblar",
        "https://storage.kun.uz/source/9/1_X_U_X_U_X_U_X_U_X_U.jpg",
        "10:15",
        "20.03.2023",
        "Xalqaro jinoiy sud tomonidan chiqarilgan qaror ko'plab savollarni keltirib chiqardi. Ushbu maqolada biz ana shu savollarga javob izlaymiz."
    )
)

@Composable
fun ArticlesScreen(
    onMenuClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    var selectedArticle by remember { mutableStateOf<Article?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    if (selectedArticle != null) {
        ArticleDetailScreen(
            article = selectedArticle!!,
            onBackClick = { selectedArticle = null }
        )
    } else {
        Scaffold(
            containerColor = Color.White,
            topBar = {
                TopBar(
                    title = "Maqolalar",
                    onMenuClick = onMenuClick,
                    onProfileClick = onProfileClick
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                // Search bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    placeholder = { Text("Maqolalarni qidirish", color = Color.LightGray) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.LightGray) },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF5F5F5),
                        unfocusedContainerColor = Color(0xFFF5F5F5),
                        disabledContainerColor = Color(0xFFF5F5F5),
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                    )
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Featured Article
                    item {
                        val featured = mockArticles[0]
                        FeaturedArticleCard(featured) { selectedArticle = featured }
                    }

                    // Latest Articles section
                    item {
                        Text(
                            text = "Eng so'nggi maqolalar",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F2B6C)
                        )
                    }

                    items(mockArticles.drop(1)) { article ->
                        ArticleRowItem(article) { selectedArticle = article }
                    }
                }
            }
        }
    }
}

@Composable
fun FeaturedArticleCard(article: Article, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = article.image,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = article.title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F2B6C),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                painter = painterResource(id = android.R.drawable.ic_menu_recent_history),
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = Color.LightGray
            )
            Text(text = article.time, fontSize = 12.sp, color = Color.LightGray)
            Spacer(modifier = Modifier.weight(1f))
            Text(text = article.date, fontSize = 12.sp, color = Color.LightGray)
        }
    }
}

@Composable
fun ArticleRowItem(article: Article, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AsyncImage(
            model = article.image,
            contentDescription = null,
            modifier = Modifier
                .size(width = 100.dp, height = 70.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = article.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2B6C),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_recent_history),
                    contentDescription = null,
                    modifier = Modifier.size(12.dp),
                    tint = Color.LightGray
                )
                Text(text = article.time, fontSize = 11.sp, color = Color.LightGray)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = article.date, fontSize = 11.sp, color = Color.LightGray)
            }
        }
    }
}

@Composable
fun ArticleDetailScreen(article: Article, onBackClick: () -> Unit) {
    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopBar(
                title = "Maqolalar",
                onMenuClick = onBackClick,
                menuIcon = Icons.AutoMirrored.Filled.ArrowBack,
                onProfileClick = {}
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            AsyncImage(
                model = article.image,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = article.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2B6C)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_recent_history),
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = Color.LightGray
                )
                Text(text = article.time, fontSize = 13.sp, color = Color.LightGray)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = article.date, fontSize = 13.sp, color = Color.LightGray)
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = article.content,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
