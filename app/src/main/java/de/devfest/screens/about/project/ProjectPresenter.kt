package de.devfest.screens.about.project

import dagger.Lazy
import de.devfest.data.ContributorManager
import de.devfest.mvpbase.BasePresenter
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class ProjectPresenter @Inject
constructor(private val contributorManager: Lazy<ContributorManager>) : BasePresenter<ProjectView>() {

    override fun attachView(mvpView: ProjectView) {
        super.attachView(mvpView)
        untilDetach(contributorManager.get().getContributors()
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ contributors -> view.onContributorsAvailable(contributors) },
                        { error -> view.onError(error) })
        )
    }
}