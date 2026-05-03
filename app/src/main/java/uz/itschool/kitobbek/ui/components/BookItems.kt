package uz.itschool.kitobbek.ui.components

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import uz.itschool.kitobbek.data.remote.model.response.BookResponse

@Composable
fun BookCardItem(book: BookResponse, onClick: (BookResponse) -> Unit) {
    Box(
        modifier = Modifier
            .width(170.dp)
            .height(94.dp)
            .shadow(2.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .clickable { onClick(book) }
            .padding(10.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {

            AsyncImage(
                model = book.image,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(54.dp)
                    .height(74.dp)
                    .clip(RoundedCornerShape(6.dp))
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(74.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = book.name,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 16.sp
                )
                Text(
                    text = book.author,
                    fontSize = 11.sp,
                    color = Color(0xFF9E9E9E),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(Modifier.width(3.dp))
                    Text(text = book.reyting.toString(), fontSize = 12.sp, color = Color(0xFF616161))
                }
            }
        }
    }
}

@Composable
fun BookCoverItem(book: BookResponse, onClick: (BookResponse) -> Unit) {
    Column(
        modifier = Modifier
            .width(90.dp)
            .clickable { onClick(book) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = book.image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(90.dp)
                .height(116.dp)
                .shadow(2.dp, RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp))
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = book.name,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun BookListItem(book: BookResponse, onClick: (BookResponse) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(114.dp)
            .shadow(1.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .clickable { onClick(book) }
            .padding(12.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

            AsyncImage(
                model = book.image,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(64.dp)
                    .height(90.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(90.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = book.name, fontSize = 15.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(text = book.author, fontSize = 12.sp, color = Color(0xFF757575))
                Text(text = book.description, fontSize = 11.sp, color = Color(0xFF9E9E9E), maxLines = 2, overflow = TextOverflow.Ellipsis)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFC107), modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(3.dp))
                    Text(text = book.reyting.toString(), fontSize = 12.sp, color = Color(0xFF616161))
                }
            }
        }
    }
}
