/*
 * Copyright 2018 - Yuanmeng Zhao (SharkRetriever)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package me.rayzz.uwataloner

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun studyAloneButtonOnClick(view: View) {
        val intent = Intent(this, StudyAloneChooseBuildingActivity::class.java)
        startActivity(intent)
    }

    fun auditCourseButtonOnClick(view: View) {
        val intent = Intent(this, AuditCourseChooseSubjectActivity::class.java)
        startActivity(intent)
    }
}
