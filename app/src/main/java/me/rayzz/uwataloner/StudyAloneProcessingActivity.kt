/*
 * Copyright 2018 - Yuanmeng Zhao (SharkRetriever)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package me.rayzz.uwataloner

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_study_alone_processing.*
import me.rayzz.uwataloner.apimodels.BuildingRoomCourse
import me.rayzz.uwataloner.scrapedapiobjects.BuildingRoomsListMap
import me.rayzz.uwataloner.services.api.BuildingRoomCoursesService
import me.rayzz.uwataloner.utilities.ExceptionStrings
import me.rayzz.uwataloner.utilities.GetGapsListTaskParameters
import me.rayzz.uwataloner.viewmodels.GapSlotCollection
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import java.lang.ref.SoftReference
import android.view.KeyEvent

class StudyAloneProcessingActivity : AppCompatActivity() {
    private val chosenBuildingString: String = "chosenBuilding"
    private val chosenRoomString: String = "chosenRoom"
    private val chosenTimeString: String = "chosenTime"
    private var getGapsListTask: AsyncTask<GetGapsListTaskParameters, String, GapSlotCollection>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_study_alone_processing)
        studyAloneProcessingStatusLabel.text = "Setting up fetching service"

        val chosenBuilding: String
        val chosenRoom: String
        val chosenTime: String
        val chosenTimeHour: Int
        val chosenTimeMinute: Int

        if (savedInstanceState == null) {
            chosenBuilding = intent.getStringExtra(chosenBuildingString)
            chosenRoom = intent.getStringExtra(chosenRoomString)
            chosenTime = intent.getStringExtra(chosenTimeString)
        }
        else {
            chosenBuilding = savedInstanceState.getString(chosenBuildingString)
            chosenRoom = savedInstanceState.getString(chosenRoomString)
            chosenTime = savedInstanceState.getString(chosenTimeString)
        }

        chosenTimeHour = chosenTime.split(":")[0].trimStart().toInt()
        chosenTimeMinute = chosenTime.split(":")[1].toInt()

        getGapsListTask = GetGapsListTask(SoftReference(this)).execute(GetGapsListTaskParameters(chosenBuilding, chosenRoom, chosenTimeHour, chosenTimeMinute))
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        outState?.putString(chosenBuildingString, intent.getStringExtra(chosenBuildingString))
        outState?.putString(chosenRoomString, intent.getStringExtra(chosenRoomString))
        outState?.putString(chosenTimeString, intent.getStringExtra(chosenTimeString))
        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            getGapsListTask?.cancel(true)
            this.finish()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    class GetGapsListTask(private val activityReference: SoftReference<StudyAloneProcessingActivity>): AsyncTask<GetGapsListTaskParameters, String, GapSlotCollection>() {
        private val foundGapsString: String = "foundGaps"

        override fun doInBackground(vararg params: GetGapsListTaskParameters?): GapSlotCollection {
            val chosenBuilding: String = params[0]!!.chosenBuilding
            val chosenRoom: String = params[0]!!.chosenRoom
            val chosenTimeHour: Int = params[0]!!.chosenTimeHour
            val chosenTimeMinute: Int = params[0]!!.chosenTimeMinute

            try {
                val currentDateTime: DateTime = DateTime.now(DateTimeZone.forID("America/Toronto"))
                val todayChosenDateTime: DateTime = currentDateTime.withTime(chosenTimeHour, chosenTimeMinute, 0, 0)

                publishProgress("Fetching data from UWaterloo API")
                val coursesInRoomTodayTimeFiltered: List<BuildingRoomCourse> = BuildingRoomCoursesService.getCourses(chosenBuilding, chosenRoom)
                        .filter { it.occursToday(todayChosenDateTime) }
                val allRoomsInBuilding: List<String> = BuildingRoomsListMap.getBuildingRoomsListMap()[chosenBuilding] ?:
                    throw IllegalArgumentException(ExceptionStrings.INVALID_PARAMETERS_STRING + "chosenBuilding in showResults")

                publishProgress("Finding the gaps through the courses")

                // if chosen room is not empty, look specifically for gaps for that room
                val gapsInCourses: GapSlotCollection = if (chosenRoom.isNotEmpty())
                    GapFinderService.findGapsForCoursesWithinDay(chosenBuilding, chosenRoom, coursesInRoomTodayTimeFiltered, todayChosenDateTime)
                else
                    GapFinderService.findGapsForMultipleRoomCoursesWithinDay(chosenBuilding, coursesInRoomTodayTimeFiltered,
                            todayChosenDateTime, allRoomsInBuilding)

                publishProgress("Finalizing the results")

                return gapsInCourses
            }
            catch (e: Exception) {
                e.printStackTrace(System.err)
                throw e
            }
        }

        override fun onProgressUpdate(vararg values: String?) {
            super.onProgressUpdate(*values)
            activityReference.get()?.studyAloneProcessingStatusLabel?.text = values[0]
        }

        override fun onPostExecute(result: GapSlotCollection?) {
            val context: Context? = activityReference.get()?.applicationContext
            if (result != null && context != null) {
                val intent = Intent(context, StudyAloneViewResultsActivity::class.java)
                intent.putExtra(foundGapsString, result)
                context.startActivity(intent)
            }
            else {
                val toast = Toast.makeText(context, "An error occurred. Cannot display results.", Toast.LENGTH_LONG)
                toast.show()
            }
        }
    }
}
