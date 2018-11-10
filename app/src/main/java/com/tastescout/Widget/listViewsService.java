package com.tastescout.Widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.tastescout.R;
import com.tastescout.Retrofit.Result;

import java.util.ArrayList;

public class listViewsService extends RemoteViewsService {

    public ListViewsFactory onGetViewFactory(Intent intent)
    {
        return new ListViewsFactory(this.getApplicationContext());
    }
}

class ListViewsFactory implements RemoteViewsService.RemoteViewsFactory
{
    private Context mContext;
    private ArrayList<Result> items;

    public ListViewsFactory(Context mContext)
    {
        this.mContext = mContext;
    }

    @Override
    public void onCreate()
    {

    }

    //Very Important,this is the place where the data is being changed each time by the adapter.
    @Override
    public void onDataSetChanged()
    {
        items = ItemWidgetProvider.mItems;
    }

    @Override
    public void onDestroy()
    {

    }

    @Override
    public int getCount()
    {
        if (items == null)
            return 0;
        return items.size();
    }

    /**
     * @param position position of current view in the ListView
     * @return a new RemoteViews object that will be one of many in the ListView
     */
    @Override
    public RemoteViews getViewAt(int position)
    {
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);
        views.setTextViewText(R.id.appwidget_text, items.get(position).getName());
        switch (items.get(position).getType()) {
            case "movie":
                views.setTextViewCompoundDrawables(R.id.appwidget_text,0,0,R.drawable.ic_movie_icon,0);
                break;
            case "show":
                views.setTextViewCompoundDrawables(R.id.appwidget_text,0,0,R.drawable.ic_tv_icon,0);
                break;
            case "music":
                views.setTextViewCompoundDrawables(R.id.appwidget_text,0,0,R.drawable.ic_music_icon,0);
                break;
            case "author":
                views.setTextViewCompoundDrawables(R.id.appwidget_text,0,0,R.drawable.ic_author_icon,0);
                break;
            case  "game":
                views.setTextViewCompoundDrawables(R.id.appwidget_text,0,0,R.drawable.ic_game_icon,0);
                break;
            case "book":
                views.setTextViewCompoundDrawables(R.id.appwidget_text,0,0,R.drawable.ic_book_icon,0);
                break;
        }
        return views;
    }

    @Override
    public RemoteViews getLoadingView()
    {
        return null;
    }

    @Override
    public int getViewTypeCount()
    {
        return 1;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public boolean hasStableIds()
    {
        return true;
    }
}
