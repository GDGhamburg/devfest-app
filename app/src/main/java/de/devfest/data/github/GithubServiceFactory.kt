package de.devfest.data.github

import com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GitHubFactory {

    private val API_URL = "https://api.github.com"

    private fun provideRestAdapter(okHttpClient: OkHttpClient) = Retrofit.Builder()
            .baseUrl(API_URL)
            .client(okHttpClient)
            .addConverterFactory(
                    GsonConverterFactory.create(
                            GsonBuilder()
                                    .setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES)
                                    .create()
                    )
            ).build()

    fun provideGitHubApi(okHttpClient: OkHttpClient) =
            provideRestAdapter(okHttpClient).create(GitHubService::class.java)
}