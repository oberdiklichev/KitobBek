package uz.itschool.kitobbek.data.local

import android.content.Context
import android.content.SharedPreferences

class Prefs(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("kitobbek_prefs", Context.MODE_PRIVATE)

    fun saveUser(name: String, surname: String, email: String) {
        prefs.edit().apply {
            putString("name", name)
            putString("surname", surname)
            putString("email", email)
            putBoolean("is_registered", true)
            apply()
        }
    }

    fun isRegistered(): Boolean = prefs.getBoolean("is_registered", false)
    
    fun getUserName(): String = prefs.getString("name", "") ?: ""
    fun getUserEmail(): String = prefs.getString("email", "") ?: ""
}
