package uz.itschool.kitobbek.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.itschool.kitobbek.R
import uz.itschool.kitobbek.data.local.Prefs

private val NavyDark = Color(0xFF0D1B4B)
private val LightBlue = Color(0xFFB3E5FC)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onBackClick: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    
    val context = LocalContext.current
    val prefs = remember { Prefs(context) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Ro'yxatdan o'tish",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = NavyDark
            )
            Spacer(modifier = Modifier.weight(1.3f))
        }

        Spacer(modifier = Modifier.height(32.dp))

        AuthTextField(label = "Ism", value = name, onValueChange = { name = it }, placeholder = "Ali")
        Spacer(modifier = Modifier.height(16.dp))
        
        AuthTextField(label = "Familiya", value = surname, onValueChange = { surname = it }, placeholder = "Azizov")
        Spacer(modifier = Modifier.height(16.dp))
        
        AuthTextField(label = "Email", value = email, onValueChange = { email = it }, placeholder = "abcdef@gmail.com")
        Spacer(modifier = Modifier.height(16.dp))
        
        AuthTextField(label = "Parol", value = password, onValueChange = { password = it }, isPassword = true)
        Spacer(modifier = Modifier.height(16.dp))
        
        AuthTextField(label = "Parolni takrorlang", value = confirmPassword, onValueChange = { confirmPassword = it }, isPassword = true)

        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Tasdiqlash tugmasini bosish orqali siz Maxfiylik siyosati shartlariga rozilik bildirasiz",
            fontSize = 11.sp,
            color = Color.Gray,
            lineHeight = 14.sp
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = {
                if (name.isNotBlank() && email.isNotBlank() && password.isNotEmpty() && password == confirmPassword) {
                    prefs.saveUser(name, surname, email)
                    onRegisterSuccess()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LightBlue),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "Ro'yxatdan o'tish", color = NavyDark, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
        
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun AuthTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    isPassword: Boolean = false
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, color = Color.Black, fontWeight = FontWeight.Medium, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder, color = Color.LightGray) },
            singleLine = true,
            visualTransformation = if (isPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
            shape = RoundedCornerShape(8.dp)
        )
    }
}
