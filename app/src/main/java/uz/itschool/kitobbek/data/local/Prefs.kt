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

    fun saveBookId(id: Int) {
        val savedIds = getSavedBookIds().toMutableSet()
        savedIds.add(id.toString())
        prefs.edit().putStringSet("saved_books", savedIds).apply()
    }

    fun removeBookId(id: Int) {
        val savedIds = getSavedBookIds().toMutableSet()
        savedIds.remove(id.toString())
        prefs.edit().putStringSet("saved_books", savedIds).apply()
    }

    fun getSavedBookIds(): Set<String> = prefs.getStringSet("saved_books", emptySet()) ?: emptySet()
    
    fun isBookSaved(id: Int): Boolean = getSavedBookIds().contains(id.toString())
}
