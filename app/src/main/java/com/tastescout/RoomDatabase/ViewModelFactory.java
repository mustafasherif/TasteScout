package com.tastescout.RoomDatabase;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private Application mApplication;
    private String name;
    private String type;
    private String user;


    public ViewModelFactory(Application application, String name,String type,String user) {
        mApplication = application;
        this.name = name;
        this.type =type;
        this.user =user;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ItemViewModel(mApplication,name,type,user);
    }

}
