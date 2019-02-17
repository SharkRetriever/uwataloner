/*
 * Copyright 2018 - Yuanmeng Zhao (SharkRetriever)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package me.rayzz.uwataloner.utilities

import org.joda.time.DateTime

object Cache {
    private val internalCache: MutableMap<String, Pair<Any, DateTime>> = hashMapOf()

    fun put(key: String, value: Any) {
        internalCache[key] = Pair(value, DateTime.now())
    }

    fun hasUnexpiredValue(key: String): Boolean {
        // expire the cache after 1 day
        return internalCache.containsKey(key) && !internalCache[key]!!.second.plusDays(1).isBeforeNow
    }

    fun getByKey(key: String): Any {
        // expire the cache after 1 day
        return internalCache[key]!!.first
    }
}