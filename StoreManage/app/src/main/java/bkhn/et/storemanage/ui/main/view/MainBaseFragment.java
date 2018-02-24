package bkhn.et.storemanage.ui.main.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import bkhn.et.storemanage.base.BaseActivity;
import bkhn.et.storemanage.base.BaseFragment;
import bkhn.et.storemanage.util.AppConstant;
import bkhn.et.storemanege.R;
import butterknife.BindView;

import static bkhn.et.storemanage.util.AppConstant.TAGG;

/**
 * Created by PL_itto on 2/16/2018.
 */

public abstract class MainBaseFragment extends BaseFragment {
    private static final String TAG = TAGG + MainBaseFragment.class.getSimpleName();
    /* Views */
    @BindView(R.id.main_drawer)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.main_navigation)
    NavigationView mMainNavigation;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.main_content_layout)
    RecyclerView mMainListView;

    //Header
    ImageView mHeaderUserAvatar;
    TextView mHeaderUserName;
    TextView mHeaderUserPosition;

    //UserContent
    @BindView(R.id.main_user_avatar)
    ImageView mUserAvatar;
    @BindView(R.id.main_user_name)
    TextView mUserName;
    @BindView(R.id.main_user_position)
    TextView mUserPosition;
    @BindView(R.id.main_user_phone)
    TextView mUserPhone;
    @BindView(R.id.main_user_time_text)
    TextView mUserLoginTime;

    //Content Header
    @BindView(R.id.main_title_header_user)
    TextView mUserHeaderTitle;
    @BindView(R.id.main_title_header_content)
    TextView mContentHeaderTitle;

    protected String mUserId = null;
    BaseActivity mActivity;
    protected ProgressDialog mProgressDialog;
    protected boolean mUserLoad = false;
    protected boolean mContentLoad = false;

    @Override
    protected void setupView() {
        setupActionBar();
        setupNavigationView();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setup();
    }

    protected void setup() {
        setHasOptionsMenu(true);
        initInjection();
        mActivity = (BaseActivity) getActivity();
        checkUser();
    }

    protected void initInjection() {

    }

    /**
     * get UserId and UserMode from Previous Activity
     */
    protected void checkUser() {
        Intent intent = mActivity.getIntent();
//        mUserMode = intent.getIntExtra(AppConstant.Main.EXTRA_USER_MODE, AppConstant.UserMode.NONE);
        mUserId = intent.getStringExtra(AppConstant.Main.EXTRA_USER_ID);
    }

    protected void setupActionBar() {
        mActivity.setSupportActionBar(mToolbar);
        mActivity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (!mDrawerLayout.isDrawerOpen(Gravity.LEFT))
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                else
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Setup for Navigation in DrawerLayout
     */
    protected void setupNavigationView() {
        mMainNavigation.getMenu().clear();
//        switch (mUserMode) {
//            case AppConstant.UserMode.USER_MANAGER:
//                mMainNavigation.inflateMenu(R.menu.main_menu_manager);
//                break;
//            case AppConstant.UserMode.USER_STAFF:
//                mMainNavigation.inflateMenu(R.menu.main_menu_staff);
//                break;
//        }

        mMainNavigation.inflateMenu(getNavigationLayoutId());
        //Header
        View header = mMainNavigation.getHeaderView(0);
        mHeaderUserAvatar = header.findViewById(R.id.main_header_avatar);
        mHeaderUserName = header.findViewById(R.id.main_header_name);
        mHeaderUserPosition = header.findViewById(R.id.main_header_position);
    }

    protected abstract int getNavigationLayoutId();

    protected boolean isAllLoaded() {
        return mContentLoad && mUserLoad;
    }
}
