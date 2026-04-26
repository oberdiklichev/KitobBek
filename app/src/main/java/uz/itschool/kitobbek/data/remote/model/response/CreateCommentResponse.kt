package uz.itschool.kitobbek.data.remote.model.response

import com.google.gson.annotations.SerializedName

data class CreateCommentResponse(
    val id: Int,
    @SerializedName("book_id")
    val bookId: String,
    @SerializedName("user_id")
    val userId: String,
    val text: String,
    val reyting: String
)
