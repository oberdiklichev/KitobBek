package uz.itschool.kitobbek.data.remote.model.response

import com.google.gson.annotations.SerializedName

data class BookResponse(
    val id: Int,
    val name: String,
    @SerializedName("type_id")
    val typeId: Int,
    val file: String,
    val audio: String?,
    val year: String,
    val author: String,
    val status: Int,
    val reyting: Int,
    val description: String,
    val image: String,
    val lang: String,
    @SerializedName("count_page")
    val countPage: Int,
    val publisher: String
)
