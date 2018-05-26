/*
 * Copyright 2018 - Yuanmeng Zhao (SharkRetriever)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package me.rayzz.uwataloner.apiobjects

import me.rayzz.uwataloner.base.CharacterRepository

/**
 * Holds the course schedule per section of a given subject and catalog number
 * GET /courses/{subject}/{catalog_number}/schedule.{format}
 */
class CourseSection(val subject: String, val catalogNumber: String,
                    val title: String, val section: String,
                    val enrollmentCapacity: Int, val enrollmentTotal: Int,
                    val offeringTimes: List<Offering>) {
    override fun toString(): String {
        val subjectString: String = "%s %s %s %s".format(subject, catalogNumber, CharacterRepository.EN_DASH, title)
        val sectionString: String = "Section %s (%s/%s enrolled)".format(section, enrollmentTotal, enrollmentCapacity)
        val offeringTimes: String = offeringTimes.map { it.toString() }.joinToString("\n")

        return when {
            (enrollmentTotal > enrollmentCapacity) -> {
                val warningString: String =
                        if (enrollmentTotal - enrollmentCapacity > 1) "WARNING: over capacity by %s people".format(enrollmentTotal - enrollmentCapacity)
                        else "WARNING: over capacity by 1 person"
                "%s\n%s\n%s\n%s".format(warningString, subjectString, sectionString, offeringTimes)
            }
            (enrollmentTotal == enrollmentCapacity) -> {
                val warningString = "WARNING: at capacity"
                "%s\n%s\n%s\n%s".format(warningString, subjectString, sectionString, offeringTimes)
            }
            else -> {
                "%s\n%s\n%s".format(subjectString, sectionString, offeringTimes)
            }
        }
    }
}