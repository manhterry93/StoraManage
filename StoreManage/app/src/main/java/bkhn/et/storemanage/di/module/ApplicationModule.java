package bkhn.et.storemanage.di.module;

import android.app.Application;
import android.content.Context;

import bkhn.et.storemanage.data.DataManager;
import bkhn.et.storemanage.data.IDataManager;
import bkhn.et.storemanage.data.remote.IRemoteProvider;
import bkhn.et.storemanage.data.remote.RemoteProvider;
import bkhn.et.storemanage.di.ApplicationContext;
import dagger.Module;
import dagger.Provides;

/**
 * Created by PL_itto on 2/12/2018.
 */
@Module
public class ApplicationModule {
    private Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    IRemoteProvider provideRemoteProvider(RemoteProvider provider) {
        return provider;
    }

    @Provides
    IDataManager provideDataManager(DataManager dataManager) {
        return dataManager;
    }
}
