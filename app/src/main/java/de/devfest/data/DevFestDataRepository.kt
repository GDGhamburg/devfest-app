package de.devfest.data

import de.devfest.model.Schedule
import io.reactivex.Observable

interface DevFestDataRepository {
    fun getSchedule(): Observable<Schedule>
}