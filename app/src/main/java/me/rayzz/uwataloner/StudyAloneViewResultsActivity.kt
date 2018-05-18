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
import com.eclipsesource.json.JsonArray
import com.eclipsesource.json.JsonValue
import kotlinx.android.synthetic.main.activity_study_alone_view_results.*
import me.rayzz.uwataloner.base.ErrorRedirector
import me.rayzz.uwataloner.base.ExceptionStrings
import me.rayzz.uwataloner.base.JsonFieldExtractor
import me.rayzz.uwataloner.base.UWaterlooAPIRequestManager
import me.rayzz.uwataloner.studyalonetoolkit.CourseSlot
import me.rayzz.uwataloner.studyalonetoolkit.GapSlot
import org.joda.time.DateTime
import java.util.*
import kotlin.collections.ArrayList

class StudyAloneViewResultsActivity : AppCompatActivity() {

    private val webRequestManager = UWaterlooAPIRequestManager()
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
            chosenBuilding = intent.getStringExtra(chosenBuildingString).split(" ")[0]
            chosenRoom = intent.getStringExtra(chosenRoomString)
            chosenTime = intent.getStringExtra(chosenTimeString)
        }
        else {
            chosenBuilding = savedInstanceState.getString(chosenBuildingString).split(" ")[0]
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
            val coursesInRoomJson: JsonValue = webRequestManager.getWebPageJson("/buildings/" + chosenBuilding + "/" +
                    chosenRoom + "/courses")

            val parsedCoursesInfo: Array<String> = parseCoursesInRoomJSON(coursesInRoomJson, chosenTimeHour, chosenTimeMinute)
            studyAloneCourseResultsList.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, parsedCoursesInfo)
        }
        catch (e: Exception) {
            ErrorRedirector.redirectError(this, ExceptionStrings.INVALID_JSON_STATUS_STRING)
        }
    }

    private fun parseCoursesInRoomJSON(coursesInRoomJson: JsonValue, chosenTimeHour: Int, chosenTimeMinute: Int): Array<String>
    {
        val coursesInRoomJsonCoursesData: JsonValue? = coursesInRoomJson.asObject().get("data")
        val coursesInRoomJSONCoursesArray: JsonArray? = coursesInRoomJsonCoursesData?.asArray()

        val courseTimes = ArrayList<CourseSlot>()
        val gapTimes = ArrayList<GapSlot>()
        val stringizedGapTimes = ArrayList<String>()

        if (coursesInRoomJSONCoursesArray == null) {
            ErrorRedirector.redirectError(this, ExceptionStrings.INVALID_JSON_FORMAT_STRING)
        }
        else {
            val currentDate: DateTime = DateTime.now()
            for (i in 0 until coursesInRoomJSONCoursesArray.size()) {
                val currentCourse: JsonValue = coursesInRoomJSONCoursesArray.get(i)

                val weekdaysString: String = JsonFieldExtractor.extractStringValue(currentCourse, "weekdays")
                val startTimeString: String = JsonFieldExtractor.extractStringValue(currentCourse, "start_time")
                val endTimeString: String = JsonFieldExtractor.extractStringValue(currentCourse, "end_time")
                val startDateString: String = JsonFieldExtractor.extractStringValue(currentCourse, "start_date")
                val endDateString: String = JsonFieldExtractor.extractStringValue(currentCourse, "end_date")
                val subject: String = JsonFieldExtractor.extractStringValue(currentCourse, "subject")
                val catalogNumber: String = JsonFieldExtractor.extractStringValue(currentCourse, "catalog_number")
                val title: String = JsonFieldExtractor.extractStringValue(currentCourse, "title")

                if ((weekdaysString.isBlank() && (startDateString.isBlank() || endDateString.isBlank())) ||
                    (startTimeString.isBlank() || endTimeString.isBlank() || subject.isBlank() || catalogNumber.isBlank() || title.isBlank())) {
                    ErrorRedirector.redirectError(this, ExceptionStrings.INVALID_JSON_FORMAT_STRING)
                }
                else {
                    val weekdays: List<Int> = findWeekDays(weekdaysString) // M-F
                    val startTime: Array<Int> = startTimeString.split(":").map { it.toInt() }.toTypedArray()  // hh:mm
                    val endTime: Array<Int> = endTimeString.split(":").map { it.toInt() }.toTypedArray()      // hh:mm
                    // might be null if weekdays is defined, so add check
                    val startDate: Array<Int> = if (weekdays.isEmpty())
                            startDateString.split("/").map { it.toInt() }.toTypedArray()
                        else arrayOf()  // mm/dd
                    val endDate: Array<Int> = if (weekdays.isEmpty())
                            endDateString.split("/").map { it.toInt() }.toTypedArray()
                        else arrayOf() // mm/dd

                    if (startDate.isEmpty()) {
                        val currentDayOfWeek: Int = currentDate.dayOfWeek
                        val adjustedDayOfWeek: Int = if (currentDayOfWeek == 6 || currentDayOfWeek == 7) 1 else currentDayOfWeek
                        val matchingDayOfWeek: Int? = weekdays.firstOrNull { it == adjustedDayOfWeek }
                        if (matchingDayOfWeek != null) {
                            val startDateTime = DateTime(currentDate.year, currentDate.monthOfYear, currentDate.dayOfMonth, startTime[0], startTime[1])
                            val endDateTime = DateTime(currentDate.year, currentDate.monthOfYear, currentDate.dayOfMonth, endTime[0], endTime[1])
                            val currentCourseSlot = CourseSlot(startDateTime, endDateTime, "", "", subject, catalogNumber, title)
                            courseTimes.add(currentCourseSlot)
                        }
                    } else {
                        // we already have startDate and endDate provided, so make a CourseSlot out of that
                        val startDateTime = DateTime(currentDate.year, startDate[0], startDate[1], startTime[0], startTime[1])
                        val endDateTime = DateTime(currentDate.year, endDate[0], endDate[1], endTime[0], endTime[1])
                        val currentDay: Int = DateTime.now().dayOfMonth

                        if ((startDateTime.dayOfMonth == currentDay && startDateTime.isAfterNow) ||
                                (endDateTime.dayOfMonth == currentDay && endDateTime.isAfterNow)) {
                            val currentCourseSlot = CourseSlot(startDateTime, endDateTime, "", "", subject, catalogNumber, title)
                            courseTimes.add(currentCourseSlot)
                        }
                    }
                }
            }

            courseTimes.sortBy { it.startDateTime.millis }

            // calculate all gaps
            for (i in 0 until courseTimes.size - 1) {
                val currentCourse: CourseSlot = courseTimes.get(i)
                val nextCourse: CourseSlot = courseTimes.get(i + 1)

                if (currentCourse.doesNotShareTimesWith(nextCourse) &&
                    nextCourse.startDateTime.minusMinutes(11).isAfter(currentCourse.endDateTime)) {
                    val gap = GapSlot(currentCourse.endDateTime, nextCourse.startDateTime, "", "")
                    gapTimes.add(gap)
                }
            }

            // reformat as string
            gapTimes.forEach { it ->
                val startDateHour: Int = it.startDateTime.hourOfDay
                val startDateMinute: Int = it.startDateTime.minuteOfHour
                val endDateHour: Int = it.endDateTime.hourOfDay
                val endDateMinute: Int = it.endDateTime.minuteOfHour
                val durationHour: Int = it.getDuration().hours
                val durationMinutes: Int = it.getDuration().minutes
                val formattedString: String = "%2d:%02d - %2d:%02d (for %2dh%02d)".format(
                        startDateHour, startDateMinute, endDateHour, endDateMinute, durationHour, durationMinutes)
                stringizedGapTimes.add(formattedString)
            }
        }

        return stringizedGapTimes.toTypedArray()
    }

    private fun findWeekDays(smushedWeekdays: String): List<Int> {
        val weekdays = LinkedList<Int>()
        if (smushedWeekdays.contains("M"))
            weekdays.add(1)
        if (smushedWeekdays.contains("T"))
            weekdays.add(2)
        if (smushedWeekdays.contains("W"))
            weekdays.add(3)
        if (smushedWeekdays.contains("Th"))
            weekdays.add(4)
        if (smushedWeekdays.contains("F"))
            weekdays.add(5)
        return weekdays
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
