package uz.itschool.kitobbek.ui.screens.pdf

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import uz.itschool.kitobbek.data.local.Prefs
import java.io.File

private val SubGray = Color(0xFF9E9E9E)
private val TealAccent = Color(0xFF4ECBA0)
private val NavyDark = Color(0xFF1B2A4A)

fun saveProgress(context: Context, bookId: Int, page: Int) {
    context.getSharedPreferences("reading_progress", Context.MODE_PRIVATE)
        .edit().putInt("page_$bookId", page).apply()
}

fun loadProgress(context: Context, bookId: Int): Int {
    return context.getSharedPreferences("reading_progress", Context.MODE_PRIVATE)
        .getInt("page_$bookId", -1)
}

suspend fun downloadPdf(context: Context, bookId: Int, url: String): File {
    val file = File(context.cacheDir, "book_$bookId.pdf")
    if (file.exists() && file.length() > 0) return file
    withContext(Dispatchers.IO) {
        val connection = java.net.URL(url).openConnection()
        connection.connectTimeout = 15_000
        connection.readTimeout = 60_000
        connection.connect()
        connection.getInputStream().use { input ->
            file.outputStream().use { output -> input.copyTo(output) }
        }
    }
    return file
}

fun renderPage(renderer: PdfRenderer, pageIndex: Int): Bitmap {
    val page = renderer.openPage(pageIndex)
    val scale = 2f
    val bmp = Bitmap.createBitmap(
        (page.width * scale).toInt(),
        (page.height * scale).toInt(),
        Bitmap.Config.ARGB_8888
    )
    bmp.eraseColor(android.graphics.Color.WHITE)
    val matrix = android.graphics.Matrix().apply { setScale(scale, scale) }
    page.render(bmp, null, matrix, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
    page.close()
    return bmp
}

@Composable
fun PdfReaderScreen(
    bookId: Int,
    bookName: String,
    bookFile: String,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    BackHandler {
        onBack()
    }

    // To'xtalgan sahifadan boshlash
    val saved = remember { loadProgress(context, bookId) }
    val startPage = if (saved == -1) 0 else saved
    var currentPage by remember { mutableIntStateOf(startPage) }
    var totalPages by remember { mutableIntStateOf(0) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    val rendererHolder = remember { mutableStateOf<PdfRenderer?>(null) }
    
    val prefs = remember { Prefs(context) }
    var showQuoteDialog by remember { mutableStateOf(false) }
    var quoteText by remember { mutableStateOf("") }

    // Zoom va Pan holati
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    LaunchedEffect(Unit) {
        prefs.addToReading(bookId)
        withContext(Dispatchers.IO) {
            try {
                val file = downloadPdf(context, bookId, bookFile)
                val fd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
                val renderer = PdfRenderer(fd)
                rendererHolder.value = renderer
                totalPages = renderer.pageCount
                currentPage = currentPage.coerceIn(0, renderer.pageCount - 1)
                bitmap = renderPage(renderer, currentPage)
            } catch (e: Exception) {
                errorMsg = "PDF yuklanmadi: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    LaunchedEffect(currentPage, rendererHolder.value) {
        val renderer = rendererHolder.value ?: return@LaunchedEffect
        withContext(Dispatchers.IO) {
            bitmap = renderPage(renderer, currentPage)
        }
        saveProgress(context, bookId, currentPage)
        
        if (totalPages > 0 && currentPage + 1 == totalPages) {
            prefs.addToRead(bookId)
        }

        // Sahifa o'zgarganda zoomni reset qilish
        scale = 1f
        offset = Offset.Zero
    }

    DisposableEffect(Unit) {
        onDispose { rendererHolder.value?.close() }
    }

    val progress = if (totalPages > 0) (currentPage + 1).toFloat() / totalPages else 0f
    val readPercent = (progress * 100).toInt()

    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
    ) {
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
                modifier = Modifier.size(24.dp).clickable { onBack() }
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text = bookName,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )
            if (totalPages > 0) {
                Text(
                    text = "${currentPage + 1} / $totalPages",
                    color = SubGray,
                    fontSize = 13.sp
                )
            }
            Spacer(Modifier.width(8.dp))
            IconButton(onClick = { showQuoteDialog = true }) {
                Icon(Icons.Default.Edit, contentDescription = "Iqtibos qo'shish", tint = Color.White)
            }
        }

        Box(
            Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            when {
                isLoading -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = TealAccent)
                        Spacer(Modifier.height(12.dp))
                        Text("Yuklanmoqda...", color = SubGray, fontSize = 14.sp)
                    }
                }
                errorMsg != null -> {
                    Text(errorMsg!!, color = Color.Red, fontSize = 14.sp)
                }
                bitmap != null -> {
                    Image(
                        bitmap = bitmap!!.asImageBitmap(),
                        contentDescription = "Sahifa ${currentPage + 1}",
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(Unit) {
                                detectTransformGestures { _, pan, zoom, _ ->
                                    scale = (scale * zoom).coerceIn(1f, 5f)
                                    if (scale > 1f) {
                                        offset += pan
                                    } else {
                                        offset = Offset.Zero
                                    }
                                }
                            }
                            .graphicsLayer(
                                scaleX = scale,
                                scaleY = scale,
                                translationX = offset.x,
                                translationY = offset.y
                            ),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }

        Row(
            Modifier
                .fillMaxWidth()
                .background(Color(0xFF1E1E1E))
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { if (currentPage > 0) currentPage-- },
                enabled = currentPage > 0,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NavyDark,
                    disabledContainerColor = Color(0xFF333333)
                )
            ) {
                Text("← Orqaga", fontSize = 13.sp)
            }

            if (totalPages > 0) {
                val pillColor = when {
                    readPercent >= 75 -> Color(0xFF2E7D32)
                    readPercent >= 40 -> Color(0xFFE65100)
                    else -> Color(0xFFC62828)
                }
                val pillBg = when {
                    readPercent >= 75 -> Color(0xFFC8F5E0)
                    readPercent >= 40 -> Color(0xFFFFE5B4)
                    else -> Color(0xFFFFD6D6)
                }
                Box(
                    Modifier
                        .background(pillBg, RoundedCornerShape(20.dp))
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "$readPercent% o'qildi",
                        color = pillColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }

            Button(
                onClick = { if (currentPage < totalPages - 1) currentPage++ },
                enabled = currentPage < totalPages - 1,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NavyDark,
                    disabledContainerColor = Color(0xFF333333)
                )
            ) {
                Text("Keyingi →", fontSize = 13.sp)
            }
        }
    }

    if (showQuoteDialog) {
        AlertDialog(
            onDismissRequest = { showQuoteDialog = false },
            title = { Text("Iqtibos qo'shish") },
            text = {
                OutlinedTextField(
                    value = quoteText,
                    onValueChange = { quoteText = it },
                    label = { Text("Iqtibos matni") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (quoteText.isNotBlank()) {
                            prefs.saveQuote(bookId, currentPage + 1, quoteText)
                            quoteText = ""
                            showQuoteDialog = false
                        }
                    }
                ) {
                    Text("Saqlash")
                }
            },
            dismissButton = {
                TextButton(onClick = { showQuoteDialog = false }) {
                    Text("Bekor qilish")
                }
            }
        )
    }
}
