/*
 * Copyright 2018 - Yuanmeng Zhao (SharkRetriever)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package me.rayzz.uwataloner.apiobjects

import me.rayzz.uwataloner.base.ExceptionStrings

/**
 * Holds a course that takes place inside of a room
 * GET /buildings/{building}/{room}/courses.{format}
 */
class BuildingCourse(val sectionDateTime: SectionDateTime,
                     val subject: String, val catalogNumber: String,
                     val title: String, val section: String,
                     val enrollmentTotal: Int,
                     val building: String, val room: String, val instructors: List<String>) {
    override fun toString(): String {
        throw UnsupportedOperationException(ExceptionStrings.INVALID_PARAMETERS_STRING)
    }
}