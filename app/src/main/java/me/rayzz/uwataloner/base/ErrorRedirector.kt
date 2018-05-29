/*
 * Copyright 2018 - Yuanmeng Zhao (SharkRetriever)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package me.rayzz.uwataloner.base

import android.content.Context
import android.content.Intent
import me.rayzz.uwataloner.ErrorActivity

object ErrorRedirector {
    fun redirectError(context: Context, errorString: String, e: Exception) {
        val intent = Intent(context, ErrorActivity::class.java)
        intent.putExtra("exception", errorString + "\n" + e.toString())
        context.startActivity(intent)
    }
}