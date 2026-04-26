package uz.itschool.kitobbek.data.remote.model.response

import com.google.gson.annotations.SerializedName

data class CategoryResponse(
    @SerializedName("type_name")
    val typeName: String
)
