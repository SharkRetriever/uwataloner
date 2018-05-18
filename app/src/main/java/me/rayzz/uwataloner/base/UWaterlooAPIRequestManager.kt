/*
 * Copyright 2018 - Yuanmeng Zhao (SharkRetriever)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package me.rayzz.uwataloner.base

import com.eclipsesource.json.JsonValue
import java.io.InvalidObjectException

class UWaterlooAPIRequestManager {
    // urlString must start with '/'
    fun getWebPageJson(apiPath: String): JsonValue {
        val asyncTask = RetrieveWebPageAsyncTask()
        val resultingObject: JsonValue = asyncTask.execute(apiPath).get()

        if (failsSanity(resultingObject)) {
            throw InvalidObjectException(ExceptionStrings.INVALID_JSON_STATUS_STRING)
        }

        return resultingObject
    }

    fun isValidWebPageJson(apiPath: String): Boolean {
        val asyncTask = RetrieveWebPageAsyncTask()
        val resultingObject: JsonValue = asyncTask.execute(apiPath).get()

        if (failsSanity(resultingObject)) {
            return false
        }

        return true
    }

    private fun failsSanity(resultingObject: JsonValue): Boolean {
        val resultingObjectMeta: JsonValue? = resultingObject.asObject().get("meta")
        val resultingObjectStatusCode: Int

        if (resultingObjectMeta == null) {
            return true
        }

        resultingObjectStatusCode = resultingObjectMeta.asObject().getInt("status", 0)
        if (resultingObjectStatusCode != 200) {
            return true
        }

        return false
    }
}