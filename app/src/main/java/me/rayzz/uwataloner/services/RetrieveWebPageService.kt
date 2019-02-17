/*
 * Copyright 2018 - Yuanmeng Zhao (SharkRetriever)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package me.rayzz.uwataloner.services

import com.eclipsesource.json.Json
import com.eclipsesource.json.JsonValue
import com.eclipsesource.json.ParseException
import me.rayzz.uwataloner.utilities.Cache
import me.rayzz.uwataloner.utilities.ExceptionStrings
import me.rayzz.uwataloner.utilities.KeyLoader
import java.io.InvalidObjectException
import java.net.URL

object RetrieveWebPageService {
    private val loader: KeyLoader = KeyLoader()

    fun getJson(apiPath: String): JsonValue {
        val keylessUrl = "https://api.uwaterloo.ca/v2/$apiPath.json"
        val url = URL(keylessUrl + "?key=" + loader.Key)
        val readText: String
        val resultingObject: JsonValue

        if (Cache.hasUnexpiredValue(keylessUrl)) {
            resultingObject = Cache.getByKey(keylessUrl) as JsonValue
        }
        else {
            readText = url.openStream().bufferedReader().use { it.readText() }

            try {
                resultingObject = Json.parse(readText)
                Cache.put(keylessUrl, resultingObject)
            } catch (e: ParseException) {
                throw InvalidObjectException(ExceptionStrings.INVALID_JSON_PARSE_STRING)
            }
        }

        return resultingObject
    }
}