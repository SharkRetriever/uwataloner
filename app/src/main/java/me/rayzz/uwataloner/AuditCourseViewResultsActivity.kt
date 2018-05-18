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
import com.eclipsesource.json.Json
import com.eclipsesource.json.JsonArray
import com.eclipsesource.json.JsonValue
import kotlinx.android.synthetic.main.activity_audit_course_view_results.*
import me.rayzz.uwataloner.base.ErrorRedirector
import me.rayzz.uwataloner.base.ExceptionStrings
import me.rayzz.uwataloner.base.JsonFieldExtractor
import me.rayzz.uwataloner.base.UWaterlooAPIRequestManager

class AuditCourseViewResultsActivity : AppCompatActivity() {

    private val webRequestManager = UWaterlooAPIRequestManager()
    private val chosenCourseString: String = "chosenCourse"
    private val chosenSubjectString: String = "chosenSubject"
    private val scheduleJsonString: String = "scheduleJson"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audit_course_view_results)

        val chosenSubject: String
        val chosenCourse: String
        val scheduleJson: JsonValue

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(chosenSubjectString) &&
                savedInstanceState.containsKey(chosenCourseString)) {
                chosenSubject = savedInstanceState.getString(chosenSubjectString)
                chosenCourse = savedInstanceState.getString(chosenCourseString)
                scheduleJson = Json.parse(savedInstanceState.getString(scheduleJsonString))
                // showResults(chosenSubject, chosenCourse)
                showResultsNew(scheduleJson)
            }
        }
        else {
            chosenSubject = intent.getStringExtra(chosenSubjectString)
            chosenCourse = intent.getStringExtra(chosenCourseString)
            scheduleJson = Json.parse(intent.getStringExtra(scheduleJsonString))
            // showResults(chosenSubject, chosenCourse)
            showResultsNew(scheduleJson)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        outState?.putString(chosenSubjectString, intent.getStringExtra(chosenSubjectString))
        outState?.putString(chosenCourseString, intent.getStringExtra(chosenCourseString))
        super.onSaveInstanceState(outState, outPersistentState)
    }

    private fun showResults(subject: String, catalogNumber: String) {
        // GET /courses/{subject}/{catalog_number}/schedule.json
        try {
            val courseJson: JsonValue = webRequestManager.getWebPageJson("/courses/" + subject + "/" +
                    catalogNumber + "/schedule")
            val parsedCoursesInfo: Array<String> = parseCourseJSON(courseJson)
            if (parsedCoursesInfo.isEmpty()) {
                val noResultsInfo: Array<String> = arrayOf("No results found for this course")
                auditCourseResultsList.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, noResultsInfo)
            }
            else {
                auditCourseResultsList.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, parsedCoursesInfo)
            }
        }
        catch (e: Exception) {
            ErrorRedirector.redirectError(this, ExceptionStrings.INVALID_JSON_STATUS_STRING)
        }
    }

    private fun showResultsNew(scheduleJson: JsonValue) {
        val parsedCoursesInfo: Array<String> = parseCourseJSON(scheduleJson)
        auditCourseResultsList.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, parsedCoursesInfo)
    }

    private fun parseCourseJSON(courseJson: JsonValue): Array<String> {
        val courseJsonData: JsonValue? = courseJson.asObject().get("data")
        val courseJsonArray: JsonArray? = courseJsonData?.asArray()
        val courseOfferingsList: MutableList<String> = mutableListOf<String>()

        if (courseJsonArray == null) {
            ErrorRedirector.redirectError(this, ExceptionStrings.INVALID_JSON_FORMAT_STRING)
        }
        else {
            // foreach result in courseJSONArray:
            /* subject catalog_number - title
               section
               {classes}
             */
            for (i in 0 until courseJsonArray.size()) {
                val currentCourseOffering: JsonValue = courseJsonArray.get(i)
                val currentSubject: String = JsonFieldExtractor.extractStringValue(currentCourseOffering, "subject")
                val currentCourseNumber: String = JsonFieldExtractor.extractStringValue(currentCourseOffering, "catalog_number")
                val currentCourseTitle: String = JsonFieldExtractor.extractStringValue(currentCourseOffering, "title")
                val currentCourseSection: String = JsonFieldExtractor.extractStringValue(currentCourseOffering, "section")
                val enrollmentCapacity: Int = currentCourseOffering.asObject().getInt("enrollment_capacity", -1)
                val enrollmentTotal: Int = currentCourseOffering.asObject().getInt("enrollment_total", -1)


                if (currentSubject.isBlank() || currentCourseNumber.isBlank() || currentCourseTitle.isBlank() || currentCourseSection.isBlank()) {
                    ErrorRedirector.redirectError(this, ExceptionStrings.INVALID_JSON_FORMAT_STRING)
                }
                // exclude online sections or test sections from this list
                if (currentCourseSection.startsWith("LEC 08") || currentCourseSection.startsWith("TST")) {
                    continue
                }

                val subjectHeading: String = "%s %s - %s".format(currentSubject, currentCourseNumber, currentCourseTitle)
                val parsedCurrentCourseSectionTimes: Array<String> = parseCourseSectionTimesJson(currentCourseOffering)

                // the section was cancelled or closed
                if (parsedCurrentCourseSectionTimes.isEmpty()) {
                    continue
                }

                val warningString = "WARNING: Section full"
                val enrollmentSizeString: String
                val seatString = "seat" + if (Math.abs(enrollmentCapacity - enrollmentTotal) == 1) "" else "s"
                if (enrollmentCapacity > 0 && enrollmentTotal > 0 && enrollmentCapacity <= enrollmentTotal) {
                    enrollmentSizeString = "%s (over by %d %s)".format(warningString, enrollmentTotal - enrollmentCapacity, seatString)
                }
                else {
                    enrollmentSizeString = "%d %s available".format(enrollmentCapacity - enrollmentTotal, seatString)
                }

                val courseOfferingStrings: Array<String> = arrayOf(subjectHeading,
                        currentCourseSection, enrollmentSizeString, parsedCurrentCourseSectionTimes.joinToString("\n"))
                courseOfferingsList.add(courseOfferingStrings.joinToString("\n"))
            }
        }

        return courseOfferingsList.toTypedArray()
    }

    // TODO: present results ordered by time, and discard results that have already passed
    private fun parseCourseSectionTimesJson(currentCourseOffering: JsonValue): Array<String> {
        val currentCourseSectionTimesData: JsonValue? = currentCourseOffering.asObject().get("classes")
        val currentCourseSectionTimesArray: JsonArray? = currentCourseSectionTimesData?.asArray()
        val courseSectionTimesList: MutableList<String> = mutableListOf<String>()

        // Template:
        // foreach class in offerings of given section:
        /*
           [date.weekdays] - [date.start_time] to [date.end_time] in [location.building] [location.room]
           Taught by [string.join " " instructors]
         */
        if (currentCourseSectionTimesArray == null) {
            ErrorRedirector.redirectError(this, ExceptionStrings.INVALID_JSON_FORMAT_STRING)
        }
        else {
            for (i in 0 until currentCourseSectionTimesArray.size()) {
                val currentCourseSectionTime: JsonValue = currentCourseSectionTimesArray.get(i)

                val date: JsonValue? = currentCourseSectionTime.asObject().get("date")
                val location: JsonValue? = currentCourseSectionTime.asObject().get("location")
                val building: String = JsonFieldExtractor.extractStringValue(location, "building")
                val room: String = JsonFieldExtractor.extractStringValue(location, "room")
                val isCancelled: Boolean = JsonFieldExtractor.extractBooleanValue(date, "is_cancelled")
                val isClosed: Boolean = JsonFieldExtractor.extractBooleanValue(date, "is_closed")

                if (isCancelled == true || isClosed == true) {
                    continue
                }

                if (date == null || location == null || building.isEmpty() || room.isEmpty()) {
                    ErrorRedirector.redirectError(this, ExceptionStrings.INVALID_JSON_FORMAT_STRING)
                }
                else {
                    val weekday: String = JsonFieldExtractor.extractStringValue(date, "weekdays")
                    val startTime: String = JsonFieldExtractor.extractStringValue(date, "start_time")
                    val endTime: String = JsonFieldExtractor.extractStringValue(date, "end_time")
                    val startDate: String =  JsonFieldExtractor.extractStringValue(date, "start_date")

                    // if (weekday.isBlank() && sectionOfferingPassed(startDate, startTime))
                    //    continue

                    val timing: String = if (startDate.isEmpty()) "%s - %s to %s".format(weekday, startTime, endTime)
                        else "%s, %s - %s to %s".format(weekday, startDate, startTime, endTime)
                    val locationString: String = "%s %s".format(building, room)

                    val instructorsData: JsonValue? = currentCourseSectionTime.asObject().get("instructors")
                    val instructorsArray: JsonArray? = instructorsData?.asArray()
                    val taughtByListString: String =
                        if (instructorsArray != null) {
                            val joinedInstructorsList: String = (0 until instructorsArray.size()).map(fun(it): String {
                                val instructor: JsonValue = instructorsArray.get(it)
                                if (instructor.isNull) {
                                    return ""
                                } else {
                                    val instructorName: String = instructorsArray.get(it).asString()
                                    val splitName: Array<String> = instructorName.split(",").toTypedArray()
                                    return "%s, %s".format(splitName[0], splitName[1])
                                }
                            }).filter { !it.isEmpty() }.joinToString("; ")
                            if (joinedInstructorsList.isNotEmpty()) joinedInstructorsList else "TBA"
                        }
                        else {
                            "TBA"
                        }

                    val courseSectionTimeString = "%s in %s\nProfs: %s".format(timing, locationString, taughtByListString)
                    courseSectionTimesList.add(courseSectionTimeString)
                }
            }
        }

        return courseSectionTimesList.toTypedArray()
    }

    fun auditAgainButtonOnClick(view: View) {
        val intent = Intent(this, AuditCourseChooseSubjectActivity::class.java)
        startActivity(intent)
    }

    fun auditReturnMenuButtonOnClick(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
