package com.chronometer.widgetissue;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.util.Date;

public class WidgetProvider extends AppWidgetProvider {
    private final static String ADD_ACTION = "com.chronometer.widgetissue.ADD";
    public final static String ACTIVITY_ID = "com.chronometer.widgetissue.ACTIVITY_ID";
    public final static String STOP_ACTION = "com.chronometer.widgetissue.STOP";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int appWidgetId : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_timers);
            remoteViews.setOnClickPendingIntent(R.id.widget_add_button,
                    getPendingSelfIntent(context, ADD_ACTION));

            Intent intent = new Intent(context, WidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            remoteViews.setRemoteAdapter(R.id.widget_timers_list, intent);

            Intent clickIntent = new Intent(context, getClass());
            clickIntent.setAction(STOP_ACTION);
            PendingIntent clickPI = PendingIntent.getBroadcast(context, 0,
                    clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setPendingIntentTemplate(R.id.widget_timers_list, clickPI);

            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (ADD_ACTION.equals(intent.getAction())) {
            Activity activity = new Activity();
            activity.setStatus(0);
            activity.setStart(new Date().getTime());
            DBHelper.getInstance(context).createActivity(activity);
        }
        else if (STOP_ACTION.equals(intent.getAction())) {
            Long activityId = intent.getExtras().getLong(ACTIVITY_ID);;
            DBHelper.getInstance(context).stopActivity(activityId);
        }

        AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = widgetManager.getAppWidgetIds(new ComponentName(context, getClass()));
        widgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_timers_list);
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
