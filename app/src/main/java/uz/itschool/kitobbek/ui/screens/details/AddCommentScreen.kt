package uz.itschool.kitobbek.ui.screens.details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.itschool.kitobbek.R
import uz.itschool.kitobbek.ui.components.TopBar

private val NavyDark = Color(0xFF0D1B4B)
private val LightBlue = Color(0xFFB3E5FC)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCommentScreen(
    bookName: String,
    onBackClick: () -> Unit,
    onSendClick: (String, Int) -> Unit
) {
    var rating by remember { mutableIntStateOf(1) }
    var commentText by remember { mutableStateOf("") }

    val emojis = listOf(
        "🙁", // 1
        "😐", // 2
        "🙂", // 3
        "😊", // 4
        "😁"  // 5
    )

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopBar(
                title = "O'z sharhingizni yozib qoldiring",
                onMenuClick = onBackClick,
                menuIcon = Icons.AutoMirrored.Filled.ArrowBack
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(NavyDark),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "“$bookName” romani sizga qanchalik manzur keldi?",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 40.dp)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-50).dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Big Emoji based on rating
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE0E0E0))
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = emojis[rating - 1],
                        fontSize = 80.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Kitobni baholang",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = NavyDark
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Star Rating
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(5) { index ->
                        val isSelected = index < rating
                        IconButton(onClick = { rating = index + 1 }) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = if (isSelected) LightBlue else Color.LightGray,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Kitob haqida o'z fikringizni yozib qoldiring",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = commentText,
                    onValueChange = { commentText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .padding(horizontal = 24.dp),
                    placeholder = { Text("Mening ushbu kitob haqidagi fikrim...", color = Color.LightGray) },
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { onSendClick(commentText, rating) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 48.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NavyDark),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "Jo'natish", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }

                TextButton(onClick = onBackClick) {
                    Text(text = "Bekor qilish", color = Color.Gray)
                }
                
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
