/*
 * Copyright 2018 - Yuanmeng Zhao (SharkRetriever)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package me.rayzz.uwataloner.studyalonetoolkit

import org.joda.time.DateTime
import org.joda.time.Period

class GapSlot(val startDateTime: DateTime, val endDateTime: DateTime,
              val building: String, val room: String) {
    fun getDuration(): Period {
        return Period(startDateTime.millis, endDateTime.millis)
    }
}