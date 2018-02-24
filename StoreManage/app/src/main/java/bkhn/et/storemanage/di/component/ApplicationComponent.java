package bkhn.et.storemanage.di.component;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import bkhn.et.storemanage.MainApp;
import bkhn.et.storemanage.data.IDataManager;
import bkhn.et.storemanage.di.ApplicationContext;
import bkhn.et.storemanage.di.module.ApplicationModule;
import dagger.Component;

/**
 * Created by PL_itto on 2/12/2018.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(MainApp app);

    @ApplicationContext
    Context getApplicationContext();

    Application getApplication();

    IDataManager getDataManager();
}
