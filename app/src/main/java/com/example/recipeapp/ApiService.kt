package com.example.recipeapp

import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


object RetrofitInstance {
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder().baseUrl("https://www.themealdb.com/api/json/v1/1/")
        .addConverterFactory(GsonConverterFactory.create()).build()


    val api: ApiService by lazy { retrofit.create(ApiService::class.java) }

}

interface ApiService {

    @GET("categories.php")
    suspend fun getCategories(): CategoriesResponse


    @GET("search.php")
    suspend fun searchRecipe(@Query("s") query: String): RecipeResponse

    @GET("search.php")
    suspend fun getMealsByFirstLetter(@Query("f") letter: String): MealResponse

    @GET("filter.php")
    suspend fun getMealsByIngredient(@Query("i") ingredient: String): MealResponse

}