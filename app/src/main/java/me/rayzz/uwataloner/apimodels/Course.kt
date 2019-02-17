/*
 * Copyright 2018 - Yuanmeng Zhao (SharkRetriever)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package me.rayzz.uwataloner.apimodels

import me.rayzz.uwataloner.utilities.CharacterRepository

/**
 * Helper to AuditCourseChooseCourseActivity
 * Holds a course's subject, catalog number, and title
 */
@Deprecated("Unused -- to be refactored into newer service in later version")
class Course(val subject: String, val catalogNumber: String, val title: String) {
    override fun toString(): String {
        return "%s %s %s %s".format(subject, catalogNumber, CharacterRepository.EN_DASH, title)
    }
}