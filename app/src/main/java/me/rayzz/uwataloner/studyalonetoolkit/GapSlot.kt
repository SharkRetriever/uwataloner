/*
 * Copyright 2018 - Yuanmeng Zhao (SharkRetriever)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package me.rayzz.uwataloner.studyalonetoolkit

import org.joda.time.DateTime
import org.joda.time.Period

class GapSlot(val startDateTime: DateTime, val endDateTime: DateTime,
              val building: String, val room: String) {
    fun getDuration(): Period {
        return Period(startDateTime.millis, endDateTime.millis)
    }

    override fun toString(): String {
        val endDateHour: Int = endDateTime.hourOfDay
        val endDateMinute: Int = endDateTime.minuteOfHour
        val startDateHour: Int = startDateTime.hourOfDay
        val startDateMinute: Int = startDateTime.minuteOfHour
        val durationHour: Int = getDuration().hours
        val durationMinutes: Int = getDuration().minutes
        return when {
            (startDateHour > endDateHour || (startDateHour == endDateHour && startDateMinute > endDateMinute)) ->
                "%s %s\nAfter %d:%02d".format(
                        building, room, startDateHour, startDateMinute)
            else ->
                "%s %s\n%d:%02d - %d:%02d (for %d hrs %2d min)".format(
                        building, room, startDateHour, startDateMinute, endDateHour, endDateMinute, durationHour, durationMinutes)
        }
    }
}