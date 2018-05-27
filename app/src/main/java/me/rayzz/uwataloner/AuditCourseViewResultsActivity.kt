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
import com.eclipsesource.json.Json
import com.eclipsesource.json.JsonArray
import com.eclipsesource.json.JsonValue
import kotlinx.android.synthetic.main.activity_audit_course_view_results.*
import me.rayzz.uwataloner.apiobjectfactories.CourseSectionFactory
import me.rayzz.uwataloner.apiobjects.CourseSection
import me.rayzz.uwataloner.base.ErrorRedirector
import me.rayzz.uwataloner.base.ExceptionStrings
import me.rayzz.uwataloner.base.JsonFieldExtractor
import me.rayzz.uwataloner.base.UWaterlooAPIRequestManager

class AuditCourseViewResultsActivity : AppCompatActivity() {

    private val chosenSubjectString: String = "chosenSubject"
    private val chosenCourseString: String = "chosenCourse"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audit_course_view_results)

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(chosenSubjectString) &&
                savedInstanceState.containsKey(chosenCourseString)) {
                val chosenSubject: String = savedInstanceState.getString(chosenSubjectString)
                val chosenCourse: String = savedInstanceState.getString(chosenCourseString)

                showResultsFromFactory(chosenSubject, chosenCourse)
            }
        }
        else {
            val chosenSubject: String = intent.getStringExtra(chosenSubjectString)
            val chosenCourse: String = intent.getStringExtra(chosenCourseString)
            showResultsFromFactory(chosenSubject, chosenCourse)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        outState?.putString(chosenSubjectString, intent.getStringExtra(chosenSubjectString))
        outState?.putString(chosenCourseString, intent.getStringExtra(chosenCourseString))
        super.onSaveInstanceState(outState, outPersistentState)
    }

    private fun showResultsFromFactory(chosenSubject: String, chosenCourse: String) {
        val parsedCourseSections: List<CourseSection> = CourseSectionFactory.generate(chosenSubject, chosenCourse)
        val parsedStringizedSections: Array<String> = parsedCourseSections.map { it.toString() }.toTypedArray()
        auditCourseResultsList.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, parsedStringizedSections)
    }

    fun auditAgainButtonOnClick(view: View) {
        val intent = Intent(this, AuditCourseChooseSubjectActivity::class.java)
        startActivity(intent)
    }

    fun auditReturnMenuButtonOnClick(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
