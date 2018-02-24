package bkhn.et.storemanage.ui.main.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;

import javax.inject.Inject;

import bkhn.et.storemanage.di.ActivityContext;
import bkhn.et.storemanage.ui.login.view.LoginActivity;
import bkhn.et.storemanage.ui.main.IMainContract;
import bkhn.et.storemanage.ui.main.IMainContract.IMainManagerView;
import bkhn.et.storemanege.R;

import static bkhn.et.storemanage.util.AppConstant.TAGG;
import static bkhn.et.storemanage.util.AppUtils.isNotNull;

/**
 * Created by PL_itto on 2/14/2018.
 */

public class MainManagerFragment extends MainBaseFragment implements IMainManagerView {
    private static final String TAG = TAGG + MainManagerFragment.class.getSimpleName();

    /* Views */

    /* Parameters */
    @Inject
    @ActivityContext
    Context mContext;

    @Inject
    IMainContract.IMainManagerPresenter<IMainManagerView> mPresenter;

    @NonNull
    @Override
    protected int getLayoutId() {
        return R.layout.frag_main;
    }

    @Override
    protected void initInjection() {
        getFragmentComponent().inject(this);
    }

    @Override
    public void onDestroy() {
        if (isNotNull(mPresenter))
            mPresenter.onDetach();
        super.onDestroy();
    }

    @Override
    protected int getNavigationLayoutId() {
        return R.menu.main_menu_manager;
    }


    @Override
    protected void setupNavigationView() {
        super.setupNavigationView();
        mMainNavigation.setNavigationItemSelectedListener(mNavItemListener);
    }

    NavigationView.OnNavigationItemSelectedListener mNavItemListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_logout:
                    requestLogout();
                    return true;
            }
            return false;
        }
    };

    @Override
    public void openStaffManageActivity() {

    }

    @Override
    public void openStatisticActivity() {

    }

    @Override
    public void requestLogout() {
        mPresenter.logout();
        openLoginActivity();
    }

    @Override
    public void openLoginActivity() {
        Intent intent = new Intent(mActivity, LoginActivity.class);
        startActivity(intent);
        mActivity.finish();
    }
}
