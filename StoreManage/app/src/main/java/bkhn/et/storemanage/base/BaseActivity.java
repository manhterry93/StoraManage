package bkhn.et.storemanage.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import bkhn.et.storemanage.util.AppUtils;
import bkhn.et.storemanage.util.NetworkUtils;
import bkhn.et.storemanege.R;


import static bkhn.et.storemanage.util.AppConstant.TAGG;
import static bkhn.et.storemanage.util.AppUtils.isNotNull;

/**
 * Created by PL_itto on 2/12/2018.
 */

public abstract class BaseActivity extends AppCompatActivity implements IBaseView {
    private static final String TAG = TAGG + BaseActivity.class.getSimpleName();

    public boolean useFragment() {
        return true;
    }

    @LayoutRes
    public int getLayoutId() {
        return R.layout.activity_default;
    }

    @NonNull
    protected abstract Fragment getFragmentClass();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initData();
        inflateFragment();
    }

    /**
     * Will be used in child class
     */
    protected void initData() {

    }

    protected void inflateFragment() {
        if (useFragment()) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            Fragment fragment = fm.findFragmentById(R.id.fragment_container);
            if (!isNotNull(fragment)) {
                fragment = getFragmentClass();
                if (isNotNull(fragment)) {
                    transaction.add(R.id.fragment_container, fragment).commit();
                } else {
                    Log.e(TAG, "Add Fragment to Activity failed\n" + "Fragment is Null");
                }
            }
        }
    }

    @Override
    public void showMessage(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessage(@NonNull String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean isNetworkConnected() {
        return NetworkUtils.isNetworkConnected(getApplicationContext());
    }
}
