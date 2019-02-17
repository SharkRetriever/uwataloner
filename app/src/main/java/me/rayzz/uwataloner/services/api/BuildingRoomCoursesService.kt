/*
 * Copyright 2018 - Yuanmeng Zhao (SharkRetriever)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package me.rayzz.uwataloner.services.api

import com.eclipsesource.json.JsonArray
import com.eclipsesource.json.JsonValue
import me.rayzz.uwataloner.apimodels.BuildingRoomCourse
import me.rayzz.uwataloner.utilities.ExceptionStrings
import me.rayzz.uwataloner.utilities.JsonFieldExtractor
import me.rayzz.uwataloner.utilities.UWaterlooAPIRequestManager
import me.rayzz.uwataloner.scrapedapiobjects.BuildingRoomsListMap
import java.io.InvalidObjectException

/**
 * Given a building acronym and a room number, generates a list of courses that takes place
 * in that room
 * GET /buildings/{building}/{room}/courses.{format}
 */
object BuildingRoomCoursesService {
    private val webRequestManager = UWaterlooAPIRequestManager()

    fun getCourses(building: String, room: String): List<BuildingRoomCourse> {
        val buildingCode: String = building.split(" ")[0]

        if (room.isNotEmpty()) {
            val coursesInRoomJson: JsonValue = webRequestManager.getWebPageJson("buildings/" + buildingCode + "/" +
                    room + "/courses")

            return parseBuildingRoomCoursesJson(coursesInRoomJson)
        }
        else {
            val buildingRooms: List<String>? = BuildingRoomsListMap.getBuildingRoomsListMap()[building]

            val coursesInBuildingRoom: List<BuildingRoomCourse>? = buildingRooms?.flatMap {
                val coursesInRoomJson: JsonValue = webRequestManager.getWebPageJson("buildings/" + buildingCode + "/" +
                        it + "/courses")
                parseBuildingRoomCoursesJson(coursesInRoomJson)
            }

            return coursesInBuildingRoom ?: throw InvalidObjectException(ExceptionStrings.UNKNOWN_EXCEPTION_STRING)
        }
    }

    private fun parseBuildingRoomCoursesJson(buildingRoomCoursesJson: JsonValue): List<BuildingRoomCourse> {
        val coursesInRoomJsonCoursesData: JsonValue = buildingRoomCoursesJson.asObject().get("data")
        val coursesInRoomJsonCoursesArray: JsonArray = coursesInRoomJsonCoursesData.asArray()

        val buildingRoomCourseList: MutableList<BuildingRoomCourse> = mutableListOf()

        for (i in 0 until coursesInRoomJsonCoursesArray.size()) {
            val currentCourse: JsonValue = coursesInRoomJsonCoursesArray.get(i)
            if (currentCourse.isNull) continue
            buildingRoomCourseList.add(parseSingleBuildingRoomCourseJson(currentCourse))
        }

        return buildingRoomCourseList
    }

    private fun parseSingleBuildingRoomCourseJson(courseJson: JsonValue): BuildingRoomCourse {
        val classNumber: Int = JsonFieldExtractor.extractIntValue(courseJson, "class_number")
        val subject: String = JsonFieldExtractor.extractStringValue(courseJson, "subject")
        val catalogNumber: String = JsonFieldExtractor.extractStringValue(courseJson, "catalog_number")
        val title: String = JsonFieldExtractor.extractStringValue(courseJson, "title")
        val section: String = JsonFieldExtractor.extractStringValue(courseJson, "section")
        val weekdays: String = JsonFieldExtractor.extractStringValue(courseJson, "weekdays")
        val startTime: String = JsonFieldExtractor.extractStringValue(courseJson, "start_time")
        val endTime: String = JsonFieldExtractor.extractStringValue(courseJson, "end_time")
        val startDate: String = JsonFieldExtractor.extractStringValue(courseJson, "start_date")
        val endDate: String = JsonFieldExtractor.extractStringValue(courseJson, "end_date")
        val enrollmentTotal: Int = JsonFieldExtractor.extractIntValue(courseJson, "enrollment_total")
        val instructors: MutableList<String> = mutableListOf()
        val building: String = JsonFieldExtractor.extractStringValue(courseJson, "building")
        val room: String = JsonFieldExtractor.extractStringValue(courseJson, "room")
        val term: Int = JsonFieldExtractor.extractIntValue(courseJson, "term")
        val lastUpdated: String = JsonFieldExtractor.extractStringValue(courseJson, "last_updated")

        if ((weekdays.isEmpty() && (startDate.isEmpty() || endDate.isEmpty())) ||
            (startTime.isEmpty() || endTime.isEmpty()) ||
            (subject.isEmpty() || catalogNumber.isEmpty() || title.isEmpty())) {
            throw IllegalArgumentException(ExceptionStrings.INVALID_JSON_FORMAT_STRING)
        }

        val instructorsData: JsonValue? = courseJson.asObject().get("instructors")
        val instructorsArray: JsonArray? = instructorsData?.asArray()
        if (instructorsArray != null) {
            for (j in 0 until instructorsArray.size()) {
                val instructor: JsonValue = instructorsArray.get(j)
                if (instructor.isNull) continue

                val instructorName: String = instructor.asString()
                val splitName: Array<String> = instructorName.split(",").toTypedArray()
                instructors.add("%s, %s".format(splitName[0], splitName[1]))
            }
        }

        return BuildingRoomCourse(classNumber, subject, catalogNumber, title, section,
                weekdays, startTime, endTime, startDate, endDate,
                enrollmentTotal, instructors, building, room, term, lastUpdated)
    }
}