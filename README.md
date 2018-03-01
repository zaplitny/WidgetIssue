
Sample for chronometer issue - https://stackoverflow.com/questions/49026265/chronometer-doesnt-update-in-app-widget-listview

To reproduce the issue install the app and launch. Pressing on "Add timer" displays new running timer, pressing on "Stop" removes the timer

The issue is that timers often don't tick. Reproduced on Android 8. 
The issue reproduced only in ListView - Chronometer works well in RecyclerView. But unfortunately I can't migrate app widget to RecyclerView as there's no RecyclerView support in RemoteViews
