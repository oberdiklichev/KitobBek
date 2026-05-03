package uz.itschool.kitobbek.ui.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.itschool.kitobbek.R

private val NavyDark = Color(0xFF0D1B4B)
private val LightBlue = Color(0xFFB3E5FC)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onRegisterClick: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        
        // Logo
        Icon(
            painter = painterResource(id = R.drawable.book_ico),
            contentDescription = null,
            tint = NavyDark,
            modifier = Modifier.size(60.dp)
        )
        Text(
            text = "HandyBook",
            color = NavyDark,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(48.dp))
        
        Text(
            text = "Hisobingizga kiring",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Email
        Text(text = "Email", modifier = Modifier.fillMaxWidth(), color = Color.Gray)
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("abcdef@gmail.com") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "Parol", color = Color.Gray)
            Text(
                text = "Parolingizni unutdingizmi?",
                color = LightBlue.copy(alpha = 0.8f),
                fontSize = 12.sp,
                modifier = Modifier.clickable { }
            )
        }
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = rememberMe, onCheckedChange = { rememberMe = it })
            Text(text = "Meni eslab qol", color = Color.Gray, fontSize = 14.sp)
        }

        if (errorMessage != null) {
            Text(text = errorMessage!!, color = Color.Red, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                // Har qanday login/parolni xato deydigan qil
                errorMessage = "Login yoki parol xato"
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LightBlue),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "Kirish", color = NavyDark, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))
        
        Text(text = "yoki quyidagilar orqali kiring", color = Color.LightGray, fontSize = 12.sp)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            SocialIcon(R.drawable.facebook_ico)
            SocialIcon(R.drawable.google_ico)
            SocialIcon(R.drawable.apple_ico)
        }

        Spacer(modifier = Modifier.weight(1f))

        Row {
            Text(text = "Hisobingiz yo'qmi? ", color = Color.Gray)
            Text(
                text = "Ro'yxatdan o'tish",
                color = LightBlue,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onRegisterClick() }
            )
        }
    }
}

@Composable
fun SocialIcon(iconRes: Int) {
    Surface(
        modifier = Modifier.size(48.dp),
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f))
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color.Unspecified
            )
        }
    }
}
