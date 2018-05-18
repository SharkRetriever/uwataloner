/*
 * Copyright 2018 - Yuanmeng Zhao (SharkRetriever)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package me.rayzz.uwataloner

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TimePicker
import kotlinx.android.synthetic.main.activity_study_alone_choose_building.*
import me.rayzz.uwataloner.base.UWaterlooAPIRequestManager
import me.rayzz.uwataloner.studyalonetoolkit.ValidRoomsListRetriever
import org.joda.time.DateTime
import java.util.*

class StudyAloneChooseBuildingActivity : AppCompatActivity() {

    private val webRequestManager = UWaterlooAPIRequestManager()
    private val chosenBuildingString: String = "chosenCourse"
    private val chosenRoomString: String = "chosenRoom"
    private val chosenTimeString: String = "chosenTime"

    private val timePickerDialogOnTimeSetEventListener = object : TimePickerDialog.OnTimeSetListener {
        override fun onTimeSet(timePicker: TimePicker?, hour: Int, minute: Int) {
            chooseTimeText.text = "%2d:%02d".format(hour, minute)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_study_alone_choose_building)

        val validRoomsListRetriever = ValidRoomsListRetriever(application.assets)
        validRoomsListRetriever.initializeRoomsList() // TODO: make this async since beginning of load
        val validRoomsList: HashMap<String, List<String>> = validRoomsListRetriever.getRoomsList()
        setTimeToCurrentTime()
        setBuildingComboBox(validRoomsList.keys)

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(chosenBuildingString)) {
                chooseBuildingText.setText(savedInstanceState.getString(chosenBuildingString))
            }
            if (savedInstanceState.containsKey(chosenRoomString)) {
                chooseRoomText.setText(savedInstanceState.getString(chosenRoomString))
            }
            if (savedInstanceState.containsKey(chosenTimeString)) {
                chooseTimeText.setText(savedInstanceState.getString(chosenTimeString))
            }
        }

        val context: Context = this
        chooseBuildingText.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                chooseRoomText.clearListSelection()
                chooseRoomText.setText("")

                val roomsList: List<String>? = if (p0 != null && validRoomsList.containsKey(p0.toString())) validRoomsList[p0.toString()] else null
                if (roomsList == null) {
                    val adapter: ArrayAdapter<String> = ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, arrayOf<String>())
                    chooseRoomText.setAdapter<ArrayAdapter<String>>(adapter)
                }
                else {
                    val adapter = ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, roomsList.toTypedArray())
                    chooseRoomText.setAdapter<ArrayAdapter<String>>(adapter)
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        outState?.putString(chosenBuildingString, chooseBuildingText.text.toString())
        outState?.putString(chosenRoomString, chooseRoomText.text.toString())
        outState?.putString(chosenTimeString, chooseTimeText.text.toString())
        super.onSaveInstanceState(outState, outPersistentState)
    }

    private fun setTimeToCurrentTime() {
        val hourAndMinute: Pair<Int, Int> = getCurrentHourAndMinute()
        chooseTimeText.text = "%2d:%02d".format(hourAndMinute.first, hourAndMinute.second)
    }

    private fun getCurrentHourAndMinute(): Pair<Int, Int> {
        val currentTime: DateTime = DateTime.now()
        val currentHour: Int = currentTime.hourOfDay
        val currentMinute: Int = currentTime.minuteOfHour
        return Pair<Int, Int>(currentHour, currentMinute)
    }

    private fun setBuildingComboBox(buildings: MutableSet<String>) {
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, buildings.toTypedArray())
        chooseBuildingText.setAdapter<ArrayAdapter<String>>(adapter)
    }

    fun chooseTimeButtonOnClick(view: View) {
        val hourAndMinute: Pair<Int, Int> = getCurrentHourAndMinute()
        val dialog = TimePickerDialog(this, timePickerDialogOnTimeSetEventListener,
                hourAndMinute.first, hourAndMinute.second, true)
        dialog.show()
    }

    fun chooseBuildingNextButtonOnClick(view: View) {
        val chosenBuilding: String = chooseBuildingText.text.toString().trimStart().split(" ")[0]
        val chosenRoom: String = chooseRoomText.text.toString()
        val chosenTime: String = chooseTimeText.text.toString()

        // TODO: this should just be checked against buildingList alone -- exclude the need for secret rooms
        if (!webRequestManager.isValidWebPageJson("/buildings/%s/%s/courses".format(chosenBuilding, chosenRoom))) {
            showInvalidInputDialog()
            return
        }

        val intent = Intent(this, StudyAloneViewResultsActivity::class.java)
        intent.putExtra("chosenBuilding", chosenBuilding)
        intent.putExtra("chosenRoom", chosenRoom)
        intent.putExtra("chosenTime", chosenTime)

        startActivity(intent)
    }

    private fun showInvalidInputDialog() {
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle("Error")
        alertDialog.setMessage("Invalid input: please select a valid building and/or room")
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                DialogInterface.OnClickListener { dialog, _ -> dialog.dismiss() })
        alertDialog.show()
    }
}
