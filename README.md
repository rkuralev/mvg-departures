# MVG Departures
The applications gets your current location and fetches upcoming departures for the nearest public transport stop.
It was created as a light-weight replacement for the official "MVG Fahrinfo" app's feature. Find more details on my motivation to create the app below.

**Supported cities and public transport operators:** Munich (MVG), including S-Bahn, but excluding BOB, BRB, RegioDB and Meridian

**Supported platforms:** Android 5.0+, requires Google Mobile Services

**APK file:** https://github.com/rkuralev/mvg-departures/releases/download/1.0/mvg-departures-1.0.apk

<img src="mvg-departures.jpg?raw=true" width="300" title="MVG Departures App Screenshot">

# Why I created the application
## Problem
I live between 2 subway stations - 2 minutes from one and 6 minutes from the other one. Whenever I am going to take a subway, 
I have to make a decision whether to go one or another direction. The decision has to be made within 30 meters I have until the intersection.

The logic for my decision is the following: I check the real-time departures list for the closes subway station and if there's less than 6 minutes
until a train arrives - I turn on the intersection right and go to the nearest station. If there's more than 6 minutes until the next train - I turn left
and go to the other station. It serves two lines so there's a lower waiting time (but I have to walk more).
## Available solutions
Munich's public transport operator MVG has the official app which solves my problem, but as it has more functionality than just live departures, I have to do up to 4 clicks to get what I need, which sometimes takes too much time.
Also, I was in a process of learning Java so it was a good chance for me to kill 2 birds with one stone.
## My solution
When app starts, it automatically determines location, finds the nearest public transport station and fetches all departures for it.
Sometimes location is not perfectly accurate, in this case one could tap a station name and chose from 10 nearest stations.

Pull-to-refresh and "update my location" features are also pretty useful and easy to use.
## Notes to implementation
* Udacity Android Developer course was the main source of information for me
* Jackson is used for JSON parsing and HttpOk3 for making HTTP/S requests. Using these libraties doesn't add too much added value, only couple of megs, but I intentionally used them just to learn.
* MVG doesn't have any official API so I checked their website and found that it uses RESTful backend. I hope that don't mind :)
