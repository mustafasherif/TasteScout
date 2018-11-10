package com.tastescout.RoomDatabase;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.tastescout.Retrofit.Result;

import java.util.List;

@Dao
public interface ItemDao {

    @Query("SELECT * FROM items WHERE user =:user")
    LiveData<List<Result>> getAllItems(String user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Result item);

    @Delete
    void delete(Result item);

    @Query("SELECT * FROM items WHERE Name =:name AND Type =:type AND user =:user")
    LiveData<Result> getItem(String name,String type,String user);

}
