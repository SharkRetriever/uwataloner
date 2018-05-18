/*
 * Copyright 2018 - Yuanmeng Zhao (SharkRetriever)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package me.rayzz.uwataloner.apiobjects

import me.rayzz.uwataloner.base.CharacterRepository

/**
 * Holds a single building code along with its name
 * GET /buildings/{building_code}.{format}
 */
class Building(val code: String, val name: String) {
    override fun toString(): String {
        return "%s %s %s".format(code, CharacterRepository.EN_DASH, name)
    }
}