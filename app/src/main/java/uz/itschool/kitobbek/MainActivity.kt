package uz.itschool.kitobbek

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import uz.itschool.kitobbek.ui.theme.KitobBekTheme

val NavyDark    = Color(0xFF1B2A4A)
val NavyDeep    = Color(0xFF162240)
val TealAccent  = Color(0xFF4ECBA0)
val StarGold    = Color(0xFFFFC107)
val SubGray     = Color(0xFF9E9E9E)
val DividerGray = Color(0xFFEEEEEE)

data class Book(
    val id: Int = 0,
    val name: String = "",
    val author: String = "",
    val description: String = "",
    val image: String = "",
    val file: String = "",
    val year: String = "",
    val reyting: Int = 0,
    val count_page: Int = 0,
    val lang: String = "",
    val publisher: String = ""
)

val sampleBook = Book(
    id = 1,
    name = "O'tkan kunlar",
    author = "Abdulla Qodiriy",
    description = "Modomiki, biz yangi davrga oyoq qo'ydik, bas, biz har bir yo'sunda ham shu yangi " +
            "davrning yangiliklari ketidan ergashamiz va shunga o'xshash dostonchilik, ro'monchilik " +
            "va hikoyachiliqlarda ham yangarishg'a, xalqimizni shu zamonning «Tohir-Zuhra»lari, " +
            "«Chor darvesh»lari, «Farhod-Shirin» va «Bahromgo'r»lari bilan tanishdirishka o'zimizda " +
            "majburiyat his etamiz.",
    image = "http://handybook.uz/frontend/web/img/701697625957.png",
    file  = "http://handybook.uz/frontend/web/file/701697625957.pdf",
    year  = "2016",
    reyting = 5,
    count_page = 209,
    lang  = "uzbek",
    publisher = "www.ziyouz.com"
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KitobBekTheme {

                var showReader by remember { mutableStateOf(false) }
                var currentBook by remember { mutableStateOf(sampleBook) }

                if (showReader) {
                    PdfReaderScreen(
                        book = currentBook,
                        onBack = { showReader = false }
                    )
                } else {
                    BookDetailScreen(
                        book = currentBook,
                        onReadClick = { showReader = true }
                    )
                }
            }
        }
    }
}

@Composable
fun BookDetailScreen(
    book: Book = sampleBook,
    onReadClick: () -> Unit = {}
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Tavsifi", "Sharhlar(7)", "Iqtiboslar(23)")

    val savedPage = remember { loadProgress(context, book.id) }
    val readProgress = if (book.count_page > 0)
        savedPage.toFloat() / book.count_page.toFloat()
    else 0f

    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize().background(Color.White)) {

            Box(
                Modifier
                    .fillMaxWidth()
                    .background(Brush.verticalGradient(listOf(NavyDeep, NavyDark)))
                    .padding(bottom = 32.dp)
            ) {
                Column {
                    TopBar()
                    Spacer(Modifier.height(20.dp))
                    BookCoverCard(imageUrl = book.image)
                    Spacer(Modifier.height(8.dp))
                }
            }

            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(Modifier.height(20.dp))
                BookTitleSection(book)
                Spacer(Modifier.height(24.dp))
                ContentTabs(tabs, selectedTab) { selectedTab = it }
                Spacer(Modifier.height(16.dp))
                BookMetaRow(pageCount = book.count_page, lang = book.lang)
                Spacer(Modifier.height(16.dp))
                HorizontalDivider(color = DividerGray)
                Spacer(Modifier.height(16.dp))
                Text(
                    text = book.description.replace("\r\n", "\n"),
                    fontSize = 14.sp,
                    color = Color(0xFF333333),
                    lineHeight = 23.sp
                )
                Spacer(Modifier.height(100.dp))
            }
        }

        Box(
            Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color.White)
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            BottomReadButton(
                progress = readProgress,
                onClick = onReadClick
            )
        }
    }
}

@Composable
fun TopBar() {
    Row(
        Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Orqaga",
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.weight(1f))
        Text("Batafsil", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(Modifier.weight(1f))
        Icon(
            imageVector = Icons.Default.Bookmark,
            contentDescription = "Saqlash",
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun BookCoverCard(imageUrl: String) {
    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Card(
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
            modifier = Modifier
                .size(width = 155.dp, height = 215.dp)
                .shadow(24.dp, RoundedCornerShape(12.dp))
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Kitob muqovasi",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun BookTitleSection(book: Book) {
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = book.name,
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF1A1A1A),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(6.dp))
        Text(text = book.author, fontSize = 14.sp, color = SubGray)
        Spacer(Modifier.height(10.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Star, null, tint = StarGold, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(4.dp))
            Text(
                text = "${book.reyting}.0",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1A1A1A)
            )
        }
    }
}

@Composable
fun ContentTabs(tabs: List<String>, selected: Int, onSelect: (Int) -> Unit) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
        tabs.forEachIndexed { index, label ->
            val isSelected = selected == index
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable { onSelect(index) }
                    .padding(vertical = 8.dp, horizontal = 4.dp)
            ) {
                Text(
                    text = label,
                    fontSize = 14.sp,
                    color = if (isSelected) TealAccent else SubGray,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
                Spacer(Modifier.height(6.dp))
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(
                            if (isSelected) TealAccent else Color.Transparent,
                            RoundedCornerShape(1.dp)
                        )
                )
            }
        }
    }
    HorizontalDivider(color = DividerGray)
}

@Composable
fun BookMetaRow(pageCount: Int, lang: String) {
    val langLabel = if (lang.equals("uzbek", ignoreCase = true)) "O'zbek tilida" else lang
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MetaChip(
            icon = { Icon(Icons.Default.MenuBook, null, tint = SubGray, modifier = Modifier.size(15.dp)) },
            text = "$pageCount bet"
        )
        VerticalDividerLine(height = 20.dp)
        MetaChip(
            icon = { Icon(Icons.Default.Language, null, tint = SubGray, modifier = Modifier.size(15.dp)) },
            text = langLabel
        )
    }
}

@Composable
fun MetaChip(icon: @Composable () -> Unit, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        icon()
        Spacer(Modifier.width(5.dp))
        Text(text, fontSize = 12.sp, color = SubGray)
    }
}

@Composable
fun VerticalDividerLine(height: Dp) {
    Box(Modifier.width(1.dp).height(height).background(DividerGray))
}


@Composable
fun BottomReadButton(progress: Float, onClick: () -> Unit) {
    val safeProgress = progress.coerceIn(0f, 1f)
    val remaining = ((1f - safeProgress) * 100).toInt()

    val rightBg = when {
        remaining <= 25 -> Color(0xFFC8F5E0)
        remaining <= 60 -> Color(0xFFFFE5B4)
        else            -> Color(0xFFFFD6D6)
    }
    val rightText = when {
        remaining <= 25 -> Color(0xFF2E7D32)
        remaining <= 60 -> Color(0xFFE65100)
        else            -> Color(0xFFC62828)
    }

    val leftWeight = safeProgress.coerceIn(0.15f, 0.82f)
    val rightWeight = 1f - leftWeight

    Row(
        Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() }
    ) {
        Box(
            Modifier
                .weight(leftWeight)
                .fillMaxHeight()
                .background(NavyDark),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (safeProgress > 0f) "O'qishni davom ettirish" else "O'qishni boshlash",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        Box(
            Modifier
                .weight(rightWeight)
                .fillMaxHeight()
                .background(rightBg),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$remaining%",
                color = rightText,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 15.sp
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BookDetailPreview() {
    KitobBekTheme {
        BookDetailScreen(book = sampleBook)
    }
}