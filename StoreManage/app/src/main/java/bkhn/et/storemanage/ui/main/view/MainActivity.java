package bkhn.et.storemanage.ui.main.view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;

import bkhn.et.storemanage.base.BaseActivity;
import bkhn.et.storemanage.util.AppConstant;
import bkhn.et.storemanage.util.AppConstant.UserMode;

import static bkhn.et.storemanage.util.AppConstant.TAGG;

public class MainActivity extends BaseActivity {
    private static final String TAG = TAGG + MainActivity.class.getSimpleName();
    protected int mUserMode = UserMode.NONE;

    @NonNull
    @Override
    protected Fragment getFragmentClass() {
        Log.d(TAG, "getFragmentClass: ");
        switch (mUserMode) {
            case UserMode.USER_MANAGER:
                return new MainManagerFragment();
            case UserMode.USER_STAFF:
                return new MainStaffFragment();
            default:
                return null;
        }
    }

    @Override
    protected void initData() {
        mUserMode = getIntent().getIntExtra(AppConstant.Main.EXTRA_USER_MODE, UserMode.NONE);
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: ");
        super.onBackPressed();
    }
}
