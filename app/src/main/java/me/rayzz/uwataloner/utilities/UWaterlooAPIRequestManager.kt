/*
 * Copyright 2018 - Yuanmeng Zhao (SharkRetriever)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package me.rayzz.uwataloner.utilities

import com.eclipsesource.json.JsonValue
import me.rayzz.uwataloner.services.RetrieveWebPageService
import java.io.InvalidObjectException

class UWaterlooAPIRequestManager {
    fun getWebPageJson(apiPath: String): JsonValue {
        val resultingObject: JsonValue? = RetrieveWebPageService.getJson(apiPath)

        if (resultingObject == null || resultingObject.asObject().get("meta") == null || resultingObject.asObject().get("data") == null) {
            throw InvalidObjectException(ExceptionStrings.INVALID_JSON_FORMAT_STRING)
        }

        return resultingObject
    }
}