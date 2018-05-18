/*
 * Copyright 2018 - Yuanmeng Zhao (SharkRetriever)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package me.rayzz.uwataloner.apiobjects

import me.rayzz.uwataloner.base.CharacterRepository

/**
 * Helper to CourseSection
 * Holds a single instance of an offering time of a given section
 */
class Offering(val sectionDateTime: SectionDateTime,
               val building: String, val room: String, val instructors: List<String>,
               val isTba: Boolean, val isCancelled: Boolean, val isClosed: Boolean) {
    override fun toString(): String {
        // currently used in CourseSection for auditing courses
        val buildingString: String = "In building: %s %s".format(building, room)
        val dateString: String = "Offered: " + sectionDateTime.toString()
        val instructorsString: String = "Taught by: " + instructors.joinToString(CharacterRepository.INSTRUCTORS_SEPARATOR)
        return "%s\n%s\n%s".format(buildingString, dateString, instructorsString)
    }
}