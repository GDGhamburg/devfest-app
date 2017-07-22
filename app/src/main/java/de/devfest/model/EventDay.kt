package de.devfest.model

import org.threeten.bp.LocalDate

data class EventDay(val date: LocalDate,
                    val title: String,
                    val timeslots: List<TimeSlot>,
                    val tracks: List<String>)