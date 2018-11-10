package com.tastescout.RoomDatabase;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.tastescout.Retrofit.Result;

@Database(entities = Result.class,version = 1,exportSchema = false)
public abstract class ItemsDatabase extends RoomDatabase {
    public abstract ItemDao itemDao();
    private static ItemsDatabase INSTANCE;

    static ItemsDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ItemsDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ItemsDatabase.class, "items_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
