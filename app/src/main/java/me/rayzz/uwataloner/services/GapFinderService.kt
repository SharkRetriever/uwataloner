/*
 * Copyright 2018 - Yuanmeng Zhao (SharkRetriever)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
import me.rayzz.uwataloner.apimodels.BuildingRoomCourse
import me.rayzz.uwataloner.viewmodels.GapSlot
import me.rayzz.uwataloner.viewmodels.GapSlotCollection
import org.joda.time.DateTime

object GapFinderService {
    // Assumption: all courses in the list either start or end today, where today is in America/Toronto time zone
    fun findGapsForCoursesWithinDay(chosenBuilding: String, chosenRoom: String, courses: List<BuildingRoomCourse>,
                                    todayChosenDateTime: DateTime): GapSlotCollection {
        val gaps: List<GapSlot> = findGapsForSingleRoomCourses(courses, todayChosenDateTime)

        return if (gaps.isNotEmpty()) {
            GapSlotCollection(gaps.sortedBy { it.startDateTime }.toTypedArray())
        }
        else {
            val endOfDayTime: DateTime = todayChosenDateTime.withTime(23, 59, 0, 0)
            GapSlotCollection(arrayOf(GapSlot(todayChosenDateTime, endOfDayTime, chosenBuilding, chosenRoom)))
        }
    }

    fun findGapsForMultipleRoomCoursesWithinDay(chosenBuilding: String, courses: List<BuildingRoomCourse>,
                                    todayChosenDateTime: DateTime, allRoomsInBuilding: List<String>): GapSlotCollection {
        // the courses should be separated into their own groups
        val coursesByRoom: Map<String, List<BuildingRoomCourse>> = courses.groupBy { it.room }
        val gaps: List<GapSlot> = coursesByRoom.entries.flatMap { findGapsForSingleRoomCourses(it.value, todayChosenDateTime) }

        val roomsWithoutCourses: Set<String> = allRoomsInBuilding.subtract(courses.map { it.room })
        val endOfDayTime: DateTime = todayChosenDateTime.withTime(23, 59, 0, 0)
        val allDayGaps: List<GapSlot> = roomsWithoutCourses.map { GapSlot(todayChosenDateTime, endOfDayTime, chosenBuilding, it) }

        return GapSlotCollection(gaps.plus(allDayGaps).sortedWith(compareBy(GapSlot::startDateTime, GapSlot::getDurationMillis, GapSlot::room)).toTypedArray())
    }

    private fun findGapsForSingleRoomCourses(roomCourses: List<BuildingRoomCourse>, todayDateTime: DateTime): List<GapSlot> {
        val sortedCourses: Array<BuildingRoomCourse> = roomCourses.sortedBy { it.getFirstStartDateTimeEndingDuringOrAfterNow(todayDateTime) }.toTypedArray()
        val gapTimes: MutableList<GapSlot> = mutableListOf()

        // calculate all gaps
        for (i in 0 until sortedCourses.size - 1) {
            val currentCourse: BuildingRoomCourse = sortedCourses[i]
            val nextCourse: BuildingRoomCourse = sortedCourses[i + 1]

            val currentCourseEndDateTime: DateTime = currentCourse.getFirstEndDateTimeEndingDuringOrAfterNow(todayDateTime)
            val nextCourseStartDateTime: DateTime = nextCourse.getFirstStartDateTimeEndingDuringOrAfterNow(todayDateTime)

            // skip 10-minute gaps
            if (nextCourseStartDateTime.minusMinutes(11).isBefore(currentCourseEndDateTime))
                continue

            gapTimes += GapSlot(currentCourseEndDateTime, nextCourseStartDateTime, currentCourse.building, currentCourse.room)
        }

        if (sortedCourses.isNotEmpty()) {
            val firstCourse: BuildingRoomCourse = sortedCourses[0]
            val firstCourseStartTime: DateTime = firstCourse.getFirstStartDateTimeEndingDuringOrAfterNow(todayDateTime)
            if (firstCourseStartTime.minusMinutes(11).isBefore(todayDateTime))
                gapTimes += GapSlot(todayDateTime, firstCourseStartTime, firstCourse.building, firstCourse.room)

            val lastCourse: BuildingRoomCourse = sortedCourses[sortedCourses.size - 1]
            val lastCourseEndDateTime: DateTime = lastCourse.getFirstEndDateTimeEndingDuringOrAfterNow(todayDateTime)
            val endOfDayTime: DateTime = todayDateTime.withTime(23, 59, 0, 0)
            if (!endOfDayTime.minusMinutes(11).isBefore(lastCourseEndDateTime))
                gapTimes += GapSlot(lastCourseEndDateTime, endOfDayTime, lastCourse.building, lastCourse.room)
        }

        gapTimes.sortBy { it.startDateTime }
        return gapTimes
    }
}