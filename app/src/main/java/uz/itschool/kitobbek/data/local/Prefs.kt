package uz.itschool.kitobbek.data.local

import android.content.Context
import android.content.SharedPreferences

class Prefs(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("kitobbek_prefs", Context.MODE_PRIVATE)

    fun saveUser(id: Int, name: String, email: String) {
        prefs.edit().apply {
            putInt("userId", id)
            putString("name", name)
            putString("email", email)
            putBoolean("is_registered", true)
            apply()
        }
    }

    fun getUserId(): Int = prefs.getInt("userId", 0)

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

    fun addToReading(id: Int) {
        val readingIds = getReadingBookIds().toMutableSet()
        if (!getReadBookIds().contains(id.toString())) {
            readingIds.add(id.toString())
            prefs.edit().putStringSet("reading_books", readingIds).apply()
        }
    }

    fun addToRead(id: Int) {
        val readIds = getReadBookIds().toMutableSet()
        readIds.add(id.toString())
        
        val readingIds = getReadingBookIds().toMutableSet()
        readingIds.remove(id.toString())
        
        prefs.edit().apply {
            putStringSet("read_books", readIds)
            putStringSet("reading_books", readingIds)
            apply()
        }
    }

    fun getReadingBookIds(): Set<String> = prefs.getStringSet("reading_books", emptySet()) ?: emptySet()
    fun getReadBookIds(): Set<String> = prefs.getStringSet("read_books", emptySet()) ?: emptySet()

    fun saveQuote(bookId: Int, page: Int, text: String) {
        val quotes = getQuotes(bookId).toMutableList()
        quotes.add("$page|$text")
        prefs.edit().putStringSet("quotes_$bookId", quotes.toSet()).apply()
    }

    fun getQuotes(bookId: Int): List<String> {
        return prefs.getStringSet("quotes_$bookId", emptySet())?.toList() ?: emptyList()
    }
}
