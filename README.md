# [DEPRECATED] UWatALoner #

This repository has been deprecated for the following reason:  
The app relies on the University of Waterloo Open Data API, which does not offer enough data to ensure that this app is fully reliable. For example, the API does not include the times and locations of some midterms, nor does it list information on when club meetings are held. Progress on this repository may continue if more endpoints are added to the API.

------------------------------

## Summary ##

Tired of walking around University of Waterloo's MC Building, trying to find a room that doesn't have a class going on to study in? This app's for you.
Have too much time on your hands, and want to audit a course, yet not sure where to find it? This app's for you.

## Features ##

This Android application written in Kotlin addresses two issues: finding empty classrooms on the University of Waterloo campus, and finding the location of courses that the user wants to audit.

### Finding empty classrooms ###

The libraries at the University of Waterloo are somewhat quiet, yet due to the number of people using the study spaces, empty classrooms are usually way quieter.
However, it's not easy to find an empty classroom in the middle of the day, especially in MC (the Math and Computers building).
With this application, the user enters a building, along with a time, which represents the time at which they want to enter an available room. 
For refined results, they can optionally enter a single room.
Then, the application builds a list of available rooms along with the length of the length of time available until the next lecture.
The user will eventually be able to sort the gaps by soonest-first or largest-first.

### Attending Lectures ###

Some classes do not have many students, so a user can use this application to attend a certain section of a course that they are interested in.
The user enters a subject (such as PHYS), along with the course (such as 242, where PHYS 242 is Electromagnetics 1). 
Then, the application displays all sections of the course in terms of the time, location, and professor.
The application also notes, for each section, whether there is an overenrollment. 
If so, the user should not attend that section, due to the risk of taking up a seat of a student legitimately enrolled in the course.

Screenshots will eventually be available that demonstrate both features.

## Usage of UWaterloo API ##

This application contains information provided by the University of Waterloo under license on an 'as is' basis.
