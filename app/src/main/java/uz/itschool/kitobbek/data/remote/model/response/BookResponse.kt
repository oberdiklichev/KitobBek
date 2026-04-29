package uz.itschool.kitobbek.data.remote.model.response

import com.google.gson.annotations.SerializedName

data class BookResponse(
    val id: Int = 0,
    val name: String = "",
    @SerializedName("type_id")
    val typeId: Int = 0,
    val file: String = "",
    val audio: String? = null,
    val year: String = "",
    val author: String = "",
    val status: Int = 0,
    val reyting: Int = 0,
    val description: String = "",
    val image: String = "",
    val lang: String = "",
    @SerializedName("count_page")
    val countPage: Int = 0,
    val publisher: String = ""
)
