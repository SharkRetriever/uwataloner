/*
 * Copyright 2018 - Yuanmeng Zhao (SharkRetriever)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package me.rayzz.uwataloner.base

import com.eclipsesource.json.JsonObject
import com.eclipsesource.json.JsonValue

object JsonFieldExtractor {
    // The Waterloo API values can be null at any given moment, so do not trust the input
    fun extractStringValue(value: JsonValue?, key: String): String {
        val jsonObject: JsonObject? = value?.asObject()
        return if (jsonObject == null || jsonObject.get(key).isNull) ""
            else jsonObject.getString(key, "").trim()
    }

    fun extractBooleanValue(value: JsonValue?, key: String): Boolean {
        val jsonObject: JsonObject? = value?.asObject()
        return if (jsonObject == null || jsonObject.get(key).isNull) false
            else jsonObject.getBoolean(key, false)
    }

    fun extractDoubleValue(value: JsonValue?, key: String): Double {
        val jsonObject: JsonObject? = value?.asObject()
        return if (jsonObject == null || jsonObject.get(key).isNull) 0.0
            else jsonObject.getDouble(key, 0.0)
    }

    fun extractIntValue(value: JsonValue?, key: String): Int {
        val jsonObject: JsonObject? = value?.asObject()
        return if (jsonObject == null || jsonObject.get(key).isNull) 0
            else jsonObject.getInt(key, 0)
    }
}