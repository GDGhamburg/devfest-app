package de.devfest.data.github

import de.devfest.data.ContributorManager
import de.devfest.data.github.GitHubFactory.provideGitHubApi
import de.devfest.model.Contributor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rx.Observable
import rx.Subscriber

class GithubContributorManager: ContributorManager {

    private val github: GitHubService by lazy { provideGitHubApi(OkHttpClient.Builder().build()) }
    private var contributors: List<Contributor> = emptyList()
    private var lastFetchTime = 0

    private var subscriber: Subscriber<in List<Contributor>>? = null


    override fun getContributors(): Observable<List<Contributor>> {
        return Observable.create({ subscriber ->
            this.subscriber = subscriber

            if (contributors.isEmpty()
                    || lastFetchTime < System.currentTimeMillis() - FETCH_INTERVAL) {
                fetchGitHubContributors()
            }
        })
    }

    private fun fetchGitHubContributors() {
        github.getContributors(GITHUB_ORGANIZATION, GITHUB_REPO)
                .enqueue(object : Callback<List<Contributor>> {
                    override fun onResponse(call: Call<List<Contributor>>, response: Response<List<Contributor>>) {
                        if (response.isSuccessful) {
                            contributors = response.body()!!
                            subscriber!!.onNext(contributors)
                        }
                    }

                    override fun onFailure(call: Call<List<Contributor>>?, t: Throwable?) {

                    }
                })
    }

    companion object {
        private val GITHUB_ORGANIZATION = "gdghamburg"
        private val GITHUB_REPO = "devfest-app"
        private val FETCH_INTERVAL = 600000 // 5 minutes
    }
}