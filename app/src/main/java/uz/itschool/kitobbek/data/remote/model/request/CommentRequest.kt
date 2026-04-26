package uz.itschool.kitobbek.data.remote.model.request

import com.google.gson.annotations.SerializedName

data class CommentRequest(
    @SerializedName("book_id")
    val bookId: Int,
    @SerializedName("user_id")
    val userId: Int,
    val text: String,
    val reyting: Int
)
