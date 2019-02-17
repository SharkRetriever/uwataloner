/*
 * Copyright 2018 - Yuanmeng Zhao (SharkRetriever)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package me.rayzz.uwataloner.utilities

import com.eclipsesource.json.JsonObject
import com.eclipsesource.json.JsonValue

object JsonFieldExtractor {
    fun extractStringValue(value: JsonValue, key: String): String {
        val jsonObject: JsonObject = value.asObject()
        return if (jsonObject.get(key).isNull) ""
            else jsonObject.getString(key, "").trim()
    }

    fun extractBooleanValue(value: JsonValue, key: String): Boolean {
        val jsonObject: JsonObject = value.asObject()
        return if (jsonObject.get(key).isNull) false
            else jsonObject.getBoolean(key, false)
    }

    fun extractIntValue(value: JsonValue, key: String): Int {
        val jsonObject: JsonObject = value.asObject()
        return if (jsonObject.get(key).isNull) 0
            else jsonObject.getInt(key, 0)
    }
}