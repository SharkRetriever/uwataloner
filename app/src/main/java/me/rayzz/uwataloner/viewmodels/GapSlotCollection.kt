/*
 * Copyright 2018 - Yuanmeng Zhao (SharkRetriever)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package me.rayzz.uwataloner.viewmodels

import android.os.Parcel
import android.os.Parcelable

class GapSlotCollection(val gapSlots: Array<GapSlot>) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.createTypedArray(GapSlot))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedArray(gapSlots, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GapSlotCollection> {
        override fun createFromParcel(parcel: Parcel): GapSlotCollection {
            return GapSlotCollection(parcel)
        }

        override fun newArray(size: Int): Array<GapSlotCollection?> {
            return arrayOfNulls(size)
        }
    }
}