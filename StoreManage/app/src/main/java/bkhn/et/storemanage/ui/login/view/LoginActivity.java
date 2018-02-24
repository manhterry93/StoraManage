package bkhn.et.storemanage.ui.login.view;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import bkhn.et.storemanage.base.BaseActivity;

import static bkhn.et.storemanage.util.AppConstant.TAGG;

public class LoginActivity extends BaseActivity {
    private static final String TAG = TAGG + LoginActivity.class.getSimpleName();

    @NonNull
    @Override
    protected Fragment getFragmentClass() {
        return new LoginFragment();
    }

}
