package uz.itschool.kitobbek.data.remote.model.request

data class RegisterRequest(
    val username: String,
    val fullname: String,
    val email: String,
    val password: String
)
