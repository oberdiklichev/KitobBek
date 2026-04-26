package uz.itschool.kitobbek.data.remote.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import uz.itschool.kitobbek.data.remote.model.request.CommentRequest
import uz.itschool.kitobbek.data.remote.model.request.LoginRequest
import uz.itschool.kitobbek.data.remote.model.request.RegisterRequest
import uz.itschool.kitobbek.data.remote.model.response.AuthResponse
import uz.itschool.kitobbek.data.remote.model.response.BookResponse
import uz.itschool.kitobbek.data.remote.model.response.CategoryResponse
import uz.itschool.kitobbek.data.remote.model.response.CommentResponse
import uz.itschool.kitobbek.data.remote.model.response.CreateCommentResponse

interface BookApi {

    @GET("book-api/")
    suspend fun getAllBooks(): List<BookResponse>

    @GET("book-api/view")
    suspend fun getBookById(@Query("id") id: Int): BookResponse

    @POST("book-api/login/")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("book-api/register/")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @GET("book-api/main-book/")
    suspend fun getMainBook(): BookResponse

    @GET("book-api/all-category/")
    suspend fun getAllCategories(): List<CategoryResponse>

    @GET("book-api/category")
    suspend fun getBooksByCategory(@Query("name") name: String): List<BookResponse>

    @GET("book-api/comment")
    suspend fun getBookComments(@Query("id") id: Int): List<CommentResponse>

    @POST("comment-api/create/")
    suspend fun createComment(@Body request: CommentRequest): CreateCommentResponse

    @GET("book-api/search-name")
    suspend fun searchBookByName(@Query("name") name: String): List<BookResponse>
}
