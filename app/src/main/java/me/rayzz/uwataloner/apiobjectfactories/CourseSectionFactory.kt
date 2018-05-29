/*
 * Copyright 2018 - Yuanmeng Zhao (SharkRetriever)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package me.rayzz.uwataloner.apiobjectfactories

import com.eclipsesource.json.JsonArray
import com.eclipsesource.json.JsonValue
import me.rayzz.uwataloner.apiobjects.CourseSection
import me.rayzz.uwataloner.apiobjects.Offering
import me.rayzz.uwataloner.apiobjects.SectionDateTime
import me.rayzz.uwataloner.base.ExceptionStrings
import me.rayzz.uwataloner.base.JsonFieldExtractor
import me.rayzz.uwataloner.base.UWaterlooAPIRequestManager
import java.security.InvalidParameterException

/**
 * Given a subject and course, generates the course schedule for every section of that
 * subject and course
 * GET /courses/{subject}/{catalog_number}/schedule.{format}
 */
object CourseSectionFactory {
    private val webRequestManager = UWaterlooAPIRequestManager()

    fun generate(subject: String, catalogNumber: String): List<CourseSection> {
        try {
            val coursesScheduleJson: JsonValue = webRequestManager.getWebPageJson("/courses/" + subject + "/" +
                    catalogNumber + "/schedule")

            return parseCourseJson(coursesScheduleJson)
        }
        catch (e: Exception) {
            throw InvalidParameterException(ExceptionStrings.INVALID_JSON_STATUS_STRING)
        }
    }

    private fun parseCourseJson(courseJson: JsonValue): List<CourseSection> {
        val courseJsonData: JsonValue? = courseJson.asObject().get("data")
        val courseJsonArray: JsonArray? = courseJsonData?.asArray()
        val courseSectionsList: MutableList<CourseSection> = mutableListOf()

        if (courseJsonArray == null) {
            throw InvalidParameterException(ExceptionStrings.INVALID_JSON_FORMAT_STRING)
        }
        else {
            /*
            class CourseSection(val subject: String, val catalogNumber: String,
                    val title: String, val section: String,
                    val enrollmentCapacity: Int, val enrollmentTotal: Int,
                    val offeringTimes: List<Offering>)
             */
            for (i in 0 until courseJsonArray.size()) {
                val currentCourseOffering: JsonValue = courseJsonArray.get(i)

                val subject: String = JsonFieldExtractor.extractStringValue(currentCourseOffering, "subject")
                val catalogNumber: String = JsonFieldExtractor.extractStringValue(currentCourseOffering, "catalog_number")
                val title: String = JsonFieldExtractor.extractStringValue(currentCourseOffering, "title")
                val section: String = JsonFieldExtractor.extractStringValue(currentCourseOffering, "section")

                if (subject.isEmpty() || catalogNumber.isEmpty() || title.isEmpty() || section.isEmpty()) {
                    throw InvalidParameterException(ExceptionStrings.INVALID_JSON_FORMAT_STRING)
                }

                if (!(section.startsWith("LEC") || section.startsWith("TUT") || section.startsWith("SEM"))) {
                    continue
                }

                val enrollmentCapacity: Int = JsonFieldExtractor.extractIntValue(currentCourseOffering, "enrollment_capacity")
                val enrollmentTotal: Int = JsonFieldExtractor.extractIntValue(currentCourseOffering,"enrollment_total")
                val parsedOfferings: List<Offering> = parseCourseOfferingsJson(currentCourseOffering)

                courseSectionsList.add(CourseSection(subject, catalogNumber, title,
                        section, enrollmentCapacity, enrollmentTotal, parsedOfferings))
            }
        }

        return courseSectionsList
    }

    private fun parseCourseOfferingsJson(currentCourseOffering: JsonValue): List<Offering> {
        val currentCourseSectionTimesData: JsonValue? = currentCourseOffering.asObject().get("classes")
        val currentCourseSectionTimesArray: JsonArray? = currentCourseSectionTimesData?.asArray()
        val courseOfferingsList: MutableList<Offering> = mutableListOf()

        /*
        class Offering(val sectionDateTime: SectionDateTime,
               val building: String, val room: String, val instructors: List<String>,
               val isTba: Boolean, val isCancelled: Boolean, val isClosed: Boolean)
         */
        if (currentCourseSectionTimesArray == null) {
            throw InvalidParameterException(ExceptionStrings.INVALID_JSON_FORMAT_STRING)
        }
        else {
            for (i in 0 until currentCourseSectionTimesArray.size()) {
                val currentCourseSectionTime: JsonValue = currentCourseSectionTimesArray.get(i)

                val date: JsonValue? = currentCourseSectionTime.asObject().get("date")
                val location: JsonValue? = currentCourseSectionTime.asObject().get("location")

                val building: String = JsonFieldExtractor.extractStringValue(location, "building")
                val room: String = JsonFieldExtractor.extractStringValue(location, "room")
                val isTba: Boolean = JsonFieldExtractor.extractBooleanValue(date, "is_tba")
                val isCancelled: Boolean = JsonFieldExtractor.extractBooleanValue(date, "is_cancelled")
                val isClosed: Boolean = JsonFieldExtractor.extractBooleanValue(date, "is_closed")
                val instructors: MutableList<String> = mutableListOf()

                if (!(isCancelled || isClosed || isTba) && (date == null || location == null)) {
                    throw InvalidParameterException(ExceptionStrings.INVALID_JSON_FORMAT_STRING)
                }
                else {
                    val weekdays: String = JsonFieldExtractor.extractStringValue(date, "weekdays")
                    val startTime: String = JsonFieldExtractor.extractStringValue(date, "start_time")
                    val endTime: String = JsonFieldExtractor.extractStringValue(date, "end_time")
                    val startDate: String = JsonFieldExtractor.extractStringValue(date, "start_date")
                    val endDate: String = JsonFieldExtractor.extractStringValue(date, "end_date")

                    val instructorsData: JsonValue? = currentCourseSectionTime.asObject().get("instructors")
                    val instructorsArray: JsonArray? = instructorsData?.asArray()
                    if (instructorsArray != null) {
                        for (j in 0 until instructorsArray.size()) {
                            val instructor: JsonValue = instructorsArray.get(j)
                            if (!instructor.isNull) {
                                val instructorName: String = instructorsArray.get(j).asString()
                                val splitName: Array<String> = instructorName.split(",").toTypedArray()
                                instructors.add("%s, %s".format(splitName[0], splitName[1]))
                            }
                        }
                    }

                    val sectionDateTime = SectionDateTime(startTime, endTime, weekdays, startDate, endDate)

                    // for now, don't add sections that aren't open
                    if (!(isTba || isCancelled || isClosed)) {
                        courseOfferingsList.add(Offering(sectionDateTime, building, room, instructors,
                                isTba, isCancelled, isClosed))
                    }
                }
            }
        }

        return courseOfferingsList
    }
}