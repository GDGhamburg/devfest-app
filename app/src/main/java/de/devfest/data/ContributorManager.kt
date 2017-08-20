package de.devfest.data

import de.devfest.model.Contributor
import rx.Observable

interface ContributorManager {

    fun getContributors(): Observable<List<Contributor>>
}