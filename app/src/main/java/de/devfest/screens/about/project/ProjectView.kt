package de.devfest.screens.about.project

import de.devfest.model.Contributor
import de.devfest.mvpbase.MvpBase

interface ProjectView: MvpBase.View {

    fun onContributorsAvailable(contributors: List<Contributor>)

    override fun onError(error: Throwable)
}