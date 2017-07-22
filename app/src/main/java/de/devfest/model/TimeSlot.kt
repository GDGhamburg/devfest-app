package de.devfest.model

import org.threeten.bp.ZonedDateTime

data class TimeSlot(val startTime: ZonedDateTime, val endTime: ZonedDateTime, val sessions: List<String>)