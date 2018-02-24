package bkhn.et.storemanage.ui.splash.view;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import bkhn.et.storemanage.base.BaseActivity;
import bkhn.et.storemanege.R;

import static bkhn.et.storemanage.util.AppConstant.TAGG;


public class SplashActivity extends BaseActivity {

    private static final String TAG=TAGG+SplashActivity.class.getSimpleName();
    @NonNull
    @Override
    protected Fragment getFragmentClass() {
        return new SplashFragment();
    }
}
