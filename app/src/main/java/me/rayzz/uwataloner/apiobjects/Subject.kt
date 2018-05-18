/*
 * Copyright 2018 - Yuanmeng Zhao (SharkRetriever)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package me.rayzz.uwataloner.apiobjects

import me.rayzz.uwataloner.base.CharacterRepository

/**
 * Holds a single subject along with its description
 * GET /courses/{subject}.{format}
 */
class Subject(val subject: String, val description: String) {
    override fun toString(): String {
        return "%s %s %s".format(subject, CharacterRepository.EN_DASH, description)
    }
}