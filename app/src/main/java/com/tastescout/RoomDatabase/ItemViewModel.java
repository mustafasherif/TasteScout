package com.tastescout.RoomDatabase;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.tastescout.Retrofit.Result;

import java.util.List;

public class ItemViewModel extends AndroidViewModel {

    private ItemsRepository itemsRepository;

    private LiveData<List<Result>> mAllItems;

    private LiveData<Result> mItem;

    ItemViewModel(@NonNull Application application, String name, String type,String user) {
        super(application);
        itemsRepository = new ItemsRepository(application,name,type,user);
        mAllItems = itemsRepository.getmAllItems();
        mItem=itemsRepository.getmItem();
    }

    public LiveData<List<Result>> getmAllItems() { return mAllItems; }

    public LiveData<Result> getmItem() { return mItem; }

    public void insert(Result item) { itemsRepository.insert(item); }

    public void delete(Result item){itemsRepository.delete(item);}
}
