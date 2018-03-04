package bkhn.et.storemanage.ui.bill.view;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import bkhn.et.storemanage.base.BaseActivity;

public class BillActivity extends BaseActivity {

    @NonNull
    @Override
    protected Fragment getFragmentClass() {
        return new BillFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
