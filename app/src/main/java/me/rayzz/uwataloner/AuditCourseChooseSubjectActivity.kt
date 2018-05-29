/*
 * Copyright 2018 - Yuanmeng Zhao (SharkRetriever)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package me.rayzz.uwataloner

import android.app.AlertDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import me.rayzz.uwataloner.base.UWaterlooAPIRequestManager
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_audit_course_choose_subject.*
import android.content.DialogInterface
import android.os.PersistableBundle
import android.widget.AdapterView
import com.eclipsesource.json.JsonArray
import com.eclipsesource.json.JsonValue
import me.rayzz.uwataloner.base.ErrorRedirector
import me.rayzz.uwataloner.base.ExceptionStrings
import me.rayzz.uwataloner.base.JsonFieldExtractor
import me.rayzz.uwataloner.generatedapiobjects.SubjectsList
import java.io.InvalidObjectException

class AuditCourseChooseSubjectActivity : AppCompatActivity() {

    private var parsedSubjects: Array<String> = arrayOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audit_course_choose_subject)
        setSubjectComboBox()

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("chosenSubject")) {
                chooseSubjectText.setText(savedInstanceState.getString("chosenSubject"))
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        outState?.putString("chosenSubject", chooseSubjectText.text.toString())
        super.onSaveInstanceState(outState, outPersistentState)
    }

    private fun setSubjectComboBox() {
        // GET /codes/subjects.json
        try {
            parsedSubjects = SubjectsList.getSubjectsList().map{ it.toString() }.toTypedArray()
            val adapter = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, parsedSubjects)
            chooseSubjectText.setAdapter<ArrayAdapter<String>>(adapter)
        }
        catch (e: InvalidObjectException) {
            e.printStackTrace(System.err)
            ErrorRedirector.redirectError(this, ExceptionStrings.INVALID_JSON_STATUS_STRING, e)
        }

        /*
        chooseSubjectText.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            // https://stackoverflow.com/questions/29387575/spinner-how-to-set-border-around-drop-down-list
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                chooseSubjectText.background.
            }
        }
        */
    }

    fun chooseSubjectNextButtonOnClick(view: View) {
        val intent = Intent(this, AuditCourseChooseCourseActivity::class.java)
        val chosenSubjectAcronym: String = chooseSubjectText.text.split(" ")[0]
        val chosenSubjectText: String = chooseSubjectText.text.toString().trim()

        if (!parsedSubjects.any { it == chosenSubjectText }) {
            showInvalidInputDialog()
            return
        }

        intent.putExtra("chosenSubject", chosenSubjectAcronym)
        startActivity(intent)
    }

    private fun showInvalidInputDialog() {
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle("Error")
        alertDialog.setMessage("Invalid input: please select a valid subject")
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                DialogInterface.OnClickListener { dialog, _ -> dialog.dismiss() })
        alertDialog.show()
    }
}
