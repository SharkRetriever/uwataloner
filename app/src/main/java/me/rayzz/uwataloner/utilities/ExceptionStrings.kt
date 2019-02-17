/*
 * Copyright 2018 - Yuanmeng Zhao (SharkRetriever)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package me.rayzz.uwataloner.utilities

object ExceptionStrings {
    // For non-200 status codes
    val INVALID_JSON_STATUS_STRING = "Invalid JSON status"

    // For invalid parameters
    val INVALID_PARAMETERS_STRING = "Invalid parameter(s) passed in to method: "

    // For invalid format returned from UWaterloo API
    val INVALID_JSON_PARSE_STRING = "Invalid JSON; the UWaterloo API returned non-parsable JSON"

    // For invalid formatting while attempting to reach a field
    val INVALID_JSON_FORMAT_STRING = "Invalid JSON; the UWaterloo API returned valid JSON data in an unexpected layout"

    // For very weird cases
    val UNKNOWN_EXCEPTION_STRING = "Something went wrong (unexpected error occurred)"
}