package bkhn.et.storemanage;

import android.app.Application;

import com.firebase.client.Firebase;

import bkhn.et.storemanage.di.component.ApplicationComponent;
import bkhn.et.storemanage.di.component.DaggerApplicationComponent;
import bkhn.et.storemanage.di.module.ApplicationModule;

import static bkhn.et.storemanage.util.AppConstant.TAGG;
import static bkhn.et.storemanage.util.AppUtils.isNotNull;

/**
 * Created by PL_itto on 2/12/2018.
 */

public class MainApp extends Application {
    private static final String TAG = TAGG + MainApp.class.getSimpleName();
    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        getApplicationComponent().inject(this);
        Firebase.setAndroidContext(this);
    }

    public ApplicationComponent getApplicationComponent() {
        if (!isNotNull(mApplicationComponent)) {
            mApplicationComponent = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this))
                    .build();
        }
        return mApplicationComponent;
    }
}
