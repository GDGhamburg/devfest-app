package de.devfest.data.github

import de.devfest.model.Contributor
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubService {

    @GET("repos/{owner}/{repo}/contributors")
    fun getContributors(@Path("owner") user: String,
                        @Path("repo") repo: String): Call<List<Contributor>>
}