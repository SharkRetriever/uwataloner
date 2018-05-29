/*
 * Copyright 2018 - Yuanmeng Zhao (SharkRetriever)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package me.rayzz.uwataloner.apiobjectfactories

import com.eclipsesource.json.JsonArray
import com.eclipsesource.json.JsonValue
import me.rayzz.uwataloner.apiobjects.BuildingCourse
import me.rayzz.uwataloner.apiobjects.SectionDateTime
import me.rayzz.uwataloner.base.ExceptionStrings
import me.rayzz.uwataloner.base.JsonFieldExtractor
import me.rayzz.uwataloner.base.UWaterlooAPIRequestManager
import me.rayzz.uwataloner.generatedapiobjects.BuildingRoomsListMap
import java.io.InvalidObjectException
import java.security.InvalidParameterException

/**
 * Given a building and a room number, generates a list of courses that takes place
 * in that room
 * GET /buildings/{building}/{room}/courses.{format}
 */
object BuildingCourseFactory {
    private val webRequestManager = UWaterlooAPIRequestManager()

    fun generate(building: String, room: String): List<BuildingCourse> {
        try {
            val buildingCode: String = building.split(" ")[0]
            if (room.isNotEmpty()) {
                val coursesInRoomJson: JsonValue = webRequestManager.getWebPageJson("/buildings/" + buildingCode + "/" +
                        room + "/courses")

                return parseBuildingCourseJson(coursesInRoomJson)
            }
            else {
                val coursesInBuilding: List<BuildingCourse>? = BuildingRoomsListMap.getBuildingRoomsListMap()[building]?.flatMap {
                    val coursesInRoomJson: JsonValue = webRequestManager.getWebPageJson("/buildings/" + buildingCode + "/" +
                        it + "/courses")
                    parseBuildingCourseJson(coursesInRoomJson)
                }

                return coursesInBuilding ?: throw InvalidObjectException(ExceptionStrings.UNKNOWN_EXCEPTION_STRING)
            }
        }
        catch (e: Exception) {
            throw InvalidParameterException(ExceptionStrings.INVALID_JSON_STATUS_STRING)
        }
    }

    private fun parseBuildingCourseJson(buildingCourseJson: JsonValue): List<BuildingCourse> {
        val coursesInRoomJsonCoursesData: JsonValue? = buildingCourseJson.asObject().get("data")
        val coursesInRoomJSONCoursesArray: JsonArray? = coursesInRoomJsonCoursesData?.asArray()

        val buildingCourseList: MutableList<BuildingCourse> = mutableListOf()

        if (coursesInRoomJSONCoursesArray == null) {
            throw InvalidParameterException(ExceptionStrings.INVALID_JSON_FORMAT_STRING)
        }
        else {
            for (i in 0 until coursesInRoomJSONCoursesArray.size()) {
                val currentCourse: JsonValue = coursesInRoomJSONCoursesArray.get(i)

                val weekdays: String = JsonFieldExtractor.extractStringValue(currentCourse, "weekdays")
                val startTime: String = JsonFieldExtractor.extractStringValue(currentCourse, "start_time")
                val endTime: String = JsonFieldExtractor.extractStringValue(currentCourse, "end_time")
                val startDate: String = JsonFieldExtractor.extractStringValue(currentCourse, "start_date")
                val endDate: String = JsonFieldExtractor.extractStringValue(currentCourse, "end_date")
                val subject: String = JsonFieldExtractor.extractStringValue(currentCourse, "subject")
                val catalogNumber: String = JsonFieldExtractor.extractStringValue(currentCourse, "catalog_number")
                val title: String = JsonFieldExtractor.extractStringValue(currentCourse, "title")
                val section: String = JsonFieldExtractor.extractStringValue(currentCourse, "section")
                val enrollmentTotal: Int = JsonFieldExtractor.extractIntValue(currentCourse, "enrollment_total")
                val building: String = JsonFieldExtractor.extractStringValue(currentCourse, "building")
                val room: String = JsonFieldExtractor.extractStringValue(currentCourse, "room")
                val instructors: MutableList<String> = mutableListOf()

                if ((weekdays.isEmpty() && (startDate.isEmpty() || endDate.isEmpty())) ||
                    (startTime.isEmpty() || endTime.isEmpty() || subject.isEmpty() || catalogNumber.isEmpty() || title.isEmpty())) {
                    throw InvalidParameterException(ExceptionStrings.INVALID_JSON_FORMAT_STRING)
                }
                else {
                    val instructorsData: JsonValue? = currentCourse.asObject().get("instructors")
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
                    buildingCourseList.add(BuildingCourse(sectionDateTime, subject, catalogNumber,
                            title, section, enrollmentTotal, building, room, instructors))
                }
            }
        }

        return buildingCourseList
    }
}