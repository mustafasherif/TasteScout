package com.tastescout.RoomDatabase;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.tastescout.Retrofit.Result;

import java.util.List;

class ItemsRepository {

    private ItemDao itemDao;
    private LiveData<List<Result>> mAllItems;
    private LiveData<Result> mItem;

    ItemsRepository(Application application,String name,String type,String user){
        ItemsDatabase db = ItemsDatabase.getDatabase(application);
        itemDao = db.itemDao();
        mAllItems = itemDao.getAllItems(user);
        mItem=itemDao.getItem(name,type,user);
    }
    LiveData<List<Result>>getmAllItems(){
        return mAllItems;
    }
    LiveData<Result>getmItem(){
        return mItem;
    }
    void insert(final Result item) {
        DatabaseExecutor.getsInstance().diskIo().execute(new Runnable() {
            @Override
            public void run() {
                itemDao.insert(item);
            }
        });
    }
    void delete(final Result item){
        DatabaseExecutor.getsInstance().diskIo().execute(new Runnable() {
            @Override
            public void run() {
                itemDao.delete(item);
            }
        });
    }


}
