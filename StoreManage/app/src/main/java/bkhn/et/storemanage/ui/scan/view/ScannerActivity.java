package bkhn.et.storemanage.ui.scan.view;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import bkhn.et.storemanage.base.BaseActivity;

public class ScannerActivity extends BaseActivity {

    @NonNull
    @Override
    protected Fragment getFragmentClass() {
        return new ScannerFragment();
    }
}
