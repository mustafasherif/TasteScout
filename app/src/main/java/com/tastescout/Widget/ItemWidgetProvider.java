package com.tastescout.Widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.tastescout.R;
import com.tastescout.Retrofit.Result;

import java.util.ArrayList;

public class ItemWidgetProvider extends AppWidgetProvider {

    public static ArrayList<Result> mItems;

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                       int appWidgetIds[], ArrayList<Result> items)
    {

        mItems=items;
        for (int appWidgetId : appWidgetIds)
        {
            Intent intent = new Intent(context, listViewsService.class);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.item_widget_provider);
            views.setRemoteAdapter(R.id.widgetListView, intent);
            ComponentName component = new ComponentName(context, ItemWidgetProvider.class);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widgetListView);
            appWidgetManager.updateAppWidget(component, views);
        }

    }

}
