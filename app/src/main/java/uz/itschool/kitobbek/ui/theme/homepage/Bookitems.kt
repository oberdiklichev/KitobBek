package uz.itschool.kitobbek.ui.theme.homepage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun BookCardItem(book: Book, onClick: (Book) -> Unit) {
    Box(
        modifier = Modifier
            .width(170.dp)
            .shadow(2.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .clickable { onClick(book) }
            .padding(10.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {

            Box(
                modifier         = Modifier
                    .width(54.dp)
                    .height(74.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFFBBDEFB)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text     = book.name.take(1),
                    fontSize = 22.sp,
                    color    = Color(0xFF1565C0),
                    fontWeight = FontWeight.Bold
                )
            }

            Column(
                modifier            = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(book.name, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Text(book.author ?: "", fontSize = 11.sp, color = Color(0xFF9E9E9E), maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFC107), modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(3.dp))
                    Text(book.reyting.toString(), fontSize = 12.sp, color = Color(0xFF616161))
                }
            }
        }
    }
}


@Composable
fun BookCoverItem(book: Book, onClick: (Book) -> Unit) {
    Column(
        modifier            = Modifier.width(90.dp).clickable { onClick(book) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier         = Modifier
                .width(90.dp)
                .height(116.dp)
                .shadow(2.dp, RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFE3F2FD)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text       = book.name.take(1),
                fontSize   = 28.sp,
                color      = Color(0xFF1565C0),
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(Modifier.height(4.dp))
        Text(book.name, fontSize = 11.sp, fontWeight = FontWeight.Medium, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}


@Composable
fun BookListItem(book: Book, onClick: (Book) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(1.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .clickable { onClick(book) }
            .padding(12.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

            Box(
                modifier         = Modifier
                    .width(64.dp)
                    .height(90.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFBBDEFB)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text       = book.name.take(1),
                    fontSize   = 26.sp,
                    color      = Color(0xFF1565C0),
                    fontWeight = FontWeight.Bold
                )
            }

            Column(
                modifier            = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(book.name, fontSize = 15.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(book.author ?: "", fontSize = 12.sp, color = Color(0xFF757575))
                Text(
                    text     = book.description?.replace("\r\n", " ") ?: "",
                    fontSize = 11.sp,
                    color    = Color(0xFF9E9E9E),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFC107), modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(3.dp))
                    Text(book.reyting.toString(), fontSize = 12.sp, color = Color(0xFF616161))
                }
            }
        }
    }
}
