/*
 * Copyright 2018 - Yuanmeng Zhao (SharkRetriever)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package me.rayzz.uwataloner

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_study_alone_view_results.*
import me.rayzz.uwataloner.apiobjectfactories.BuildingCourseFactory
import me.rayzz.uwataloner.apiobjects.BuildingCourse
import me.rayzz.uwataloner.base.ErrorRedirector
import me.rayzz.uwataloner.base.ExceptionStrings
import me.rayzz.uwataloner.studyalonetoolkit.GapSlot
import org.joda.time.DateTime
import org.joda.time.Period

class StudyAloneViewResultsActivity : AppCompatActivity() {

    private val chosenBuildingString: String = "chosenBuilding"
    private val chosenRoomString: String = "chosenRoom"
    private val chosenTimeString: String = "chosenTime"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_study_alone_view_results)

        val chosenBuilding: String
        val chosenRoom: String
        val chosenTime: String
        val chosenTimeHour: Int
        val chosenTimeMinute: Int

        if (savedInstanceState == null) {
            chosenBuilding = intent.getStringExtra(chosenBuildingString)
            chosenRoom = intent.getStringExtra(chosenRoomString)
            chosenTime = intent.getStringExtra(chosenTimeString)
        }
        else {
            chosenBuilding = savedInstanceState.getString(chosenBuildingString)
            chosenRoom = savedInstanceState.getString(chosenRoomString)
            chosenTime = savedInstanceState.getString(chosenTimeString)
        }

        chosenTimeHour = chosenTime.split(":")[0].trimStart().toInt()
        chosenTimeMinute = chosenTime.split(":")[1].toInt()

        showResults(chosenBuilding, chosenRoom, chosenTimeHour, chosenTimeMinute)
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        outState?.putString(chosenBuildingString, intent.getStringExtra(chosenBuildingString))
        outState?.putString(chosenRoomString, intent.getStringExtra(chosenRoomString))
        outState?.putString(chosenTimeString, intent.getStringExtra(chosenTimeString))
        super.onSaveInstanceState(outState, outPersistentState)
    }

    private fun showResults(chosenBuilding: String, chosenRoom: String, chosenTimeHour: Int, chosenTimeMinute: Int) {
        // first we have to find all rooms in chosenBuilding that had a lecture in them this term
        // then we have to create a schedules list for each course and find the gaps
        // /buildings/{building_code}/{room}/courses with custom data structure
        // we need a way to get rooms per building
        // note: chosenRoom might be empty
        try {
            val coursesInRoom: List<BuildingCourse> = BuildingCourseFactory.generate(chosenBuilding, chosenRoom)
            val parsedCoursesInfo: Array<String> = findAndListGaps(coursesInRoom, chosenTimeHour, chosenTimeMinute)
            studyAloneCourseResultsList.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, parsedCoursesInfo)
        }
        catch (e: Exception) {
            e.printStackTrace(System.err)
            ErrorRedirector.redirectError(this, ExceptionStrings.INVALID_JSON_STATUS_STRING, e)
        }
    }

    private fun findAndListGaps(coursesInRoom: List<BuildingCourse>, chosenTimeHour: Int, chosenTimeMinute: Int): Array<String>
    {
        val gapTimes: MutableList<GapSlot> = mutableListOf()
        val sortedCourses: Array<BuildingCourse> = coursesInRoom.filter{ it.sectionDateTime.occursToday() }.sortedWith(compareBy({ it.room }, { it.sectionDateTime.asNextOccurrence() })).toTypedArray()

        // calculate all gaps
        for (i in 0 until sortedCourses.size - 1) {
            val currentCourse: BuildingCourse = sortedCourses.get(i)
            val nextCourse: BuildingCourse = sortedCourses.get(i + 1)

            // TODO: list rooms that are available all day, if chosenRoom is empty

            // gaps that have yet to occur, during the day (lecture in progress)
            if (!currentCourse.sectionDateTime.hasPassed(chosenTimeHour, chosenTimeMinute) &&
                currentCourse.room == nextCourse.room &&
                nextCourse.sectionDateTime.asNextOccurrence().minusMinutes(11).isAfter(currentCourse.sectionDateTime.asNextOccurrenceEnd())) {
                val gap = GapSlot(currentCourse.sectionDateTime.asNextOccurrenceEnd(),
                        nextCourse.sectionDateTime.asNextOccurrence(),
                        currentCourse.building, currentCourse.room)
                gapTimes.add(gap)
            }

            // gaps that occur between the requested time and the first class of a given room
            if ((i == 0 && !currentCourse.sectionDateTime.hasPassed(chosenTimeHour, chosenTimeMinute)) ||
                (currentCourse.room != nextCourse.room && !nextCourse.sectionDateTime.hasPassed(chosenTimeHour, chosenTimeMinute))) {
                val currentTime = DateTime.now()
                val requestedStudyTime = DateTime(currentTime.year, currentTime.monthOfYear, currentTime.dayOfMonth, chosenTimeHour, chosenTimeMinute)

                // stolen from SectionDateTime
                val curActualDay: Int = currentTime.dayOfWeek
                val daysToAdd: Int = if (curActualDay >= 6) 8 - curActualDay else 0
                val daysToAddAsDuration = Period(0, 0, 0, daysToAdd, 0, 0, 0, 0)

                val dayAdjustedRequestedStudyTime = requestedStudyTime.withPeriodAdded(daysToAddAsDuration, 1)
                val gap = if (i == 0)
                        GapSlot(dayAdjustedRequestedStudyTime, currentCourse.sectionDateTime.asNextOccurrence(), currentCourse.building, currentCourse.room)
                    else
                        GapSlot(dayAdjustedRequestedStudyTime, nextCourse.sectionDateTime.asNextOccurrence(), nextCourse.building, nextCourse.room)
                if (gap.getDuration().minutes >= 20)
                    gapTimes.add(gap)
            }

            // gaps that occur between the requested time and the end of the day (all lectures completed in the given room)
            if ((i + 1 == sortedCourses.size - 1 && nextCourse.sectionDateTime.hasPassed(chosenTimeHour, chosenTimeMinute)) ||
                (currentCourse.room != nextCourse.room && currentCourse.sectionDateTime.hasPassed(chosenTimeHour, chosenTimeMinute))) {
                val currentTime = DateTime.now()
                val requestedStudyTime = DateTime(currentTime.year, currentTime.monthOfYear, currentTime.dayOfMonth, chosenTimeHour, chosenTimeMinute)
                val requestedStudyEnd = DateTime(currentTime.year, currentTime.monthOfYear, currentTime.dayOfMonth, 23, 59)

                // stolen from SectionDateTime
                val curActualDay: Int = currentTime.dayOfWeek
                val daysToAdd: Int = if (curActualDay >= 6) 8 - curActualDay else 0
                val daysToAddAsDuration = Period(0, 0, 0, daysToAdd, 0, 0, 0, 0)

                val dayAdjustedRequestedStudyTime = requestedStudyTime.withPeriodAdded(daysToAddAsDuration, 1)
                val dayAdjustedRequestedStudyTimeEnd = requestedStudyEnd.withPeriodAdded(daysToAddAsDuration, 1)
                val gap = if (i + 1 == sortedCourses.size - 1)
                        GapSlot(dayAdjustedRequestedStudyTime, dayAdjustedRequestedStudyTimeEnd, nextCourse.building, nextCourse.room)
                    else
                        GapSlot(dayAdjustedRequestedStudyTime, dayAdjustedRequestedStudyTimeEnd, currentCourse.building, currentCourse.room)
                gapTimes.add(gap)
            }

            // gaps that occur between the last class of the day and the end of the day (assuming last one failed)
            else if ((i + 1 == sortedCourses.size - 1) || (currentCourse.room != nextCourse.room)) {
                val currentTime = DateTime.now()
                val requestedStudyEnd = DateTime(currentTime.year, currentTime.monthOfYear, currentTime.dayOfMonth, 23, 59)

                // stolen from SectionDateTime
                val curActualDay: Int = currentTime.dayOfWeek
                val daysToAdd: Int = if (curActualDay >= 6) 8 - curActualDay else 0
                val daysToAddAsDuration = Period(0, 0, 0, daysToAdd, 0, 0, 0, 0)

                val dayAdjustedRequestedStudyTimeEnd = requestedStudyEnd.withPeriodAdded(daysToAddAsDuration, 1)
                val gap = if (i + 1 == sortedCourses.size - 1)
                    GapSlot(nextCourse.sectionDateTime.asNextOccurrenceEnd(), dayAdjustedRequestedStudyTimeEnd, nextCourse.building, nextCourse.room)
                else
                    GapSlot(currentCourse.sectionDateTime.asNextOccurrenceEnd(), dayAdjustedRequestedStudyTimeEnd, currentCourse.building, currentCourse.room)
                gapTimes.add(gap)
            }
        }

        // reformat as string
        gapTimes.sortBy { it.startDateTime }
        return gapTimes.map { it.toString() }.toTypedArray()
    }

    fun studyAloneAgainButtonOnClick(view: View) {
        val intent = Intent(this, StudyAloneChooseBuildingActivity::class.java)
        startActivity(intent)
    }

    fun studyAloneReturnMenuButtonOnClick(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
