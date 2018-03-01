package com.chronometer.widgetissue;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.List;

public class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory  {

    private final Context context;
    private List<Activity> activityList;

    public WidgetRemoteViewsFactory(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        initActivityList();
    }

    @Override
    public void onDataSetChanged() {
        initActivityList();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return activityList.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {

        Activity activity = activityList.get(i);

        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_timer);

        rv.setChronometer(R.id.widget_timer, SystemClock.elapsedRealtime() - System.currentTimeMillis() +  activity.getStart(), null, true);

        final Intent fillInIntent = new Intent();
        final Bundle extras = new Bundle();
        fillInIntent.setAction(WidgetProvider.STOP_ACTION);
        extras.putLong(WidgetProvider.ACTIVITY_ID, activity.getId());
        fillInIntent.putExtras(extras);

        rv.setOnClickFillInIntent(R.id.widget_stop, fillInIntent);

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private void initActivityList() {
        activityList = DBHelper.getInstance(context).findActivities();
    }
}
