/*
 * Copyright 2018 - Yuanmeng Zhao (SharkRetriever)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package me.rayzz.uwataloner.apimodels

import me.rayzz.uwataloner.utilities.CharacterRepository
import me.rayzz.uwataloner.viewmodels.SectionDateTime

/**
 * Helper to CourseSection
 * Holds a single instance of an offering time of a given section
 */
@Deprecated("Unused -- to be refactored into newer service in later version")
class Offering(val sectionDateTime: SectionDateTime,
               val building: String, val room: String, val instructors: List<String>,
               val isTba: Boolean, val isCancelled: Boolean, val isClosed: Boolean) {
    override fun toString(): String {
        // currently used in CourseSection for auditing courses
        val buildingString: String = if (building.isNotEmpty()) "In building: %s %s".format(building, room) else "Location TBA or online"
        val dateString: String = "Offered: " + if (sectionDateTime.isValid()) sectionDateTime.toString() else "N/A"
        val instructorsString: String = "Taught by: " + if (instructors.isNotEmpty()) instructors.joinToString(CharacterRepository.INSTRUCTORS_SEPARATOR) else "TBA"
        return "%s\n%s\n%s".format(buildingString, dateString, instructorsString)
    }
}