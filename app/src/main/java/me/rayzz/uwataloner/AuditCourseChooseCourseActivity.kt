/*
 * Copyright 2018 - Yuanmeng Zhao (SharkRetriever)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package me.rayzz.uwataloner

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.ArrayAdapter
import com.eclipsesource.json.JsonArray
import com.eclipsesource.json.JsonValue
import kotlinx.android.synthetic.main.activity_audit_course_choose_course.*
import kotlinx.android.synthetic.main.activity_audit_course_choose_subject.*
import me.rayzz.uwataloner.apiobjectfactories.CourseFactory
import me.rayzz.uwataloner.apiobjects.Course
import me.rayzz.uwataloner.base.ErrorRedirector
import me.rayzz.uwataloner.base.ExceptionStrings
import me.rayzz.uwataloner.base.JsonFieldExtractor
import me.rayzz.uwataloner.base.UWaterlooAPIRequestManager

class AuditCourseChooseCourseActivity : AppCompatActivity() {

    private val webRequestManager = UWaterlooAPIRequestManager()
    private val chosenSubjectString: String = "chosenSubject"
    private val chosenCourseString: String = "chosenCourse"
    private val exportChosenSubjectString: String = "chosenSubject"
    private val exportChosenCourseString: String = "chosenCourse"
    private var coursesComboBoxElements: Array<String> = arrayOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audit_course_choose_course)

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(chosenSubjectString)) {
                val chosenSubject: String = savedInstanceState.getString(chosenSubjectString)
                setCourseComboBox(chosenSubject)
            }
            if (savedInstanceState.containsKey(chosenCourseString)) {
                val chosenCourse: String = savedInstanceState.getString(chosenCourseString)
                chooseCourseText.setText(chosenCourse)
            }
        }
        else {
            val chosenSubject: String = intent.getStringExtra(chosenSubjectString)
            setCourseComboBox(chosenSubject)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        outState?.putString(chosenSubjectString, intent.getStringExtra(chosenSubjectString))
        outState?.putString(chosenCourseString, chooseCourseText.text.toString())
        super.onSaveInstanceState(outState, outPersistentState)
    }

    private fun setCourseComboBox(chosenSubject: String) {
        // GET /courses/{subject}.json
        try {
            val parsedCourses: List<Course> = CourseFactory.generate(chosenSubject)
            coursesComboBoxElements = parsedCourses.map { it.toString() }.toTypedArray()
            val adapter = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, coursesComboBoxElements)
            chooseCourseText.setAdapter<ArrayAdapter<String>>(adapter)
        }
        catch (e: Exception) {
            ErrorRedirector.redirectError(this, ExceptionStrings.INVALID_JSON_STATUS_STRING)
        }
    }

    fun chooseCourseNextButtonOnClick(view: View) {
        val intent = Intent(this, AuditCourseViewResultsActivity::class.java)
        val chosenSubjectCourseSplit: Array<String> = chooseCourseText.text.toString().trim().split(" ").toTypedArray()
        val chosenSubjectCourseText: String = chooseCourseText.text.toString().trim()

        if (!coursesComboBoxElements.any { it == chosenSubjectCourseText }) {
            showInvalidInputDialog()
            return
        }

        val chosenSubjectAcronym: String = chosenSubjectCourseSplit[0].trim()
        val chosenCourseNumber: String = chosenSubjectCourseSplit[1].trim()

        if (!webRequestManager.isValidWebPageJson("/courses/%s/%s/schedule".format(chosenSubjectAcronym, chosenCourseNumber))) {
            showInvalidInputDialog()
            return
        }

        try {
            intent.putExtra(exportChosenSubjectString, chosenSubjectAcronym)
            intent.putExtra(exportChosenCourseString, chosenCourseNumber)
            startActivity(intent)
        }
        catch (e: Exception) {
            showInvalidInputDialog()
        }
    }

    private fun showInvalidInputDialog() {
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle("Error")
        alertDialog.setMessage("The course you want to audit either does not exist, or is not offered this term")
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                DialogInterface.OnClickListener { dialog, _ -> dialog.dismiss() })
        alertDialog.show()
    }
}
