package uz.itschool.kitobbek.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.itschool.kitobbek.R

private val NavyDark = Color(0xFF0D1B4B)
private val SelectedItemBg = Color(0xFF1A237E)

@Composable
fun AppDrawer(
    userName: String,
    userEmail: String,
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    onCloseDrawer: () -> Unit
) {
    ModalDrawerSheet(
        drawerContainerColor = Color.White,
        drawerShape = RoundedCornerShape(topEnd = 0.dp, bottomEnd = 0.dp),
        modifier = Modifier.fillMaxHeight().width(300.dp)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(NavyDark)
                .padding(top = 40.dp, bottom = 24.dp, start = 24.dp, end = 24.dp)
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.profile_ico),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = userName,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = userEmail,
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
            }
        }

        // Menu Items
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            item {
                DrawerItem(
                    iconRes = R.drawable.book_ico,
                    label = "Bosh sahifa",
                    isSelected = currentRoute == "home",
                    onClick = { onNavigate("home"); onCloseDrawer() }
                )
            }
            item {
                DrawerItem(
                    iconRes = R.drawable.search_ico,
                    label = "Qidiruv",
                    isSelected = currentRoute == "search",
                    onClick = { onNavigate("search"); onCloseDrawer() }
                )
            }
            item {
                DrawerItem(
                    iconRes = R.drawable.feather_ico,
                    label = "Maqolalar",
                    isSelected = currentRoute == "write",
                    onClick = { onNavigate("write"); onCloseDrawer() }
                )
            }
            item {
                DrawerItem(
                    iconRes = R.drawable.bookmark_ico,
                    label = "Saqlangan kitoblar",
                    isSelected = currentRoute == "bookmarks",
                    onClick = { onNavigate("bookmarks"); onCloseDrawer() }
                )
            }
            item {
                DrawerItem(
                    iconRes = R.drawable.internet_ico,
                    label = "Tilni o'zgartiring",
                    isSelected = currentRoute == "language",
                    onClick = { onNavigate("language"); onCloseDrawer() }
                )
            }

            item { HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp), color = Color.LightGray.copy(alpha = 0.5f)) }

            item {
                DrawerItem(
                    iconRes = R.drawable.telegram_ico,
                    label = "Telegram Kanalimiz",
                    isSelected = false,
                    onClick = { /* Open Telegram */ onCloseDrawer() }
                )
            }
            item {
                DrawerItem(
                    iconRes = R.drawable.instagram_ico,
                    label = "Instagram do'konimiz",
                    isSelected = false,
                    onClick = { /* Open Instagram */ onCloseDrawer() }
                )
            }

            item { HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp), color = Color.LightGray.copy(alpha = 0.5f)) }

            item {
                DrawerItem(
                    iconRes = R.drawable.share_ico,
                    label = "Ulashish",
                    isSelected = false,
                    onClick = { /* Share App */ onCloseDrawer() }
                )
            }
            item {
                DrawerItem(
                    iconRes = R.drawable.star_ico,
                    label = "Bizga baho bering",
                    isSelected = false,
                    onClick = { /* Rate App */ onCloseDrawer() }
                )
            }

            item { HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp), color = Color.LightGray.copy(alpha = 0.5f)) }

            item {
                DrawerItem(
                    iconRes = R.drawable.log_out_ico,
                    label = "Hisobdan chiqish",
                    isSelected = false,
                    onClick = { /* Logout */ onCloseDrawer() }
                )
            }
        }
    }
}

@Composable
fun DrawerItem(
    iconRes: Int,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        color = if (isSelected) SelectedItemBg else Color.Transparent,
        shape = RoundedCornerShape(50.dp)
    ) {
        Row(
            modifier = Modifier
                .clickable { onClick() }
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = if (isSelected) Color.White else NavyDark,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(24.dp))
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                color = if (isSelected) Color.White else NavyDark
            )
        }
    }
}
