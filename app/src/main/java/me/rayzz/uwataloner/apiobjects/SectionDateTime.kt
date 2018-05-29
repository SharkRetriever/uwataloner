/*
 * Copyright 2018 - Yuanmeng Zhao (SharkRetriever)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package me.rayzz.uwataloner.apiobjects

import me.rayzz.uwataloner.base.CharacterRepository
import org.joda.time.DateTime
import org.joda.time.Period
import java.security.InvalidParameterException
import java.util.*

/**
 * Helper for Offering and BuildingCourse
 */
class SectionDateTime(val startTime: String, val endTime: String,
                      val weekdays: String, val startDate: String, val endDate: String) {
    fun isValid(): Boolean {
        return (startTime.isNotEmpty() && endTime.isNotEmpty() &&
                (weekdays.isNotEmpty() || (startDate.isNotEmpty() && endDate.isNotEmpty())))
    }

    private fun getWeekdays(): List<Int> {
        val weekdaysList = LinkedList<Int>()
        if (weekdays.contains("M"))
            weekdaysList.add(1)
        if (weekdays.contains("T"))
            weekdaysList.add(2)
        if (weekdays.contains("W"))
            weekdaysList.add(3)
        if (weekdays.contains("Th"))
            weekdaysList.add(4)
        if (weekdays.contains("F"))
            weekdaysList.add(5)
        return weekdaysList
    }

    private fun curSchoolDay(): Int {
        val curWeekday: Int = DateTime.now().dayOfWeek
        return if (curWeekday >= 6) 1 else curWeekday
    }

    /**
     * Return whether a course has passed the point of consideration or not
     * It has passed if its end time comes before the current time
     */
    fun hasPassed(chosenTimeHour: Int, chosenTimeMinute: Int): Boolean {
        val instance: DateTime = DateTime.now()
        val currentMonth: Int = instance.monthOfYear
        val currentDay: Int = instance.dayOfMonth

        if (weekdays.isEmpty()) {
            val endMonth: Int? = endDate.split("/")[0].toIntOrNull()
            val endDay: Int? = endDate.split("/")[1].toIntOrNull()
            val endHour: Int? = endDate.split(":")[0].toIntOrNull()
            val endMinute: Int? = endDate.split(":")[1].toIntOrNull()
            if (endMonth == null || endDay == null || endHour == null || endMinute == null)
                throw InvalidParameterException("Invalid date given!")
            else if (currentMonth > endMonth) {
                return true
            }
            else if (currentMonth == endMonth) {
                if (currentDay > endDay) {
                    return true
                }
                else if (currentDay == endDay) {
                    return if (chosenTimeHour > endHour) {
                        true
                    }
                    else {
                        (chosenTimeHour == endHour && chosenTimeMinute > endMinute)
                    }
                }
            }
        }

        return false
    }

    fun occursToday(): Boolean {
        val instance: DateTime = DateTime.now()
        val currentMonth: Int = instance.monthOfYear
        val currentDay: Int = instance.dayOfMonth

        if (weekdays.isEmpty()) {
            if (startDate.split("/").size != 2 || endDate.split("/").size != 2) {
                throw InvalidParameterException("Invalid date given!")
            }
            val startMonth: Int? = startDate.split("/")[0].toIntOrNull()
            val startDay: Int? = startDate.split("/")[1].toIntOrNull()
            val endMonth: Int? = endDate.split("/")[0].toIntOrNull()
            val endDay: Int? = endDate.split("/")[1].toIntOrNull()
            if (startMonth == null || startDay == null || endMonth == null || endDay == null)
                throw InvalidParameterException("Invalid date given!")
            else
                return (currentMonth == startMonth && currentDay == startDay) ||
                       (currentMonth == endMonth && currentDay == endDay)
        }
        else {
            val weekdaysList: List<Int> = getWeekdays()
            val curday: Int = curSchoolDay()
            return weekdaysList.any { it == curday }
        }
    }

    private fun toDateTime(date: String, time: String): DateTime {
        val instance: DateTime = DateTime.now()
        val currentYear: Int = instance.year

        if (time.split(":").size != 2) {
            throw InvalidParameterException("Invalid time given!")
        }
        val startHour: Int? = time.split(":")[0].toIntOrNull()
        val startMinute: Int? = time.split(":")[1].toIntOrNull()
        if (startHour == null || startMinute == null) {
            throw InvalidParameterException("Invalid time given!")
        }

        if (weekdays.isEmpty()) {
            if (date.split("/").size != 2) {
                throw InvalidParameterException("Invalid time given!")
            }
            val startMonth: Int? = date.split("/")[0].toIntOrNull()
            val startDay: Int? = date.split("/")[1].toIntOrNull()
            if (startMonth == null || startDay == null) {
                throw InvalidParameterException("Invalid date given!")
            }
            return DateTime(currentYear, startMonth, startDay, startHour, startMinute)
        }
        else {
            val currentMonth: Int = instance.monthOfYear
            val currentDay: Int = instance.dayOfMonth

            val weekdaysList: List<Int> = getWeekdays()
            // the current school day, where weekend corresponds to monday
            val curSchoolDay: Int = curSchoolDay()
            // return the first weekday that is >= curday
            val firstMatchingWeekday: Int = weekdaysList.first { it >= curSchoolDay }
            // return the corresponding day of month, but note this might carry over a month or year
            val curActualDay: Int = DateTime.now().dayOfWeek
            val daysToAdd: Int = if (firstMatchingWeekday >= curActualDay) firstMatchingWeekday - curActualDay
                                 else 7 - (curActualDay - firstMatchingWeekday)
            val daysToAddAsDuration = Period(0, 0, 0, daysToAdd, 0, 0, 0, 0)

            // 1 indicates add once
            return DateTime(currentYear, currentMonth, currentDay, startHour, startMinute).withPeriodAdded(daysToAddAsDuration, 1)
        }
    }

    fun asNextOccurrence(): DateTime {
        return toDateTime(startDate, startTime)
    }

    fun asNextOccurrenceEnd(): DateTime {
        return toDateTime(endDate, endTime)
    }

    override fun toString(): String {
        return if (weekdays.isEmpty()) {
            "%s %s %s, %s %s %s".format(startDate, CharacterRepository.EN_DASH, endDate,
                    startTime, CharacterRepository.EN_DASH, endTime)
        }
        else if (startDate.isEmpty()) {
            "%s, %s %s %s".format(weekdays, startTime, CharacterRepository.EN_DASH, endTime)
        }
        else {
            "%s %s %s (%s), %s %s %s".format(startDate, CharacterRepository.EN_DASH, endDate,
                    weekdays, startTime, CharacterRepository.EN_DASH, endTime)
        }
    }
}