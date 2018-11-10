package com.tastescout.RoomDatabase;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

class DatabaseExecutor {
    private static final Object Lock=new Object();
    private static DatabaseExecutor sInstance;
    private final Executor deskIO;


    private DatabaseExecutor(Executor deskIO) {
        this.deskIO = deskIO;
    }

    static DatabaseExecutor getsInstance(){
        if (sInstance==null){
            synchronized (Lock){
                sInstance=new DatabaseExecutor(Executors.newSingleThreadExecutor());
            }

        }

        return sInstance;
    }

    Executor diskIo(){return deskIO;}
}
