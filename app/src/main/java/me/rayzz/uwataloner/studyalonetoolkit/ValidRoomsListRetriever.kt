/*
 * Copyright 2018 - Yuanmeng Zhao (SharkRetriever)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package me.rayzz.uwataloner.studyalonetoolkit

import android.content.res.AssetManager

class ValidRoomsListRetriever(private val assetManager: AssetManager) {
    // warning: this list should be updated once per term
    private val roomsList: HashMap<String, List<String>> = HashMap(60);
    private var initialized: Boolean = false
    // private val webPageRetriever = UWaterlooAPIRequestManager()

    fun getRoomsList(): HashMap<String, List<String>> {
        return roomsList
    }

    fun initializeRoomsList() {
        if (!initialized) {
            assetManager.open("rooms.txt").bufferedReader().use {
                it.readLines().forEach {
                    val buildingCode: String = it.split(":")[0]
                    val buildingName: String = getRoomName(buildingCode)
                    val buildingFullName: String = if (buildingName == "") "" else buildingCode + " – " + buildingName
                    val roomNumbers: List<String> = it.split(":")[1].trim().split(", ")

                    if (buildingFullName != "") {
                        roomsList.put(buildingFullName, roomNumbers)
                    }
                }
                initialized = true
            }
        }
    }

    private fun getRoomName(buildingCode: String): String {
        /*
        val buildingNameJSON: JSONObject = webPageRetriever.getWebPageJson("/buildings/" + buildingCode)
        val status: Int = buildingNameJSON.getJSONObject("meta").getInt("status")
        if (status == 200) {
            return buildingNameJSON.getJSONObject("data").getString("building_name")
        } else {
            return ""
        }
        */
        return ""
    }
}