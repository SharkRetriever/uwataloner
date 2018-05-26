/*
 * Copyright 2018 - Yuanmeng Zhao (SharkRetriever)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package me.rayzz.uwataloner.apiobjectfactories

import com.eclipsesource.json.JsonArray
import com.eclipsesource.json.JsonValue
import me.rayzz.uwataloner.apiobjects.Course
import me.rayzz.uwataloner.base.ExceptionStrings
import me.rayzz.uwataloner.base.JsonFieldExtractor
import me.rayzz.uwataloner.base.UWaterlooAPIRequestManager
import java.security.InvalidParameterException

object CourseFactory {
    private val webRequestManager = UWaterlooAPIRequestManager()

    fun generate(chosenSubject: String): List<Course> {
        try {
            val courseJson: JsonValue = webRequestManager.getWebPageJson("/courses/" + chosenSubject)
            return parseCoursesJson(courseJson)
        }
        catch (e: Exception) {
            throw InvalidParameterException(ExceptionStrings.INVALID_JSON_STATUS_STRING)
        }
    }

    private fun parseCoursesJson(courseJson: JsonValue): List<Course> {
        val courseJsonData: JsonValue? = courseJson.asObject().get("data")
        val courseJsonCourses: JsonArray? = courseJsonData?.asArray()
        val courseList = mutableListOf<Course>()

        if (courseJsonCourses == null) {
            throw InvalidParameterException(ExceptionStrings.INVALID_JSON_FORMAT_STRING)
        }
        else {
            // foreach subject:
            // [subject] (description)
            for (i in 0 until courseJsonCourses.size()) {
                val currentCourse: JsonValue = courseJsonCourses.get(i)
                val currentCourseSubject: String = JsonFieldExtractor.extractStringValue(currentCourse, "subject")
                val currentCourseNumber: String = JsonFieldExtractor.extractStringValue(currentCourse, "catalog_number")
                val currentCourseTitle: String = JsonFieldExtractor.extractStringValue(currentCourse, "title")

                if (currentCourseSubject.isBlank() || currentCourseNumber.isBlank() || currentCourseTitle.isBlank()) {
                    throw InvalidParameterException(ExceptionStrings.INVALID_JSON_FORMAT_STRING)
                }

                courseList.add(Course(currentCourseSubject, currentCourseNumber, currentCourseTitle))
            }
        }

        return courseList
    }
}