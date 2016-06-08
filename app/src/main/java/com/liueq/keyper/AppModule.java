package com.liueq.keyper;

import android.app.Application;

import com.liueq.keyper.utils.FileReader;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by liueq on 13/7/15.
 * 提供所有的SingleTon依赖
 */
@Module
public class AppModule {
    private Application application;

    public AppModule(Application application){
        this.application = application;
    }

    @Provides
    @Singleton
    public Application provideApplication(){
        return application;
    }

    @Provides
    @Singleton
    public FileReader provideFileReader(){
        return FileReader.getInstance();
    }

    @Provides
    @Singleton
    public VisibleObserver provideVisibleObserver(){
        return VisibleObserver.getInstance();
    }

}
