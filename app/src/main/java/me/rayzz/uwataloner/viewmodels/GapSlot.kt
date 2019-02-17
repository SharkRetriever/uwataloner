/*
 * Copyright 2018 - Yuanmeng Zhao (SharkRetriever)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package me.rayzz.uwataloner.viewmodels

import android.os.Parcel
import android.os.Parcelable
import org.joda.time.DateTime
import org.joda.time.Period

class GapSlot(val startDateTime: DateTime, val endDateTime: DateTime,
              val building: String, val room: String) : Parcelable {
    constructor(parcel: Parcel) : this(
        DateTime.parse(parcel.readString()),
        DateTime.parse(parcel.readString()),
        parcel.readString(),
        parcel.readString())

    private fun getDuration(): Period {
        return Period(startDateTime.millis, endDateTime.millis)
    }

    fun getDurationMillis(): Long {
        return endDateTime.millis - startDateTime.millis
    }

    override fun toString(): String {
        val buildingAcronym: String = building.split(" ")[0]
        val endDateHour: Int = endDateTime.hourOfDay
        val endDateMinute: Int = endDateTime.minuteOfHour
        val startDateHour: Int = startDateTime.hourOfDay
        val startDateMinute: Int = startDateTime.minuteOfHour
        val durationHour: Int = getDuration().hours
        val durationMinutes: Int = getDuration().minutes
        return when {
            (endDateHour == 23 && endDateMinute == 59) ->
                "%s %s\nAfter %d:%02d".format(
                        buildingAcronym, room, startDateHour, startDateMinute)
            else ->
                "%s %s\n%d:%02d - %d:%02d (for %d hrs %2d min)".format(
                        buildingAcronym, room, startDateHour, startDateMinute, endDateHour, endDateMinute, durationHour, durationMinutes)
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(startDateTime.toString())
        parcel.writeString(endDateTime.toString())
        parcel.writeString(building)
        parcel.writeString(room)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GapSlot> {
        override fun createFromParcel(parcel: Parcel): GapSlot {
            return GapSlot(parcel)
        }

        override fun newArray(size: Int): Array<GapSlot?> {
            return arrayOfNulls(size)
        }
    }
}