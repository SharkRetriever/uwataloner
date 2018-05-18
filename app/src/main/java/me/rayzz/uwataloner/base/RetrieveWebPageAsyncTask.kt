/*
 * Copyright 2018 - Yuanmeng Zhao (SharkRetriever)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package me.rayzz.uwataloner.base

import android.os.AsyncTask
import com.eclipsesource.json.Json
import com.eclipsesource.json.JsonValue
import com.eclipsesource.json.ParseException
import java.io.IOException
import java.io.InvalidObjectException
import java.net.URL
import java.security.InvalidParameterException

class RetrieveWebPageAsyncTask: AsyncTask<String, Void, JsonValue> () {
    private val loader: KeyLoader = KeyLoader()

    override fun doInBackground(vararg p0: String?): JsonValue? {
        if (p0.size != 1) {
            throw InvalidParameterException(ExceptionStrings.INVALID_PARAMETERS_STRING)
        }

        val apiPath = p0[0]
        val url = URL("https://api.uwaterloo.ca/v2" + apiPath + ".json?key=" + loader.Key)
        val readText: String
        val resultingObject: JsonValue

        try {
            readText = url.openStream().bufferedReader().use { it.readText() }
        }
        catch (e: IOException) {
            throw IOException(ExceptionStrings.INVALID_PARAMETERS_STRING)
        }
        catch (e: Exception) {
            throw Exception(ExceptionStrings.UNKNOWN_EXCEPTION_STRING)
        }

        try {
            resultingObject = Json.parse(readText)
        }
        catch (e: ParseException) {
            throw InvalidObjectException(ExceptionStrings.INVALID_JSON_PARSE_STRING)
        }

        return resultingObject
    }
}