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
import android.os.PersistableBundle
import android.view.View
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_study_alone_view_results.*
import me.rayzz.uwataloner.viewmodels.GapSlotCollection

class StudyAloneViewResultsActivity : AppCompatActivity() {
    private val foundGapsString: String = "foundGaps"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_study_alone_view_results)

        val foundGaps: GapSlotCollection

        if (savedInstanceState == null) {
            foundGaps = intent.getParcelableExtra(foundGapsString)
        }
        else {
            foundGaps = savedInstanceState.getParcelable(foundGapsString)
        }

        showResults(foundGaps)
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        outState?.putParcelable(foundGapsString, intent.getParcelableExtra(foundGapsString))
        super.onSaveInstanceState(outState, outPersistentState)
    }

    private fun showResults(foundGaps: GapSlotCollection) {
        val gapsInCourses: Array<String> = foundGaps.gapSlots.map { it.toString() }.toTypedArray()
        val gapsInCoursesDisplay: Array<String> = if (gapsInCourses.isEmpty()) arrayOf("No results found.") else gapsInCourses
        studyAloneCourseResultsList.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, gapsInCoursesDisplay)
    }

    fun studyAloneAgainButtonOnClick(view: View) {
        val intent = Intent(this, StudyAloneChooseBuildingActivity::class.java)
        startActivity(intent)
    }

    fun studyAloneReturnMenuButtonOnClick(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
