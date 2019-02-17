/*
 * Copyright 2018 - Yuanmeng Zhao (SharkRetriever)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package me.rayzz.uwataloner.apimodels

import me.rayzz.uwataloner.utilities.ExceptionStrings
import org.joda.time.DateTime

/**
 * Holds a course that takes place inside of a room
 * GET /buildings/{building}/{room}/courses.{format}
 */
class BuildingRoomCourse(
        val classNumber: Int,
        val subject: String,
        val catalogNumber: String,
        val title: String,
        val section: String,
        val weekdays: String,
        val startTime: String, // HH:mm
        val endTime: String,
        val startDate: String, // MM/dd
        val endDate: String,
        val enrollmentTotal: Int,
        val instructors: List<String>,
        val building: String,
        val room: String,
        val term: Int,
        val lastUpdated: String) // for example "2019-01-11T22:04:20-05:00"
{
    // Pass in a constant dateTime to prevent a for loop having later calls end up in the next day
    // returns true if the class starts OR ends today
    fun occursToday(todayDateTime: DateTime): Boolean {
        return startsToday(todayDateTime) || endsToday(todayDateTime)
    }

    private fun startsToday(todayDateTime: DateTime): Boolean {
        val todayDateString: String = todayDateTime.toString("MM/dd")
        if (startDate.isNotEmpty())
            return startDate == todayDateString

        // http://joda-time.sourceforge.net/apidocs/org/joda/time/format/DateTimeFormat.html
        val todayDayOfWeek: String = convertDayOfWeekToAcronym(todayDateTime.toString("e"))
        return weekdays.contains(todayDayOfWeek)
    }

    private fun endsToday(todayDateTime: DateTime): Boolean {
        val todayDateString: String = todayDateTime.toString("MM/dd")
        if (endDate.isNotEmpty())
            return endDate == todayDateString

        val todayDayOfWeek: String = convertDayOfWeekToAcronym(todayDateTime.toString("e"))
        return weekdays.contains(todayDayOfWeek)
    }

    fun endsDuringOrAfter(searchStartHour: Int, searchStartMinute: Int): Boolean {
        val endTimeHour: Int = endTime.split(":")[0].toInt()
        val endTimeMinute: Int = endTime.split(":")[1].toInt()
        return endTimeHour > searchStartHour || (endTimeHour == searchStartHour && endTimeMinute >= searchStartMinute)
    }

    fun getFirstStartDateTimeEndingDuringOrAfterNow(todayDateTime: DateTime): DateTime {
        // confirm first that the session starts today
        if (!startsToday(todayDateTime)) {
            throw IllegalArgumentException(ExceptionStrings.INVALID_PARAMETERS_STRING + "todayDateTime within getFirstStartDateTimeEndingDuringOrAfterNow")
        }

        val startHour: Int = startTime.split(":")[0].toInt()
        val startMinute: Int = startTime.split(":")[1].toInt()

        return todayDateTime.withTime(startHour, startMinute, 0, 0)
    }

    fun getFirstEndDateTimeEndingDuringOrAfterNow(todayDateTime: DateTime): DateTime {
        // confirm first that the session ends today
        if (!endsToday(todayDateTime)) {
            throw IllegalArgumentException(ExceptionStrings.INVALID_PARAMETERS_STRING + "todayDateTime within getFirstEndDateTimeEndingDuringOrAfterNow")
        }

        val startHour: Int = endTime.split(":")[0].toInt()
        val startMinute: Int = endTime.split(":")[1].toInt()

        return todayDateTime.withTime(startHour, startMinute, 0, 0)
    }

    private fun convertDayOfWeekToAcronym(dayOfWeekNumber: String): String {
        return when (dayOfWeekNumber)
        {
            "1" -> "M"
            "2" -> "T"
            "3" -> "W"
            "4" -> "Th"
            "5" -> "F"
            "6" -> "S"
            "7" -> "Su" // honestly, all rooms are available on a Sunday
            else -> throw IllegalArgumentException(ExceptionStrings.INVALID_PARAMETERS_STRING + "dayOfWeekNumber within convertDayOfWeekToAcronym")
        }
    }
}