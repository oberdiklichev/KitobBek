package uz.itschool.kitobbek.data.remote.model.response

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    val id: Int,
    @SerializedName("user name")
    val userName: String,
    @SerializedName("full name")
    val fullName: String,
    @SerializedName("access_token")
    val accessToken: String
)
